package repositories.persistent;

import models.Group;
import models.User;
import org.eproject.protocol.exceptions.GroupNotFound;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repositories.GroupRepository;
import utils.Generators;
import utils.HibernateConnector;

import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


public class PersistentGroupRepository implements GroupRepository {
  private HibernateConnector connector = HibernateConnector.getInstance();
  @Override
  public Group create(String name) {
    Group group = new Group();
    group.setName(name);
    group.setSecret(Generators.getInstance().generateSecret());

    Session session = connector.getSession();
    Transaction transaction = session.beginTransaction();
    session.save(group);
    transaction.commit();

    return group;
  }

  @Override
  public Group findByNameAndSecret(String name, String secret) {
    Session session = connector.getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Group> query = builder.createQuery(Group.class);
    Root<Group> groupList = query.from(Group.class);

    query.select(groupList)
            .where(builder.and(
                    builder.equal(groupList.get("name"), name),
                    builder.equal(groupList.get("secret"), secret)
            ));

    try {
      return session.createQuery(query).getSingleResult();
    } catch (NoResultException e) {
      throw new GroupNotFound();
    }
  }

  @Override
  public void update(Group group) {
    Session session = connector.getSession();
    Transaction transaction = session.beginTransaction();
    session.merge(group);
    transaction.commit();
  }

  @Override
  public Group getById(int groupId) {
    Session session = connector.getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Group> query = builder.createQuery(Group.class);
    Root<Group> groupList = query.from(Group.class);

    query.select(groupList)
            .where(builder.equal(groupList.get("id"), groupId));

    try {
      return session.createQuery(query).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Override
  public void removeUser(Group group, User user) {
    Session session = connector.getSession();
    Transaction transaction = session.beginTransaction();
    user.setDeleted(true);
    session.merge(user);
//    session.delete(user);
    transaction.commit();
  }
}
