package unit.repositories.memory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import models.Group;
import repositories.GroupRepository;
import repositories.memory.MemoryGroupRepository;
import org.eproject.protocol.exceptions.GroupNotFound;

import org.junit.jupiter.api.BeforeEach;

class MemoryGroupRepositoryTest {
  private GroupRepository repository;

  @BeforeEach
  void beforeEach() {
    repository = new MemoryGroupRepository();
  }

  @Test
  void shouldCreateGrupAndSetName() {
    Group group = repository.create("new_group");

    assertEquals("new_group", group.getName());
  }

  @Test
  void shouldCreateMultipleGroupsWithTheSameName() {
    Group group1 = repository.create("group");
    Group group2 = repository.create("group");

    assertNotNull(group1);
    assertNotNull(group2);
  }

  @Test
  void shouldCreateRepositoriesWithUniqueSecrets() {
    Group group1 = repository.create("group");
    Group group2 = repository.create("group");

    assertNotEquals(group1.getSecret(), group2.getSecret());
  }

  @Test
  void shouldFindExistingGroupByNameAndSecret() {
    Group group = repository.create("group");

    Group found = repository.findByNameAndSecret(group.getName(), group.getSecret());

    assertEquals(group, found);
  }

  @Test
  void shouldFindGroupWithSpecifiedSecret_WhenMultipleGroupsWithSameNameExists() {
    Group group1 = repository.create("group");
    Group group2 = repository.create("group");

    Group found = repository.findByNameAndSecret(group2.getName(), group2.getSecret());

    assertEquals(group2, found);
  }

  @Test
  void shouldThrowException_WhenGroupWithTheSameNameButWithDifferentSecretExists() {
    Group group = repository.create("group");

    assertThrows(GroupNotFound.class, () -> {
      repository.findByNameAndSecret(group.getName(), "secret");
    });
  }

  @Test
  void shouldThrowException_WhenGroupWithSpeicifiedNameDoesNotExists() {
    Group group = repository.create("group");

    assertThrows(GroupNotFound.class, () -> {
      repository.findByNameAndSecret("name", group.getSecret());
    });
  }
}
