package com.example.album;

import com.mysql.cj.jdbc.exceptions.SQLError;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@Component
public class ExceptionInterceptor {

    @ExceptionHandler(SQLException.class)
    public void handleMysqlException(SQLException e){
        System.out.println(e);
    }

}
