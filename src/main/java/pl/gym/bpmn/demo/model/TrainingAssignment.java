package pl.gym.bpmn.demo.model;

import javax.persistence.*;

@Entity
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

  public TrainingAssignment() {}

  public TrainingAssignment(GymUser gymUser, Training training) {
    this.gymUser = gymUser;
    this.training = training;
  }

  public Long getId() {
    return id;
  }

  public GymUser getGymUser() {
    return gymUser;
  }

  public Training getTraining() {
    return training;
  }
}
