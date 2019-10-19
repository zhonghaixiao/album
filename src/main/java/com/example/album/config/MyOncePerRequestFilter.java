package com.example.album.config;

import org.apache.shiro.web.servlet.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class MyOncePerRequestFilter extends OncePerRequestFilter {

    private static final AtomicInteger count = new AtomicInteger();

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println("-----> once per request filter, request = " + ((HttpServletRequest)request).getRequestURI());
//        chain.doFilter(request, response);
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        chain.doFilter(request, response);
        Cookie cookie = new Cookie("name", "zhong:" + count.incrementAndGet());
        cookie.setHttpOnly(false);
        cookie.setMaxAge(24*60*60);
        cookie.setPath("/");
        cookie.setSecure(false);
        httpServletResponse.addCookie(cookie);
        httpServletResponse.sendError(200, "ok");
    }
}
