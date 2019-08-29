package com.example.album.config;

import com.github.bingoohuang.utils.mail.MailMatcher;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.*;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    private static final int REDIS_CACHE_EXPIRE = 24*60*60;

    @Autowired
    JedisPool jedisPool;

    @Bean
    public TestRealm realm(){
        TestRealm testRealm = new TestRealm();
        testRealm.setCredentialsMatcher(credentialsMatcher());
        testRealm.setAuthenticationTokenClass(SimpleToken.class);
//        testRealm.setCachingEnabled(true);
//        testRealm.setAuthenticationCachingEnabled(true);
//        testRealm.setAuthorizationCacheName("");
//        testRealm.setAuthorizationCachingEnabled(true);
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
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionIdUrlRewritingEnabled(true);
        sessionManager.setSessionDAO(redisSessionDAO());
        sessionManager.setGlobalSessionTimeout(REDIS_CACHE_EXPIRE * 1000);
        sessionManager.setSessionListeners(Arrays.asList(new MySessionListener1(), new MySessionListener2()));
        return sessionManager;
    }

    @Bean
    public IRedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setJedisPool(jedisPool);
        return redisManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager());
        sessionDAO.setSessionInMemoryTimeout(REDIS_CACHE_EXPIRE);
        sessionDAO.setExpire(REDIS_CACHE_EXPIRE);
        sessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return sessionDAO;
    }

    @Bean
    public SessionIdGenerator sessionIdGenerator(){
        return new JavaUuidSessionIdGenerator();
    }

    @Bean
    public SimpleCookie sessionIdCookie(){
        SimpleCookie cookie = new SimpleCookie("admin");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        return cookie;
    }

    @Bean
    public SimpleCookie rememberMeCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager cookieRememberMeManager(){
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCipherKey(Base64.encode("123456".getBytes()));
        manager.setCookie(rememberMeCookie());
        return manager;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        securityManager.setSessionManager(sessionManager());
        securityManager.setRememberMeManager(cookieRememberMeManager());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager manager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(manager);
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("myFilter", new MyOncePerRequestFilter());
        filterMap.put("myAdviceFilter", new MyAdviceFilter());
        filterMap.put("myAuthc", new MyAuthc());
        MyPathFilter myPathFilter = new MyPathFilter();
//        myPathFilter.processPathConfig("/user/**", "roles=role1");
        filterMap.put("myPathFilter",myPathFilter);
        bean.setFilters(filterMap);

        //访问权限
        LinkedHashMap<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
//        filterChainDefinitionMap.put("/user/**", "myFilter");
        filterChainDefinitionMap.put("/user/**", "myAdviceFilter,myAuthc,myPathFilter[role1,role2]");
        filterChainDefinitionMap.put("/test/**", "myFilter");
//        filterChainDefinitionMap.put("/favicon.ico", "anon");
//        filterChainDefinitionMap.put("/user/*", "authc");
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

}
