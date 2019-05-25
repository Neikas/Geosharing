package repositories;

import repositories.memory.MemoryUserRepository;
import repositories.persistent.PersistentUserRepository;

import java.util.logging.Logger;


/**
 * Factory class that decouples the client and User Repository implementation.
 * Makes it possible to switch the implementation at the runtime.
 */
public final class UserRepositoryFactory {
  private static final Logger logger = Logger.getLogger(UserRepositoryFactory.class.getName());

  private static UserRepository instance;

  public static UserRepository getInstance() {
    if (instance == null) {
//      instance = new MemoryUserRepository();
      instance = new PersistentUserRepository();
    }

    return instance;
  }

  public static void setInstance(UserRepository instance) {
    UserRepositoryFactory.instance = instance;
  }
}
