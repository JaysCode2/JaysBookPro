package com.example.bookprobyjays.controller;

import com.example.bookprobyjays.common.BaseResponse;
import com.example.bookprobyjays.common.ErrorCode;
import com.example.bookprobyjays.common.ResultUtils;
import com.example.bookprobyjays.domain.Order;
import com.example.bookprobyjays.dto.BookCartDto;
import com.example.bookprobyjays.exception.BusinessException;
import com.example.bookprobyjays.service.OrderService;
import com.example.bookprobyjays.vo.BookCartVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    /**
     * 购物车添加书籍
     * @param bookCartDto
     * @return
     */
    @PostMapping("/addBookCart")
    public BaseResponse<BookCartDto> addBookCart(@RequestBody BookCartDto bookCartDto){
        if(bookCartDto == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"书籍请求参数为空");
        }
        orderService.addBookCart(bookCartDto);
        return ResultUtils.success(bookCartDto);
    }

    /**
     * 购物车删除、减少数量功能
     * @param bookCartDto
     * @return
     */
    @DeleteMapping("/deleteBookCart")
    public BaseResponse<BookCartDto> deleteBookCart(@RequestBody BookCartDto bookCartDto){
        if(bookCartDto == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"书籍请求参数为空");
        }
        orderService.deleteBookCart(bookCartDto);
        return ResultUtils.success(bookCartDto);
    }

    /**
     * 罗列购物车
     * @return
     */
    @GetMapping("/list/BookCartVoList")
    public BaseResponse<List<BookCartVo>> listBookCartVo(){
        return ResultUtils.success(orderService.listBookCartVo());
    }

    @PostMapping("/createOrder")
    public BaseResponse<Order> createOrder(){
        return ResultUtils.success(orderService.createOrder());
    }
}
