package com.example.bookprobyjays.service;

import com.example.bookprobyjays.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookprobyjays.dto.BookCartDto;

/**
* @author chenjiexiang
* @description 针对表【order】的数据库操作Service
* @createDate 2023-12-09 21:07:57
*/
public interface OrderService extends IService<Order> {
    //购物车添加书籍
    BookCartDto addBookCart(BookCartDto bookCartDto);
}
