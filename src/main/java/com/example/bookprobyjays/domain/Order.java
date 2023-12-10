package com.example.bookprobyjays.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName order
 */
@TableName(value ="order")
@Data
public class Order implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 
     */
    private String bookList;

    /**
     * 
     */
    private Date orderTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 订单是否完成
     */
    private Integer isFinished;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}