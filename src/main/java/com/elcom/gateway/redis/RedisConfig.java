package com.elcom.gateway.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 *
 * @author ducnh
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory
                = new JedisConnectionFactory();
        jedisConFactory.setHostName(redisHost);
        jedisConFactory.setPort(redisPort);
        jedisConFactory.setPassword(password);
        return jedisConFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        //create redistemplate
        //key string , value object
        //Redis template giup springboot thao tac redis
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        configureSerializers(template);
        template.setEnableTransactionSupport(true);

        return template;
    }

    private void configureSerializers(RedisTemplate<String, Object> redisTemplate) {
        //Use Jackson 2Json RedisSerializer to serialize and deserialize the value of redis (default JDK serialization)
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        // Specify the fields, fields, get and set to be serialized, and the range of modifiers. ANY includes both private and public.
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // Specify the type of serialized input, the class must be non-final modified, final modified classes, such as String,Integer, etc., will run out of exceptions
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);
        // Values are serialized using json
        redisTemplate.setValueSerializer(jacksonSeial);
        //Use String RedisSerializer to serialize and deserialize the key value of redis
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // Setting hash key and value serialization mode
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jacksonSeial);
        redisTemplate.afterPropertiesSet();

    }
}
