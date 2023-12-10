package com.example.bookprobyjays.utils;

import com.example.bookprobyjays.vo.UserLoginVo;


public class UserHolder {
    private static final ThreadLocal<UserLoginVo> tl = new ThreadLocal<>();

    public static void saveUser(UserLoginVo user){
        tl.set(user);
    }

    public static UserLoginVo getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
