package com.example.simple_template.config;

import com.example.simple_template.common.util.R;
import com.example.simple_template.exception.OwnException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice注解是Spring MVC和Spring Boot应用程序中用于定义全局异常处理类的注解，它是@ControllerAdvice注解的特殊版本，用于RESTful风格的应用程序。
//@RestControllerAdvice可以捕获整个应用程序中抛出的异常，并对它们进行处理。这样可以实现在整个应用程序范围内统一处理异常的目标；
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public R exceptionhandler(Exception e) {
        log.error("执行异常", e);
        // 后端自定义的数据校验异常
        if(e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            return R.error(exception.getBindingResult().getFieldError().getDefaultMessage());
        }else if(e instanceof OwnException) {
            OwnException ownException = (OwnException) e;
            return R.error(ownException.getCode(), ownException.getMsg());
        }else if(e instanceof AuthenticationException){
            return R.error(401, "You are not authenticated");
        }else if(e instanceof AccessDeniedException){
            return R.error(403,"You do not have the required permissions");
        }else {
            return R.error("Backend execution error");
        }
    }
}
