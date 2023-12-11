package com.example.bookprobyjays.controller;

import com.example.bookprobyjays.common.BaseResponse;
import com.example.bookprobyjays.common.ErrorCode;
import com.example.bookprobyjays.common.ResultUtils;
import com.example.bookprobyjays.domain.Order;
import com.example.bookprobyjays.dto.BookCartDto;
import com.example.bookprobyjays.exception.BusinessException;
import com.example.bookprobyjays.service.OrderService;
import com.example.bookprobyjays.vo.BookCartVo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/order")
@Api(tags = "订单管理接口")
public class OrderController {
    @Resource
    private OrderService orderService;

    /**
     * 购物车添加书籍
     * @param bookCartDto
     * @return
     */
    @Operation(summary = "购物车添加书籍",description = "可以根据所需书籍的id和数量在购物车添加书籍")
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
    @Operation(summary = "购物车删除、减少数量功能",description = "可以根据所需书籍的id和数量在购物车删除书籍")
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
    @Operation(summary = "罗列购物车",description = "罗列redis中存入的购物车信息")
    @GetMapping("/list/BookCartVoList")
    public BaseResponse<List<BookCartVo>> listBookCartVo(){
        return ResultUtils.success(orderService.listBookCartVo());
    }

    /**
     * 下订单
     * @return
     */
    @Operation(summary = "下订单",description = "把当前用户的购物车书籍全部下订单")
    @PostMapping("/createOrder")
    public BaseResponse<Order> createOrder(){
        return ResultUtils.success(orderService.createOrder());
    }
}
