package com.example.bookprobyjays.controller;

import com.example.bookprobyjays.common.BaseResponse;
import com.example.bookprobyjays.common.ResultUtils;
import com.example.bookprobyjays.designPatterns.OrderTypeFactory;
import com.example.bookprobyjays.designPatterns.OrderTypeService;
import com.example.bookprobyjays.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/designPatterns")
public class TestController {
    //注入参数
    @Value("${OrderType.type}")
    String type;
    @Qualifier("dealOrder")
    @Autowired
    private OrderTypeService orderTypeService;

    @GetMapping()
    public BaseResponse<Book> testDesignPatterns(Long id){
//        OrderTypeService orderTypeService = OrderTypeFactory.newInstance(type);
        return ResultUtils.success(orderTypeService.dealBook(id));
    }

}
