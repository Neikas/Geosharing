package repositories.memory;

import java.util.logging.Logger;
import java.util.List;
import java.util.LinkedList;
import java.util.Base64;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;

import models.Group;
import models.User;
import repositories.GroupRepository;
import org.eproject.protocol.exceptions.GroupNotFound;
import utils.Generators;

public class MemoryGroupRepository implements GroupRepository {
  private static final Logger logger = Logger.getLogger(MemoryGroupRepository.class.getName());
  private List<Group> groups = new LinkedList<>();

  public MemoryGroupRepository() {
  }

  @Override
  public Group create(String name) {
    Group group = new Group();
    group.setName(name);
    group.setSecret(Generators.getInstance().generateSecret());

    groups.add(group);

    return group;
  }

  @Override
  public Group findByNameAndSecret(String name, String secret) {
    List<Group> result = groups.stream().filter(group ->
      group.getName().equals(name) && group.getSecret().equals(secret)
    ).collect(Collectors.toList());

    if (result.size() == 0) {
      throw new GroupNotFound();
    }

    // Whooops, seems like there are multiple groups with the same secret.
    // Unlikely to happen
    if (result.size() > 1) {
      throw new RuntimeException("Multiple groups found");
    }

    return result.get(0);
  }

  @Override
  public void update(Group group) {

  }

  @Override
  public Group getById(int groupId) {
    return null;
  }

  @Override
  public void removeUser(Group group, User user) {
    group.removeUser(user);
  }
}
