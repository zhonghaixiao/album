package com.example.album.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.AdviceFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyAdviceFilter extends AdviceFilter {

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject s = SecurityUtils.getSubject();
        Session session;
        try{
            session = s.getSession();
            System.out.println("当前session = " + session.getId());
        }catch (UnknownSessionException e){
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.addHeader("WWW-Authenticate", "Basic realm='test'");
            httpServletResponse.sendError(401, "请登录");
            return true;
        }


        if (s.isAuthenticated()){
            return true;
        }

        System.out.println("预处理");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (httpServletRequest == null){
            System.out.println("非HTTP请求");
            return false;
        }
        String basicToken = httpServletRequest.getHeader("Authorization");
        if (StringUtils.isEmpty(basicToken)){
            System.out.println("token 不能为空");
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.addHeader("WWW-Authenticate", "Basic realm='test'");
            httpServletResponse.sendError(401, "请登录");
            return true;
        }
        String[] strs = basicToken.split(" ");
        if (strs.length == 2 && StringUtils.equalsIgnoreCase("basic", strs[0])){
            String base64Token = strs[1];
            String userNameAndPassword = Base64.decodeToString(base64Token);
            String[] strArrasy = userNameAndPassword.split(":");
            String userName = strArrasy[0];
            String password = strArrasy[1];
            Subject subject = SecurityUtils.getSubject();
            try{
                subject.login(
                        SimpleToken.builder()
                                .userName(userName)
                                .password(password)
                                .type(TokenType.NAME)
                                .build());
                System.out.println("登录成功");

                return true;
            }catch (AuthenticationException e){
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.addHeader("WWW-Authenticate", "Basic realm='test'");
                httpServletResponse.sendError(401, "请登录");
                return true;
            }finally {

            }
        }
        return false;//继续
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("后处理");
    }

    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
        System.out.println("最终处理");
    }
}
