package repositories.memory;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import models.Group;
import models.User;
import repositories.UserRepository;

public class MemoryUserRepository implements UserRepository {
  private static final Logger logger = Logger.getLogger(MemoryUserRepository.class.getName());
  private Map<Integer, User> users = new HashMap<>();
  private int lastId = 0;

  private int generateUserId() {
    return lastId++;
  }

  public User create(String name, byte[] picture) {
    User user = new User();
    user.setId(generateUserId());
    user.setName(name);
    user.setPicture(picture);

    users.put(user.getId(), user);

    logger.info(String.format("User created. id: %d, name: %s, address: %s",
            user.getId(), user.getName(), user.toString()));

    return user;
  }

  @Override
  public void setLocation(User user, double lat, double lon) {
    user.setLocation(lat, lon);
  }

  public void update(User user) {

  }

  public void removeUser(Group group, User user) {
    group.removeUser(user);
  }
}
