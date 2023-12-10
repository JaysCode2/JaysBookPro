package com.example.bookprobyjays.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.bookprobyjays.domain.Book;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author chenjiexiang
* @description 针对表【book】的数据库操作Service
* @createDate 2023-12-09 21:07:08
*/
public interface BookService extends IService<Book> {
    //查看书籍信息的查询条件
    LambdaQueryWrapper<Book> queryWrapper(Book book);
}
