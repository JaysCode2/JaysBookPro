package com.example.bookprobyjays.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.example.bookprobyjays.vo.UserLoginVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

public class LoginInterceptor implements HandlerInterceptor {


    private StringRedisTemplate stringRedisTemplate;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1. 获取请求头中的token
//        String token = request.getHeader("authorization");
//        if(StrUtil.isBlank(token)){
//            response.setStatus(401);
//            /**
//             * 这里我先放行了，因为没前端，传不了token
//             */
//            return true;
//        }
        /**
         * 你看到这里可能会疑惑，但是我没前端啊我只能用session来存这个token了
         */
        String token = (String) request.getSession().getAttribute("token");
        //2.获取基于token获取redis中的用户
        Map<Object,Object> userMap = stringRedisTemplate
                .opsForHash().entries(RedisConstants.LOGIN_USER_KEY+token);
        if(userMap.isEmpty()){
            response.setStatus(401);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("redis里面找不到用户信息哦");
            return false;
        }
        //3. 判断用户是否存在
        if (user == null){
            //4. 不存在，拦截
            response.setStatus(401);
            return false;
        }
        // 将查询到的Hash数据转化为UserLoginVo对象
        UserLoginVo userLoginVo = BeanUtil.fillBeanWithMap(userMap,new UserLoginVo(),false);
        // 存在 保存用户信息到ThreadLocal
        UserHolder.saveUser(userLoginVo);
        // 刷新token有效期
        stringRedisTemplate
                .expire(RedisConstants.LOGIN_USER_KEY+token,RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        // 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移除用户
        UserHolder.removeUser();
    }
}
