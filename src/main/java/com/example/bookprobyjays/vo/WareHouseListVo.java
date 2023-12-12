package com.example.bookprobyjays.vo;

import lombok.Data;

@Data
public class WareHouseListVo {
    /**
     * 每本书的唯一标识
     */
    private Long bookId;

    /**
     * 书籍库存量，不能小于零
     */
    private Integer bookNum;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    private static final long serialVersionUID = 1L;
}
