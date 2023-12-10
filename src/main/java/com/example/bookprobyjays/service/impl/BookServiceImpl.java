package com.example.bookprobyjays.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookprobyjays.domain.Book;
import com.example.bookprobyjays.service.BookService;
import com.example.bookprobyjays.mapper.BookMapper;
import org.springframework.stereotype.Service;

/**
* @author chenjiexiang
* @description 针对表【book】的数据库操作Service实现
* @createDate 2023-12-09 21:07:08
*/
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
    implements BookService{

}




