package repositories;

import repositories.memory.MemoryGroupRepository;
import repositories.persistent.PersistentGroupRepository;

/**
 * Factory class that decouples the client and Group Repository implementation.
 * Makes it possible to switch the implementation at the runtime.
 */
public final class GroupRepositoryFactory {
  private static GroupRepository instance;

  public static GroupRepository getInstance() {
    if (instance == null) {
//      instance = new MemoryGroupRepository();
      instance = new PersistentGroupRepository();
    }

    return instance;
  }

  public static void setInstance(GroupRepository instance) {
    GroupRepositoryFactory.instance = instance;
  }
}
