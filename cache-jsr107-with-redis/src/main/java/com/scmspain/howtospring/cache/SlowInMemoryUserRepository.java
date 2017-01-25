package com.scmspain.howtospring.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SlowInMemoryUserRepository implements UserRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(SlowInMemoryUserRepository.class);

  private Map<String, User> data = new HashMap<String, User>() {{
    put("1", new User("1", "mom"));
    put("2", new User("2", "dad"));
    put("3", new User("2", "grandpa"));
  }};

  @Override
  @CacheResult(cacheName = "user")
  public User findBy(String id) {
    slowOperation();
    return data.get(id);
  }

  @Override
  @CacheRemove(cacheName = "user")
  public void persist(@CacheKey String id, User user) {
    data.put(id, user);
  }

  @SuppressWarnings("EmptyCatchBlock")
  private void slowOperation() {
    try {
      LOGGER.info("Performing slow operation...");
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
    }
  }
}
