package com.example.album.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

@Slf4j
public class MyCredentialMatcher extends HashedCredentialsMatcher {

    public MyCredentialMatcher(){
        super("sha-256");
        setHashIterations(3);
        setStoredCredentialsHexEncoded(true);
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        log.info("token = {}", token);
        log.info("info = {}", info);
        return super.doCredentialsMatch(token, info);
    }
}
