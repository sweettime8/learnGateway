/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.messaging.rabbitmq;

/**
 *
 * @author Admin
 */
import com.elcom.gateway.config.GatewayConfig;
import java.util.concurrent.TimeUnit;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Link management
 *
 * @author kiven·ing
 */
@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean("connectionFactory")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setChannelCacheSize(40);
        
        // set the heartbeat timeout to 60 seconds
        connectionFactory.setRequestedHeartBeat(60);
        
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        //rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        //rabbitTemplate.setReceiveTimeout(TimeUnit.SECONDS.toMillis(10));
        rabbitTemplate.setReplyTimeout(TimeUnit.SECONDS.toMillis(GatewayConfig.RPC_TIMEOUT));
        return rabbitTemplate;
    }

    @Bean("directAutoDeleteQueue")
    public Queue directAutoDeleteQueue() {
        return new AnonymousQueue();
    }
}
