package com.example.bookprobyjays.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookprobyjays.common.ErrorCode;
import com.example.bookprobyjays.domain.Book;
import com.example.bookprobyjays.domain.Order;
import com.example.bookprobyjays.domain.Warehouse;
import com.example.bookprobyjays.dto.BookCartDto;
import com.example.bookprobyjays.exception.BusinessException;
import com.example.bookprobyjays.service.BookService;
import com.example.bookprobyjays.service.OrderService;
import com.example.bookprobyjays.mapper.OrderMapper;
import com.example.bookprobyjays.service.WarehouseService;
import com.example.bookprobyjays.utils.UserHolder;
import com.example.bookprobyjays.vo.BookCartVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
* @author chenjiexiang
* @description 针对表【order】的数据库操作Service实现
* @createDate 2023-12-09 21:07:57
*/
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private BookService bookService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private WarehouseService warehouseService;

    public final static String BOOK_CART_KEY = "book:cart:";

    /**
     * 购物车添加方法，可按数量和书籍id添加
     * @param bookCartDto
     * @return
     */
    @Override
    public BookCartDto addBookCart(BookCartDto bookCartDto) {
        String key = BOOK_CART_KEY+ UserHolder.getUser().getUserAccount();

        Book book = bookService.getById(bookCartDto.getBookId());
        if(book == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"所要添加进购物车的书籍信息不存在");
        }
        BookCartVo bookCartVo = BeanUtil.copyProperties(book,BookCartVo.class);
        bookCartVo.setNum(bookCartDto.getNum());
        bookCartVo.setBookId(book.getId());

        //查询redis中是否存在该书籍，若存在，直接加数量就行了
        if(stringRedisTemplate.opsForHash().hasKey(key,book.getId().toString())){
            //从redis里拿对象数据
            String bookCartJson = (String) stringRedisTemplate.opsForHash().get(key,book.getId().toString());
            BookCartVo bookCartVoUpdate = JSONUtil.toBean(bookCartJson, BookCartVo.class);
            //更新数量
            bookCartVoUpdate.setNum(bookCartVoUpdate.getNum()+bookCartDto.getNum());
            stringRedisTemplate.opsForHash().put(key,book.getId().toString(),JSONUtil.toJsonStr(bookCartVoUpdate));
            // stringRedisTemplate.expire(cartKey,10, TimeUnit.MINUTES);
        }else {
            //若不存在该书籍订单，那要添加进redis中
            stringRedisTemplate.opsForHash().put(key,book.getId().toString(), JSONUtil.toJsonStr(bookCartVo));
        }
        return bookCartDto;
    }

    /**
     * 购物车减少功能
     * @param bookCartDto
     * @return
     */
    @Override
    public BookCartDto deleteBookCart(BookCartDto bookCartDto) {
        String key = BOOK_CART_KEY+ UserHolder.getUser().getUserAccount();
        String bookId = bookCartDto.getBookId().toString();

        String bookCartJson = (String) stringRedisTemplate.opsForHash().get(key,bookId);
        //处理找不到订单没有响应的问题
        if(StrUtil.isBlank(bookCartJson)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"无订单数据");
        }
        BookCartVo bookCartVoUpdate = JSONUtil.toBean(bookCartJson, BookCartVo.class);

        if(bookCartDto.getNum() > bookCartVoUpdate.getNum()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"需要删除的数量大于购物车中的数量");
        }
        bookCartVoUpdate.setNum(bookCartVoUpdate.getNum() - bookCartDto.getNum());
        stringRedisTemplate.opsForHash().put(key,bookId,JSONUtil.toJsonStr(bookCartVoUpdate));

        //判断是否为0，数量为0则删除key
        if(bookCartVoUpdate.getNum() == 0){
            stringRedisTemplate.opsForHash().delete(key,bookId);
        }
        return bookCartDto;
    }

    /**
     * 罗列购物车
     * @return
     */
    @Override
    public List<BookCartVo> listBookCartVo() {
        String key = BOOK_CART_KEY+ UserHolder.getUser().getUserAccount();
        //用map获取redis的数据
        Map<Object,Object> map = stringRedisTemplate.opsForHash().entries(key);
        if(map.size() == 0){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"map无数据");
        }
        List<BookCartVo> list = new ArrayList<>();
        //把map数据取出到list中
        for(Map.Entry<Object,Object> entry : map.entrySet()){
            String cartItem = entry.getValue().toString();
            BookCartVo bookCartVo = JSONUtil.toBean(cartItem, BookCartVo.class);
            list.add(bookCartVo);
        }
        return list;
    }

    /**
     * 从购物车拿订单，完成下单功能，完成
     * 需要拓展的功能：下单后需要更新库存量，加分布式锁(已完成)
     * @return
     */
    @Transactional//涉及多表操作，需要开启事务
    @Override
    public Order createOrder() {
        //原业务实现逻辑
//        List<BookCartVo> bookCartVoList = this.listBookCartVo();
//        String bookCartVoListJson = JSONUtil.toJsonStr(bookCartVoList);
//        Order order = new Order();
//        order.setBookList(bookCartVoListJson);
//        order.setId(null);
//        order.setUserId(UserHolder.getUser().getId());
//        //对list批量整理，更新对应库存信息
//        for(BookCartVo item : bookCartVoList){
//            Warehouse warehouse = warehouseService.getById(item.getBookId());
//            if(warehouse == null){
//                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"库存量不足,1");
//            }
//            warehouse.setBookNum(warehouse.getBookNum() - item.getNum());
//            //不能为负数
//            if(warehouse.getBookNum() < 0){
//                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,item.getContent()+"库存量不足,2");
//            }
//            warehouseService.updateWarehouseBook(warehouse);
//        }
//        this.save(order);
        //重新构建，完善功能
        String lockKey = UserHolder.getUser().getUserAccount().toString();
        RLock myLock = redissonClient.getLock(lockKey);

        List<BookCartVo> bookCartVoList = this.listBookCartVo();
        String bookCartVoListJson = JSONUtil.toJsonStr(bookCartVoList);
        Order order = new Order();
        order.setBookList(bookCartVoListJson);
        order.setId(null);
        order.setUserId(UserHolder.getUser().getId());

        try {
            if(myLock.tryLock(10,10, TimeUnit.SECONDS)){

                //对list批量整理，更新对应库存信息
                for(BookCartVo item : bookCartVoList){
                    Warehouse warehouse = warehouseService.getById(item.getBookId());
                    if(warehouse == null){
                        throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"库存量不足,1");
                    }
                    warehouse.setBookNum(warehouse.getBookNum() - item.getNum());
                    //不能为负数
                    if(warehouse.getBookNum() < 0){
                        throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,item.getContent()+"库存量不足,2");
                    }
                    warehouseService.updateWarehouseBook(warehouse);
                }
                this.save(order);
            }else {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"锁定失败");
            }
        } catch (BusinessException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            myLock.unlock();
        }
        return order;
    }
}




