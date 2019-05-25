package models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "GeoGroup")
@Table(name = "geogroup")
public class Group {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private int id;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<User> users = new LinkedList<>();

  private String name;
  private String secret;

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public void addUser(User user) {
    users.add(user);
  }

  public void removeUser(User user) {
    users.remove(user);
  }

  public List<User> getUsers() {
    return users.stream().filter((u) -> !u.isDeleted()).collect(Collectors.toList());
  }
}
