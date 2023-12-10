package com.example.bookprobyjays.vo;

import lombok.Data;

@Data
public class BookCartVo {
    private Long bookId;
    private int num;
    private String title;
    private String content;
}
