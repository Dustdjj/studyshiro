package cn.dust.server.aspect;


import cn.dust.common.utils.HttpContextUtils;
import cn.dust.common.utils.IPUtil;
import cn.dust.model.entity.SysLog;
import cn.dust.server.annotation.LogAnnotation;
import cn.dust.server.service.SysLogService;
import cn.dust.server.shiro.ShiroUtil;
import com.google.gson.Gson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author: dust
 * @Date: 2019/10/21 13:00
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;


    @Pointcut("@annotation(cn.dust.server.annotation.LogAnnotation)")
    public void logPointCut(){

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point)throws Throwable {
        long start=System.currentTimeMillis();
        Object result=point.proceed();
        long time=System.currentTimeMillis()-start;
        saveLog(point,time);

        return result;
    }

    private void saveLog(ProceedingJoinPoint point,Long time){

        MethodSignature signature=(MethodSignature)point.getSignature();
        Method method=signature.getMethod();

        SysLog sysLog=new SysLog();

//        获取请求操作的描述信息
        LogAnnotation logAnnotation=method.getAnnotation(LogAnnotation.class);
        if(logAnnotation!=null){
            sysLog.setOperation(logAnnotation.value());
        }
//        获取操作方法
        String className=point.getTarget().getClass().getName();
        String methodName=signature.getName();
        sysLog.setMethod(new StringBuilder(className).append(".").append(methodName).append("()").toString());

//        获取请求参数
        Object[] args=point.getArgs();
        String params=new Gson().toJson(args[0]);
        sysLog.setParams(params);

//        获取ip
        sysLog.setIp(IPUtil.getIpAddr(HttpContextUtils.getHttpServletRequest()));

//        获取剩下的参数
        sysLog.setCreateDate(DateTime.now().toDate());
        String userName= ShiroUtil.getUserEntity().getUsername();
        sysLog.setUsername(userName);

        sysLog.setTime(time);
        sysLogService.save(sysLog);
    }


}
