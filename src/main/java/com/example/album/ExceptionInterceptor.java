package com.example.album;

import com.example.album.config.NeedAuthenticateException;
import com.example.album.config.NeedAuthorizeException;
import com.example.album.config.Result;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Component
@RestControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler(UnauthorizedException.class)
    public void handleUnauthorized(HttpServletRequest request, HttpServletResponse response){
        try {
            response.sendError(403);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @ExceptionHandler(UnauthenticatedException.class)
    public void handleUnAuthenticate(HttpServletRequest request, HttpServletResponse response){
        try {
            response.sendError(403);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ExceptionHandler(value = {NeedAuthenticateException.class, NeedAuthorizeException.class})
    public void handleNeedAuthenticate(HttpServletRequest request, HttpServletResponse response){
        try {
            response.sendError(403);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ExceptionHandler(SQLException.class)
    public void handleMysqlException(SQLException e){
        System.out.println(e);
    }

    @ExceptionHandler(ExcessiveAttemptsException.class)
    public void handleExcessiveAttemptsException(SQLException e){
        System.out.println("密码当日重试次数超过3次");
    }

}
