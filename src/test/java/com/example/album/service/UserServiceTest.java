package com.example.album.service;

import com.example.album.config.SimpleToken;
import com.example.album.config.TokenType;
import com.example.album.dao.UserDao;
import com.example.album.domain.SysUser;
import com.example.album.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceTest {

    @Autowired
    UserDao userDao;

    RandomNumberGenerator generator = new SecureRandomNumberGenerator();

    @Test
    public void createUser(){
        String salt = generator.nextBytes().toHex();
        log.info("generated salt = {}", salt);
        SimpleHash hash = new SimpleHash("sha-256", "123456", salt, 3);
        log.info("password 123 hash = {}", hash);
        String userId = IdWorker.next();
        int count = userDao.createUser(SysUser.builder()
                .userId(userId)
                .userName("zhong1")
                .phone("18260631671")
                .salt(salt)
                .password(hash.toHex())
                .build());
        if (count == 1){
            log.info("insert user success!!!");
        }
        SysUser user = userDao.getUserById(userId);
        log.info("new user = {}", user);
    }

    @Test
    public void simpleLogin(){
        Subject subject = SecurityUtils.getSubject();
        SimpleToken token = SimpleToken.builder().userName("zhong1").type(TokenType.NAME).password("123456").build();
        try{
            subject.login(token);
        }catch (AuthenticationException e){
            log.error("{}", e);
        }
        assertEquals(true, subject.isAuthenticated());
    }

}
