package com.scmspain.howtospring.cache;

public interface UserRepository {

  User findBy(String key);

  void persist(String id, User user);
}
