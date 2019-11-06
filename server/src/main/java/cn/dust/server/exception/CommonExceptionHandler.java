package cn.dust.server.exception;

import cn.dust.common.response.BaseResponse;
import cn.dust.common.response.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

/**
 * @Author: dust
 * @Date: 2019/10/20 14:00
 */
@RestControllerAdvice
public class CommonExceptionHandler {

    private static final Logger log= LoggerFactory.getLogger(CommonExceptionHandler.class);
    @ExceptionHandler(AuthenticationException.class)
    public BaseResponse handlerAuthorizationException(AuthenticationException e){
        log.info("无授权访问");
        return new BaseResponse(StatusCode.CurrUserHasNotPermission);
    }

}
