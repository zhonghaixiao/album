package com.example.album.controller;

import com.example.album.config.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/hello")
    public Result test(){
        return Result.ok("test message");
    }

}
