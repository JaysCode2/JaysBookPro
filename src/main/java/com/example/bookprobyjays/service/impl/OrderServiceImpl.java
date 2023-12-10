package com.example.bookprobyjays.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookprobyjays.common.ErrorCode;
import com.example.bookprobyjays.domain.Book;
import com.example.bookprobyjays.domain.Order;
import com.example.bookprobyjays.dto.BookCartDto;
import com.example.bookprobyjays.exception.BusinessException;
import com.example.bookprobyjays.service.BookService;
import com.example.bookprobyjays.service.OrderService;
import com.example.bookprobyjays.mapper.OrderMapper;
import com.example.bookprobyjays.utils.UserHolder;
import com.example.bookprobyjays.vo.BookCartVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    public final static String BOOK_CART_KEY = "book:cart:";

    /**
     * 购物车添加方法，可按数量和书籍id添加
     * @param bookCartDto
     * @return
     */
    @Override
    public BookCartDto addBookCart(BookCartDto bookCartDto) {
        String key = BOOK_CART_KEY+ UserHolder.getUser().getUserAccount();
        String userId = UserHolder.getUser().getId().toString();

        Book book = bookService.getById(bookCartDto.getBookId());
        if(book == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"所要添加进购物车的书籍信息不存在");
        }
        BookCartVo bookCartVo = BeanUtil.copyProperties(book,BookCartVo.class);
        bookCartVo.setNum(bookCartDto.getNum());

        //查询redis中是否存在该书籍，若存在，直接加数量就行了
        if(stringRedisTemplate.opsForHash().hasKey(key,book.getId().toString())){
            //从redis里拿对象数据
            String bookCartJson = (String) stringRedisTemplate.opsForHash().get(key,userId);
            BookCartVo bookCartVoUpdate = JSONUtil.toBean(bookCartJson, BookCartVo.class);
            //更新数量
            log.info("原num:{};要添加的num:{}",bookCartVoUpdate.getNum(),bookCartDto.getNum());
            bookCartVoUpdate.setNum(bookCartVoUpdate.getNum()+bookCartDto.getNum());
            log.info("相加后num:{}",bookCartVoUpdate.getNum());
            stringRedisTemplate.opsForHash().put(key,book.getId().toString(),JSONUtil.toJsonStr(bookCartVoUpdate));
            // stringRedisTemplate.expire(cartKey,10, TimeUnit.MINUTES);
        }else {
            //若不存在该书籍订单，那要添加进redis中
            stringRedisTemplate.opsForHash().put(key,book.getId().toString(), JSONUtil.toJsonStr(bookCartVo));
        }

        return bookCartDto;
    }
}




