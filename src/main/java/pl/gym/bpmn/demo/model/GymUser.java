package pl.gym.bpmn.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GymUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "surname")
  private String surname;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "age")
  private Integer age;

  public GymUser(String name, String surname, String email, Integer age) {
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.age = age;
  }
}
