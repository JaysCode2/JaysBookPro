package com.example.bookprobyjays.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookprobyjays.common.BaseResponse;
import com.example.bookprobyjays.common.ErrorCode;
import com.example.bookprobyjays.common.ResultUtils;
import com.example.bookprobyjays.domain.User;
import com.example.bookprobyjays.exception.BusinessException;
import com.example.bookprobyjays.service.UserService;
import com.example.bookprobyjays.mapper.UserMapper;
import com.example.bookprobyjays.vo.UserLoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.bookprobyjays.utils.RedisConstants.LOGIN_USER_KEY;
import static com.example.bookprobyjays.utils.RedisConstants.LOGIN_USER_TTL;

/**
* @author chenjiexiang
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-12-09 20:59:15
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private  static final String SALT = "jays";

    /**
     * 登入
     * @param user
     * @param request
     * @return
     */
    @Override
    public BaseResponse login(User user, HttpServletRequest request) {
        String userAccount = user.getUserAccount();
        String userPassword = user.getUserPassword();
        //校验
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"账号或密码错误!!!");
        }
        if (userAccount.length() < 4) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"账号错误!!!");
        }
        if (userPassword.length() < 8) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"密码错误!!!");
        }
        //密码加密，注意了，md5并非加密算法
        String userPasswordMd5 = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount,userAccount)
                    .eq(User::getUserPassword,userPasswordMd5);
        User loginUser = this.getOne(queryWrapper);
        if(loginUser == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"找不到该用户");
        }
        if(loginUser.getIsDelete() == 1){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"该用户已被删除");
        }
//        request.getSession().setAttribute("loginUser",loginUser);
        String token = UUID.randomUUID().toString(true);
        String tokenKey = LOGIN_USER_KEY+token;
        UserLoginVo userLoginVo = BeanUtil.copyProperties(loginUser,UserLoginVo.class);
        //转map存入redis
        Map<String, Object> userMap = BeanUtil.beanToMap(userLoginVo, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        stringRedisTemplate.opsForHash().putAll(tokenKey,userMap);
        //设置过期时间
        stringRedisTemplate.expire(tokenKey,LOGIN_USER_TTL, TimeUnit.MINUTES);

        //注意一下，真实开发不用存token进session的，return给前端就行了，因为没前端所以我在这只能存进session模拟给前端token
        request.getSession().setAttribute("token",token);
        return ResultUtils.success(token);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public UserLoginVo register(User user) {
        String userAccount = user.getUserAccount();
        String userPassword = user.getUserPassword();
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        //md5处理
        String userPasswordMd5 = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        //赋值
        User registerUser = new User();
        registerUser.setUserAccount(userAccount);
        registerUser.setUserPassword(userPasswordMd5);
        //保存新用户
        this.save(registerUser);
        //脱敏
        UserLoginVo userLoginVo = BeanUtil.copyProperties(registerUser,UserLoginVo.class);
        return userLoginVo;
    }
}




