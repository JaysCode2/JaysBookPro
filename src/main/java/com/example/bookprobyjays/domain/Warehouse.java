package com.example.bookprobyjays.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 存储书籍库存信息的表
 * @TableName warehouse
 */
@TableName(value ="warehouse")
@Data
public class Warehouse implements Serializable {
    /**
     * 每本书的唯一标识
     */
    @TableId
    private Long bookId;

    /**
     * 书籍库存量，不能小于零
     */
    private Integer bookNum;

    /**
     * 最新一次变动时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}