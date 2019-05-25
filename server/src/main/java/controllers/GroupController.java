package controllers;

import models.User;
import models.Group;
import repositories.GroupRepository;
import repositories.GroupRepositoryFactory;

import org.eproject.protocol.exceptions.InvalidData;

import java.util.List;
import java.util.stream.Collectors;

final public class GroupController {
  private GroupRepository groupRepository = GroupRepositoryFactory.getInstance();

  private String formatInvitationKey(Group group) {
    return String.format("%s:%s", group.getName(), group.getSecret());
  }

  /**
   * Attempts to create a group with the given name
   * 
   * @param name Group name
   * @return Group invitation key
   */
  public String createGroup(String name) {
    if (name.length() == 0 || name.length() > 32) {
      throw new InvalidData();
    }

    Group group = groupRepository.create(name);

    return formatInvitationKey(group);
  }

  public Group findByInvitationKey(String invitationKey) {
    if (invitationKey.length() == 0) {
      throw new InvalidData();
    }

    String[] parts = invitationKey.split(":", 2);
    if (parts.length != 2) {
      throw new InvalidData();
    }

    return groupRepository.findByNameAndSecret(parts[0], parts[1]);
  }

  public void addMember(Group group, User user) {
    user.setGroupId(group.getId());
    user.setAddedAt();
    group.addUser(user);

    groupRepository.update(group);
  }

  public void exitGroup(Group group, User user) {
    groupRepository.removeUser(group, user);
  }

  public List<User> getNewMembers(Group group, User callee, long timestamp) {
    return group.getUsers().stream()
            .filter((user) -> user.getAddedAt() > timestamp && user.getId() != callee.getId())
            .collect(Collectors.toList());
  }

  public List<Integer> getMemberIDs(Group group) {
    return group.getUsers().stream()
            .map(User::getId)
            .collect(Collectors.toList());
  }

  public List<User> getMembers(Group group) {
    return group.getUsers();
  }

  public Group getGroup(User user) {
    if (user != null) {
      return groupRepository.getById(user.getGroupId());
    }
    return null;
  }
}
