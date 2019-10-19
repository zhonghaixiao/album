package com.example.album.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Slf4j
public class MyPathFilter extends PathMatchingFilter {

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        log.info("mappedValue = {}", mappedValue);
        if (mappedValue == null) {
            return false;
        }
        String[] accessStrs = (String[]) mappedValue;
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasAllRoles(Arrays.asList(accessStrs))){
            return true;
        }else{
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.addHeader("WWW-Authenticate", "Basic realm='test'");
            httpServletResponse.sendError(401, "请登录");
            return false;
        }
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
        super.postHandle(request, response);
    }
}
