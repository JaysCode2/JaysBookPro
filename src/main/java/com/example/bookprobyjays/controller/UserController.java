package com.example.bookprobyjays.controller;

import com.example.bookprobyjays.common.BaseResponse;
import com.example.bookprobyjays.common.ErrorCode;
import com.example.bookprobyjays.common.ResultUtils;
import com.example.bookprobyjays.domain.User;
import com.example.bookprobyjays.exception.BusinessException;
import com.example.bookprobyjays.service.UserService;
import com.example.bookprobyjays.utils.UserHolder;
import com.example.bookprobyjays.vo.UserLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.example.bookprobyjays.utils.RedisConstants.LOGIN_USER_KEY;

@RestController
@Slf4j
@RequestMapping("/user")
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
    @PostMapping("/login")
    public BaseResponse login(@RequestBody User user, HttpServletRequest request){
        if(user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"user为空");
        }
        return ResultUtils.success(userService.login(user,request));
    }
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
    @PostMapping("/register")
    public BaseResponse<UserLoginVo> register(@RequestBody User user){
        return ResultUtils.success(userService.register(user));
    }

    /**
     * 测试ThreadLocal里的信息能不能拿到
     * @return
     */
    @GetMapping("/getLoginUser")
    public UserLoginVo getLoginUser(){
        UserLoginVo userLoginVo = UserHolder.getUser();
        log.info("========看这里:{}",userLoginVo);
        return UserHolder.getUser();
    }
}
