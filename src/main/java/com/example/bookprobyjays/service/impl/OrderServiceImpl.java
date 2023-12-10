package com.example.bookprobyjays.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookprobyjays.domain.Order;
import com.example.bookprobyjays.service.OrderService;
import com.example.bookprobyjays.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author chenjiexiang
* @description 针对表【order】的数据库操作Service实现
* @createDate 2023-12-09 21:07:57
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




