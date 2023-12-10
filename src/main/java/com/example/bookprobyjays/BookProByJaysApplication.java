package com.example.bookprobyjays;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.bookprobyjays.mapper")
public class BookProByJaysApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookProByJaysApplication.class, args);
    }

}
