package com.example.album.config;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

public class MySessionListener1 implements SessionListener {
    @Override
    public void onStart(Session session) {
        System.out.println("MySessionListener1 onStart");
    }

    @Override
    public void onStop(Session session) {
        System.out.println("MySessionListener1 onStop");
    }

    @Override
    public void onExpiration(Session session) {
        System.out.println("MySessionListener1 onExpiration");
    }
}
