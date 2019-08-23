package com.example.album.config;

import com.example.album.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
public class MyCredentialMatcher extends HashedCredentialsMatcher {

    private static final String PASSWORD_RETRY_PREFIX = "password:retry:times";//password:retry--->count--->[userId]
    private static final int secondsOfDay = 24*60*60*60;

    @Autowired
    JedisPool jedisPool;

    public MyCredentialMatcher(){
        super("sha-256");
        setHashIterations(3);
        setStoredCredentialsHexEncoded(true);
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        log.info("token = {}", token);
        log.info("info = {}", info);
        if (!(info instanceof SysUser)){
            throw new RuntimeException("认证信息类型错误");
        }
        SysUser user = (SysUser) info;
        boolean isSuccess;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            isSuccess = super.doCredentialsMatch(token, info);
            if (!isSuccess){
                int retry = jedis.zincrby(PASSWORD_RETRY_PREFIX, 1, (String) user.getUserId()).intValue();
                jedis.expire(PASSWORD_RETRY_PREFIX, secondsOfDay - DateTime.now().getMillisOfDay()/1000);
                if (retry > 3) {
                    throw new ExcessiveAttemptsException("当日密码重试次数超过限制");
                }
            }

        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return isSuccess;
    }
}
