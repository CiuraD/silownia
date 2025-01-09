package pl.gym.bpmn.demo.model;

import javax.persistence.*;

@Entity
public class Training {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "name")
  private String name;
  @Column(name = "duration")
  private Integer duration;
  @Column(name = "description")
  private String description;

  public Training() {
  }

  public Training(String name, Integer duration, String description) {
    this.name = name;
    this.duration = duration;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getDuration() {
    return duration;
  }

  public String getDescription() {
    return description;
  }
}
