package controllers;

import models.User;
import repositories.UserRepository;
import repositories.UserRepositoryFactory;

public class UserController {
  private UserRepository repository = UserRepositoryFactory.getInstance();

  public User create(String name, byte[] picture) {
    return repository.create(name, picture);
  }

  public void updateLocation(User user, double lat, double lon) {
    repository.setLocation(user, lat, lon);
  }

  public void update(User user) {
    repository.update(user);
  }
}
