package com.letcafe.conf;

import com.letcafe.controller.DefaultMessageDelegate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.io.Serializable;
import java.util.*;

@Configuration
public class RedisConfig {

    public interface MessageDelegate {
        void handleMessage(String message);

        void handleMessage(Map message);

        void handleMessage(byte[] message);

        void handleMessage(Serializable message);

        /**
         * pass the channel/pattern as well
         * @param message 消息
         * @param channel 信道
         */
        void handleMessage(Serializable message, String channel);
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(new DefaultMessageDelegate());
    }

    /**
     * 通过Yaml注入RedisConnectionFactory
     * @param connectionFactory Redis 的连接工厂
     * @param messageListenerAdapter 消息监听适配器
     * @return Redis连接监听的容器
     */
    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        ChannelTopic subTopic = new ChannelTopic("__keyevent@0__:expired");
        container.addMessageListener(messageListenerAdapter, subTopic);
        return container;
    }

}
