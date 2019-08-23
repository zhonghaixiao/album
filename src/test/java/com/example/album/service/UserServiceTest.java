package com.example.album.service;

import com.example.album.config.SimpleToken;
import com.example.album.config.TokenType;
import com.example.album.dao.UserDao;
import com.example.album.domain.Role;
import com.example.album.domain.SysUser;
import com.example.album.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceTest {

    @Autowired
    JedisPool jedisPool;

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;


    RandomNumberGenerator generator = new SecureRandomNumberGenerator();

    Jedis jedis;

    @Before
    public void init(){
        jedis = jedisPool.getResource();
    }

    @After
    public void destroy(){
        if (jedis != null){
            jedis.close();
        }
    }



    @Test
    public void createUser(){
        String salt = generator.nextBytes().toHex();
        log.info("generated salt = {}", salt);
        SimpleHash hash = new SimpleHash("sha-256", "123456", salt, 3);
        log.info("password 123 hash = {}", hash);
        String userId = IdWorker.next();
        int count = userService.newUser(SysUser.builder()
                .userId(userId)
                .userName("zhong2")
                .phone("18260631672")
                .salt(salt)
                .password(hash.toHex())
                .build(), initRole());
        if (count > 0){
            log.info("insert user success!!!");
        }
        SysUser user = userDao.getUserById(userId);
        log.info("new user = {}", user);
    }

    @Test
    public void getUserInfo(){
        SysUser user = userService.getUser("1164792856871833600");
        System.out.println(user);
    }

    private List<Role> initRole() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().roleId("2").build());
        return roles;
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
        assertEquals(true, subject.hasRole("role1"));
        assertEquals(true, subject.hasRole("role2"));
        assertEquals(true, subject.isPermitted("user:*"));
        subject.execute(new Callable() {
            @Override
            public String call() throws Exception {
                return null;
            }
        });
        new Thread(subject.associateWith(new Runnable() {
            @Override
            public void run() {

            }
        })).start();
        subject.logout();
    }

    private void login(String userName, String password){
        Subject subject = SecurityUtils.getSubject();
        SimpleToken token = SimpleToken.builder().userName(userName).type(TokenType.NAME).password(password).build();
        try{
            subject.login(token);
        }catch (AuthenticationException e){
            log.error("{}", e);
        }
    }

    @Test
    public void testRetryTime(){
        for (int i = 0; i < 10; i++){
            login("zhong1", "123");
        }
    }

    @Test
    public void testPermissionResolve(){
        WildcardPermission perm1 = new WildcardPermission("user:*");
        WildcardPermission perm2 = new WildcardPermission("user:view,update");
        System.out.println(perm1.implies(perm2));
    }

    @Test
    public void testRedisZset(){
        jedis.zincrby("test:zset", 1, "hai");
    }

    @Test
    public void testBaseEncode(){
        //base64
        System.out.println(Base64.encodeToString("zhong".getBytes()));
        System.out.println(Base64.decodeToString(Base64.encodeToString("zhong".getBytes())));
        //hex
        System.out.println(Hex.encodeToString("zhong".getBytes()));
        System.out.println(Hex.decode(Hex.encodeToString("zhong".getBytes())));
        //md5 hash
        System.out.println(new Md5Hash("zhong", "123").toHex());
        //sha-256 hash
        System.out.println(new Sha256Hash("zhong", "123").toBase64());
        //simpleHash
        System.out.println(new SimpleHash("sha-256", "zhong", "123").toBase64());
        //DefaultHashService
        DefaultHashService service = new DefaultHashService();
        service.setRandomNumberGenerator(new SecureRandomNumberGenerator());
        service.setHashAlgorithmName("md5");
        service.setHashIterations(3);
        service.setPrivateSalt(new SimpleByteSource("123"));
        HashRequest request = new HashRequest.Builder()
                .setAlgorithmName("MD5").setSource(ByteSource.Util.bytes("hello"))
                .setSalt(ByteSource.Util.bytes("123")).setIterations(2).build();
        System.out.println(service.computeHash(request));
        //SecureRandomNumberGenerator 生成随机数
        SecureRandomNumberGenerator generator = new SecureRandomNumberGenerator();
        System.out.println(generator.nextBytes().toHex());
        //加密/解密
        //aes
        AesCipherService aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128);
        Key key = aesCipherService.generateNewKey();
        System.out.println("key = " + key);
        String text = "hello";
        String encryptText = aesCipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
        String text2 = new String(aesCipherService.decrypt(Hex.decode(encryptText), key.getEncoded()).getBytes());;
        System.out.println("text2 = " + text2);
        //DefaultPasswordService
        DefaultPasswordService passwordService = new DefaultPasswordService();
        System.out.println(passwordService.encryptPassword("123"));
        //HashCredentialMatcher

    }

}











