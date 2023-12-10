package com.example.bookprobyjays.service;

import com.example.bookprobyjays.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookprobyjays.dto.BookCartDto;
import com.example.bookprobyjays.vo.BookCartVo;

import java.util.List;

/**
* @author chenjiexiang
* @description 针对表【order】的数据库操作Service
* @createDate 2023-12-09 21:07:57
*/
public interface OrderService extends IService<Order> {
    //购物车添加书籍
    BookCartDto addBookCart(BookCartDto bookCartDto);
    //减少购物车书籍数量
    BookCartDto deleteBookCart(BookCartDto bookCartDto);
    //列出购物车订单
    List<BookCartVo> listBookCartVo();


    //现在开始做下单功能
    //下单功能，从redis拿购物车数据，存入order表
    Order createOrder();
}
