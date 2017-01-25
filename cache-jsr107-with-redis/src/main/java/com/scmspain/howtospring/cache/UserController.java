package com.scmspain.howtospring.cache;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RequestMapping("/users")
@RestController
public class UserController {
  private UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<User> get(@PathVariable("id") String key) {
    return Optional.ofNullable(userRepository.findBy(key))
        .map(ResponseEntity::ok)
        .orElseGet(this::notFound);

  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> post(@RequestBody User user) {
    userRepository.persist(user.getId(), user);

    return ResponseEntity.created(
        UriComponentsBuilder.fromPath("/users/{id}").buildAndExpand(user.getId()).toUri()
    ).build();
  }

  private <T> ResponseEntity<T> notFound() {
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

}
