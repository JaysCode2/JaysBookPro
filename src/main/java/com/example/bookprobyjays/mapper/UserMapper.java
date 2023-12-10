package com.example.bookprobyjays.mapper;

import com.example.bookprobyjays.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
/**
* @author chenjiexiang
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-12-09 20:59:15
* @Entity com.example.bookprobyjays.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

}




