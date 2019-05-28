package com.letcafe.controller;

import com.letcafe.conf.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

public class DefaultMessageDelegate implements RedisConfig.MessageDelegate {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageDelegate.class);

    @Override
    public void handleMessage(String message) {
        logger.info(message);
    }

    @Override
    public void handleMessage(Map message) {
        logger.info(message.toString());
    }

    @Override
    public void handleMessage(byte[] message) {
        logger.info(Arrays.toString(message));
    }

    @Override
    public void handleMessage(Serializable message) {
        logger.info(message.toString());
    }

    @Override
    public void handleMessage(Serializable message, String channel) {
        logger.info(message.toString());
    }
}