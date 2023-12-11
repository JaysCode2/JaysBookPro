package com.example.bookprobyjays.controller;

import com.example.bookprobyjays.common.BaseResponse;
import com.example.bookprobyjays.common.ErrorCode;
import com.example.bookprobyjays.common.ResultUtils;
import com.example.bookprobyjays.domain.User;
import com.example.bookprobyjays.exception.BusinessException;
import com.example.bookprobyjays.service.UserService;
import com.example.bookprobyjays.utils.UserHolder;
import com.example.bookprobyjays.vo.UserLoginVo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.example.bookprobyjays.utils.RedisConstants.LOGIN_USER_KEY;

@RestController
@Slf4j
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 登入
     * @param user
     * @param request
     * @return
     */
    @Operation(summary = "用户登入" ,description = "用户登入接口")
    @PostMapping("/login")
    public BaseResponse login(@RequestBody User user, HttpServletRequest request){
        if(user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"user为空");
        }
        return ResultUtils.success(userService.login(user,request));
    }
    @Operation(summary = "用户退出",description = "用户退出接口")
    @PostMapping("logout")
    public BaseResponse<String> logout(HttpServletRequest request){
        //没办法呀，前面挖坑了
        //真正的流程应该是移除redis中的token才对呀
        String token = (String) request.getSession().getAttribute("token");
        stringRedisTemplate.delete(LOGIN_USER_KEY+token);
        request.getSession().removeAttribute("token");
        log.info("退出登入");
        return ResultUtils.success("退出登入成功");
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Operation(summary = "用户注册",description = "用户注册接口")
    @PostMapping("/register")
    public BaseResponse<UserLoginVo> register(@RequestBody User user){
        return ResultUtils.success(userService.register(user));
    }

    /**
     * 测试ThreadLocal里的信息能不能拿到
     * @return
     */
    @Operation(summary = "获取登录用户信息",description = "获取登录用户信息接口")
    @GetMapping("/getLoginUser")
    public UserLoginVo getLoginUser(){
        UserLoginVo userLoginVo = UserHolder.getUser();
        log.info("========看这里:{}",userLoginVo);
        return UserHolder.getUser();
    }
}
