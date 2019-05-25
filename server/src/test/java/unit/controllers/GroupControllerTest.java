package unit.controllers;

import static org.junit.jupiter.api.Assertions.*;

import models.User;
import org.junit.jupiter.api.*;

import models.Group;
import controllers.GroupController;

import org.eproject.protocol.exceptions.InvalidData;
import org.eproject.protocol.exceptions.GroupNotFound;
import repositories.GroupRepository;
import repositories.GroupRepositoryFactory;

class GroupControllerTest {
  private GroupController controller;

  @BeforeAll
  static void beforeAll() {
    // Mock the group repository
    GroupRepositoryFactory.setInstance(new MockedGroupRepository());
  }

  @AfterAll
  static void afterAll() {
    GroupRepositoryFactory.setInstance(null);
  }

  @BeforeEach
  void beforeEach() {
    controller = new GroupController();
  }

  @Test
  void shouldCreateGroupAndReturnInvitationKey() {
    String key = controller.createGroup("group");

    assertEquals("group:secret", key);
  }


  @Test
  void shouldThrowError_whenInvitationKetIsInvalid() {
    controller.createGroup("group");

    assertThrows(InvalidData.class, () -> {
      controller.findByInvitationKey("secret");
    });
  }

  @Test
  void shouldThrowError_whenInvitationKeyHasCorrectGroupNameAndInvalidSecret() {
    controller.createGroup("group");

    assertThrows(GroupNotFound.class, () -> {
      controller.findByInvitationKey("group:abc123");
    });
  }

  @Test
  void shouldThrowError_whenInvitationKeyHasNonExistingGroupNameAndValidSecret() {
    controller.createGroup("group");

    assertThrows(GroupNotFound.class, () -> {
      controller.findByInvitationKey("abc:secret");
    });
  }

  @Test
  void shouldReturnFoundGroup() {
    controller.createGroup("group");

    Group group = controller.findByInvitationKey("group:secret");

    assertEquals("group", group.getName());
  }
}

class MockedGroupRepository implements GroupRepository {
  private Group group = new Group();

  public Group create(String name) {
    group.setName(name);
    group.setSecret("secret");

    return group;
  }

  public Group findByNameAndSecret(String name, String secret) {
    if (group.getName().equals(name) && group.getSecret().equals(secret)) {
      return group;
    }

    throw new GroupNotFound();
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

  }
}
