package unit.repositories.memory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import models.User;
import repositories.UserRepository;
import repositories.memory.MemoryUserRepository;

class MemoryUserRepositoryTest {
  private UserRepository repository;

  @BeforeEach
  void beforeEach() {
    repository = new MemoryUserRepository();
  }

  @Test
  void shouldCreateNewUserAndSetNameAndPicture() {
    byte[] picture = { 0x01, 0x02 };

    User user = repository.create("user", picture);

    assertEquals("user", user.getName());
    assertEquals(picture, user.getPicture());
  }

  @Test
  void shouldCreateUsersWithTheSameName() {
    byte[] picture = { 0x01, 0x02 };

    User user1 = repository.create("user", picture);
    User user2 = repository.create("user", picture);

    assertEquals("user", user1.getName());
    assertEquals("user", user2.getName());
    assertNotEquals(user1.getId(), user2.getId());
  }

  @Test
  void shouldCreateUsersWithDifferentIds() {
    byte[] picture = { 0x01, 0x02 };

    User user1 = repository.create("user", picture);
    User user2 = repository.create("user", picture);

    assertEquals("user", user1.getName());
    assertEquals("user", user2.getName());

    assertNotEquals(user1.getId(), user2.getId());
  }
}
