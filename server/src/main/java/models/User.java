package models;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "GeoUser")
@Table(name = "geouser")
public class User {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private int userId;

  private String name;

  private byte[] picture;

  private int groupId;

  private long addedAt; // Timestamp shows when user has been added to the group

  private double lat;

  private double lon;

  private boolean deleted = false;

  /**
   * Sets addedAt to the current time
   */
  public void setAddedAt() {
    addedAt = Instant.now().toEpochMilli();
  }

  public long getAddedAt() {
    return addedAt;
  }

  public int getId() {
    return userId;
  }

  public void setId(int userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public byte[] getPicture() {
    return picture;
  }

  public void setPicture(byte[] picture) {
    this.picture = picture;
  }

  public int getGroupId() {
    return groupId;
  }

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }

  public void setLocation(double lat, double lon) {
    this.lat = lat;
    this.lon = lon;
  }

  public double getLat() {
    return lat;
  }

  public double getLon() {
    return lon;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
}
