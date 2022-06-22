//package com.yaoxiong.retail.gateway.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.BeanClassLoaderAware;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.security.jackson2.SecurityJackson2Modules;
//
//@Configuration
//public class SessionConfig implements BeanClassLoaderAware {
//    private ClassLoader loader;
//
//    private ObjectMapper objectMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModules(SecurityJackson2Modules.getModules(this.loader));
//        return mapper;
//    }
//
//    @Bean
//    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
//        return new GenericJackson2JsonRedisSerializer(objectMapper());
//    }
//
//    @Override
//    public void setBeanClassLoader(ClassLoader classLoader) {
//        this.loader = classLoader;
//    }
//}