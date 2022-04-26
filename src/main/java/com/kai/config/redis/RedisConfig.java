package com.kai.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author kai
 * @date 2022/3/12 14:03
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //redis键采用String序列化方式
        //redis值采用JSON序列化方式(GenericJackson2JsonRedisSerializer)，额外存储了类型信息
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

//    /**
//     * 采用JSON序列化方式(Jackson2JsonRedisSerializer)
//     */
//    @Bean
//    public RedisSerializer<Object> redisSerializer() {
//        //创建JSON序列化器
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        //必须设置，否则无法将JSON转化为对象，会转化成Map类型
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,ObjectMapper.DefaultTyping.NON_FINAL);
//        serializer.setObjectMapper(objectMapper);
//        return serializer;
//    }

}

