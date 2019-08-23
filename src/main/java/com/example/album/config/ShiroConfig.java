package com.example.album.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

    @Bean
    public TestRealm realm(){
        TestRealm testRealm = new TestRealm();
        testRealm.setCredentialsMatcher(credentialsMatcher());
        testRealm.setAuthenticationTokenClass(SimpleToken.class);
        return testRealm;
    }

    @Bean
    public CredentialsMatcher credentialsMatcher(){
//        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
//        matcher.setHashAlgorithmName("sha-256");
//        matcher.setHashIterations(3);
//        matcher.setStoredCredentialsHexEncoded(true);
        return new MyCredentialMatcher();
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

}
