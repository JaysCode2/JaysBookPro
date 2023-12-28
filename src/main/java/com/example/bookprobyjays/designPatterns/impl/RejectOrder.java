package com.example.bookprobyjays.designPatterns.impl;

import com.example.bookprobyjays.designPatterns.OrderTypeService;
import com.example.bookprobyjays.domain.Book;
import com.example.bookprobyjays.service.BookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class RejectOrder implements OrderTypeService {
    @Resource
    private BookService bookService;
    @Override
    public Book dealBook(long id) {
        Book book = bookService.getById(id);
        book.setTitle("这是被reject的书籍");
        return book;
    }
}
