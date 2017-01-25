package com.scmspain.howtospring.cache;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.util.HashMap;


@Configuration
public class ApplicationConfiguration {

  @Bean
  public UserController userController(UserRepository userRepository) {
    return new UserController(userRepository);
  }

  @Bean
  public UserRepository userRepository() {
    return new SlowInMemoryUserRepository();
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .findAndRegisterModules();
  }

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

    redisConnectionFactory.setHostName("redis-server");
    redisConnectionFactory.setPort(6379);
    return redisConnectionFactory;
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf, ObjectMapper objectMapper) {
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(cf);

    objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
    redisTemplate.setDefaultSerializer(serializer);

    return redisTemplate;
  }

  @Bean
  public CacheManager cacheManager(RedisTemplate<String, String> redisTemplate) {
    RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);

    cacheManager.setDefaultExpiration(300);
    cacheManager.setUsePrefix(true);

    // Custom cache regions TTL configuration
    cacheManager.setExpires(new HashMap<String, Long>() {{
      put("user", 500L);
    }});

    return cacheManager;
  }
}
