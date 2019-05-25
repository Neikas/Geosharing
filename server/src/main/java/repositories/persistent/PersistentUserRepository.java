package repositories.persistent;

import models.Location;
import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repositories.UserRepository;
import utils.HibernateConnector;

public class PersistentUserRepository implements UserRepository {
  private HibernateConnector connector = HibernateConnector.getInstance();
  @Override
  public User create(String name, byte[] picture) {
    User user = new User();
    user.setName(name);
    user.setPicture(picture);

    Session session = connector.getSession();
    Transaction transaction = session.beginTransaction();
    session.save(user);
    transaction.commit();

    return user;
  }

  @Override
  public void setLocation(User user, double lat, double lon) {
    Session session = connector.getSession();
    Transaction transaction = session.beginTransaction();
    user.setLocation(lat, lon);
    session.merge(user);
    Location location = new Location();
    location.setLat(lat);
    location.setLon(lon);
    location.setUser(user);
    session.save(location);

    transaction.commit();
  }

  @Override
  public void update(User user) {
    Session session = connector.getSession();
    Transaction transaction = session.beginTransaction();
    session.merge(user);
    transaction.commit();
  }
}
