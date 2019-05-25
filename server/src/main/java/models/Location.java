package models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

@Entity
@Table(name = "geolocation")
public class Location {
  @Id
  @GeneratedValue
  private int id;
  private double lat;
  private double lon;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="userId")
  private User user;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "createdDate")
  private java.util.Calendar createdDate;

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLon() {
    return lon;
  }

  public void setLon(double lon) {
    this.lon = lon;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
