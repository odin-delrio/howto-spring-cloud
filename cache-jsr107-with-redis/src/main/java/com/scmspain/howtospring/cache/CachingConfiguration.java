package com.scmspain.howtospring.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.jcache.config.JCacheConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CachingConfiguration extends JCacheConfigurerSupport {

  @Override
  @Bean
  public CacheErrorHandler errorHandler() {
    return new NoOpCacheErrorHandler();
  }

  private static class NoOpCacheErrorHandler implements CacheErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoOpCacheErrorHandler.class);

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
      LOGGER.warn("Error while reading from cache the object with key '{}'", key, exception);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
      LOGGER.warn("Error while putting in cache the object with key '{}'", key, exception);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
      LOGGER.warn("Error while evict from cache the object with key '{}'", key, exception);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
      LOGGER.warn("Error while clearing the cache.", exception);
    }
  }
}
