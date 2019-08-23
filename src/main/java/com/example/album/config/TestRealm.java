package com.example.album.config;

import com.example.album.dao.UserDao;
import com.example.album.domain.SysUser;
import com.example.album.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
public class TestRealm extends AuthorizingRealm {

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof SimpleToken) {
            return true;
        }
        return super.supports(token);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String principal = (String) principalCollection.getPrimaryPrincipal();
        log.info("principal = {}", principal);
        SysUser user = userService.getUserByUserName(principal);
        return user;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token instanceof SimpleToken){
            log.info("input token = {}", token);
            SimpleToken realToken = (SimpleToken) token;
            String principal = realToken.getPrincipal();
            SysUser user = userDao.getUser(principal);
            if (user == null) {
                throw new UnknownAccountException();
            }
            if (user.getLocked().equals("1")){
                throw new LockedAccountException();
            }
            return user;
        }
        return null;
    }
}
