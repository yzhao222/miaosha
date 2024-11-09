package org.example.controller;

import org.example.error.BusinessException;
import org.example.error.EmBusinessError;
import org.example.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";
    //定义exceptionhandler解决未被controller层吸收的异
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public Object handlerException(HttpServletRequest httpServerRequest, Exception exception){
//        Map<String,Object> responseData=new HashMap<>();
//        if(exception instanceof BusinessException){
//            BusinessException businessException=(BusinessException)exception;
//            responseData.put("errorCode",businessException.getErrorCode());
//            responseData.put("errorMessage",businessException.getErrorMessage());
//        }else {
//            responseData.put("errorCode", EmBusinessError.UNKNOWN_ERROR.getErrorCode());
//            responseData.put("errorMessage",EmBusinessError.UNKNOWN_ERROR.getErrorMessage());
//        }
//        return  CommonReturnType.create(responseData,"fail");
//
//    }
}
