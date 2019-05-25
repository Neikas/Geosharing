package repositories;

import models.User;

public interface UserRepository {
  User create(String name, byte[] picture);
  void setLocation(User user, double lat, double lon);
  void update(User user);
}
