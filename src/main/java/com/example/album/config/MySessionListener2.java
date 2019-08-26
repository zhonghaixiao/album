package com.example.album.config;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;

public class MySessionListener2 extends SessionListenerAdapter {


    @Override
    public void onStart(Session session) {
        System.out.println("mySessionListener2 on start");
    }
}
