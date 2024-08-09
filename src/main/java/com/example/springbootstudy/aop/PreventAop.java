package com.example.springbootstudy.aop;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.example.springbootstudy.annotation.Prevent;
import com.example.springbootstudy.annotation.PreventStrategy;
import com.example.springbootstudy.redis.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 防刷切面实现类
 *
 * @author MCW 2022/10/16
 */
@Component
@Aspect
public class PreventAop {

   private static Logger logger=LoggerFactory.getLogger(PreventAop.class);

   @Pointcut("@annotation(com.example.springbootstudy.annotation.Prevent)")
   public void pointcut(){}


    /**
     * 处理前
     */
    @Before("pointcut()")
    public void joinPoint(JoinPoint joinPoint) throws Exception {
        String requestStr = JSON.toJSONString(joinPoint.getArgs()[0]);
        if (StrUtil.isEmpty(requestStr) || requestStr.equalsIgnoreCase("{}")){
            throw new Exception("[防刷]入参不允许为空");
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getTarget().getClass().getMethod(signature.getName(), signature.getParameterTypes());
        Prevent annotation = method.getAnnotation(Prevent.class);
        String methodFullName=method.getDeclaringClass().getName()+method.getName();
        entrance(annotation,requestStr,methodFullName);
        return;
    }

    /**
     * 入口
     */
    private void entrance(Prevent prevent,String requestStr,String methodFullName) throws Exception {
        PreventStrategy strategy = prevent.strategy();
        switch (strategy){
            case DEFAULT:
                defaultHandle(requestStr,prevent,methodFullName);
                break;
            default:
                throw new Exception("无效的策略");
        }
    }

    /**
     * 默认的处理方式
     */
    private void defaultHandle(String requestStr,Prevent prevent,String methodFullName) throws Exception {
        String base64Str=toBase64String(requestStr);
        long expire=Long.parseLong(prevent.value());

        RedisUtil redisUtil = new RedisUtil();
        String resp= (String) redisUtil.get(methodFullName+base64Str);
        if (StrUtil.isEmpty(resp)){
            redisUtil.set(methodFullName+base64Str,requestStr,expire);
        }else {
            String message=!StrUtil.isEmpty(prevent.message())?prevent.message():expire+"秒内不许重复请求";
            throw new Exception(message);
        }
    }

    private String toBase64String(String obj) throws Exception{
        if (StrUtil.isEmpty(obj)){
            return null;
        }
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] bytes = obj.getBytes(StandardCharsets.UTF_8);
        return encoder.encodeToString(bytes);
    }
}
