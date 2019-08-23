package com.example.album;

import com.mysql.cj.jdbc.exceptions.SQLError;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@Component
public class ExceptionInterceptor {

    @ExceptionHandler(SQLException.class)
    public void handleMysqlException(SQLException e){
        System.out.println(e);
    }

    @ExceptionHandler(ExcessiveAttemptsException.class)
    public void handleExcessiveAttemptsException(SQLException e){
        System.out.println("密码当日重试次数超过3次");
    }

}
