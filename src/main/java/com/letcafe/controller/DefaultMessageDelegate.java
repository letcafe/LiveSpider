package com.letcafe.controller;

import com.letcafe.conf.RedisConfig;

import java.io.Serializable;
import java.util.Map;

public class DefaultMessageDelegate implements RedisConfig.MessageDelegate {

    @Override
    public void handleMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void handleMessage(Map message) {
        System.out.println(message);
    }

    @Override
    public void handleMessage(byte[] message) {
        System.out.println(message);
    }

    @Override
    public void handleMessage(Serializable message) {
        System.out.println(message);
    }

    @Override
    public void handleMessage(Serializable message, String channel) {
        System.out.println(message);
    }
}