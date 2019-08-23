package com.example.album.service;

import com.example.album.dao.UserDao;
import com.example.album.domain.Role;
import com.example.album.domain.SysUser;
import org.n3r.eql.trans.spring.annotation.EqlTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public SysUser getUser(String userId){
        SysUser user = userDao.getUserById(userId);
        if (user != null){
            user.setRoles(userDao.getAllRoles(userId));
            user.setPerms(userDao.getAllPerms(userId));
        }
        return user;
    }

    public SysUser getUserByUserName(String principal){
        SysUser user = userDao.getUser(principal);
        if (user != null){
            user.setRoles(userDao.getAllRoles(user.getUserId()));
            user.setPerms(userDao.getAllPerms(user.getUserId()));
        }
        return user;
    }

    @EqlTransactional
    public int newUser(SysUser user, List<Role> roles){
        userDao.createUser(user);
        List<String> roleStrs =  roles.stream().map(Role::getRoleId).collect(Collectors.toList());
        int r = userDao.insertRole(user.getUserId(), roleStrs);
        return r;
    }


}









