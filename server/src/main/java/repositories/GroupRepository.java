package repositories;

import models.Group;
import models.User;

public interface GroupRepository {
  Group create(String name);
  Group findByNameAndSecret(String name, String secret);
  void update(Group group);
  Group getById(int groupId);
  void removeUser(Group group, User user);
}
