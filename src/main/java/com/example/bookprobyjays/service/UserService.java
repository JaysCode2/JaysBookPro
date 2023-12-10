package com.example.bookprobyjays.service;

import com.example.bookprobyjays.common.BaseResponse;
import com.example.bookprobyjays.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookprobyjays.vo.UserLoginVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author chenjiexiang
* @description 针对表【user】的数据库操作Service
* @createDate 2023-12-09 20:59:15
*/
public interface UserService extends IService<User> {
    //注册
    UserLoginVo register(User user);

    //登入
    BaseResponse<UserLoginVo> login(User user,HttpServletRequest request);

    //退出登入(在controller层写了)

}
