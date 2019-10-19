package com.example.album.controller;

import com.example.album.config.Result;
import com.example.album.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("{userId}")
    public Result getUser(@PathVariable String userId){

        return Result.ok(userService.getUser(userId));
    }

    @GetMapping("test")
    public Result getTest(){
        return Result.ok("hello");
    }

}
