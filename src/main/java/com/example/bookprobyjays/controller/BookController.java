package com.example.bookprobyjays.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookprobyjays.common.BaseResponse;
import com.example.bookprobyjays.common.ErrorCode;
import com.example.bookprobyjays.common.ResultUtils;
import com.example.bookprobyjays.domain.Book;
import com.example.bookprobyjays.dto.BookCartDto;
import com.example.bookprobyjays.exception.BusinessException;
import com.example.bookprobyjays.service.BookService;
import com.example.bookprobyjays.service.OrderService;
import com.example.bookprobyjays.utils.UserHolder;
import com.example.bookprobyjays.vo.BookCartVo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/book")
@Api(tags = "书籍管理接口")
public class BookController {
    @Resource
    private BookService bookService;
    @Resource
    private OrderService orderService;

    /**
     * 添加书籍
     * @param book
     * @return
     */
    @Operation(summary = "添加书籍",description = "添加书籍功能")
    @PostMapping("/add")
    public BaseResponse<Book> addBook(@RequestBody Book book){
        if(StringUtils.isAnyBlank(book.getContent(),book.getTitle())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"书籍请求参数为空");
        }
        book.setUserId(UserHolder.getUser().getId());
        bookService.save(book);
        /**
         * 难受了，突然发现如果不用新的实体来拿数据的话，前端传的book对象里的id属性默认为0，导致了触发mysql自增
         * 没办法用@TableId(type = IdType.ASSIGN_ID)没办法实现雪花算法生成唯一id,所以前端要设null值的id
         */
//        Book newBook = new Book();
//        newBook.setContent(book.getContent());
//        newBook.setTitle(book.getTitle());
//        newBook.setUserId(UserHolder.getUser().getId());
//        bookService.save(newBook);
        return ResultUtils.success(book);
    }

    /**
     * 删除书籍
     * @param id
     * @return
     */
    @Operation(summary = "删除书籍",description = "根据bookId删除书籍功能")
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteBook(@PathVariable long id){
        Book book = bookService.getById(id);
        if(book == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"所要删除的书籍信息不存在");
        }
        boolean deleteBook = bookService.removeById(book);
        return ResultUtils.success(deleteBook);
    }

    /**
     * 更改书籍信息
     * @param book
     * @return
     */
    @Operation(summary = "更改书籍信息",description = "更改书籍信息功能")
    @PutMapping("/update")
    public BaseResponse<Book> updateBook(@RequestBody Book book){
        if(book == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"书籍请求参数为空");
        }
        Book updateBook = bookService.getById(book);
        if(updateBook == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"要更改的书籍信息不存在");
        }
        book.setUserId(UserHolder.getUser().getId());
        bookService.updateById(book);
        return ResultUtils.success(book);
    }

    /**
     * 书籍条分页件，但是传参数最好做成dto类传
     * @param current
     * @param pageSize
     * @param book
     * @return
     */
    @Operation(summary = "分页查询书籍",description = "分页查询书籍功能，同时提供了title和content查询方式")
    @GetMapping("/list/pageByWrapper")
    public BaseResponse<Page<Book>> getBookPage(long current,long pageSize,Book book){
        Page<Book> pageInfo = bookService.page(new Page<>(current,pageSize),
                bookService.queryWrapper(book));
        return ResultUtils.success(pageInfo);
    }
}
