package pl.gym.bpmn.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TrainingAssignment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "gym_user_id")
  private GymUser gymUser;

  @ManyToOne
  @JoinColumn(name = "training_id")
  private Training training;

  public TrainingAssignment(GymUser gymUser, Training training) {
    this.gymUser = gymUser;
    this.training = training;
  }
}
