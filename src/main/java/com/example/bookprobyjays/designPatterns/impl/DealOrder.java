package com.example.bookprobyjays.designPatterns.impl;

import com.example.bookprobyjays.designPatterns.OrderTypeService;
import com.example.bookprobyjays.domain.Book;
import com.example.bookprobyjays.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class DealOrder implements OrderTypeService {
    @Resource
    private BookService bookService;
    @Override
    public Book dealBook(long id) {
        log.info("id={}",id);
        Book book = bookService.getById(id);
        book.setTitle("这是被deal的书籍信息");
        return book;
    }
}
