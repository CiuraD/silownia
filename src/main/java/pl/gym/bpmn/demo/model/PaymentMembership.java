package pl.gym.bpmn.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMembership {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "amount")
  private Double amount;

  @OneToOne
  @JoinColumn(name = "gym_user_id")
  private GymUser gymUser;

  public PaymentMembership(Double amount, GymUser gymUser) {
    this.amount = amount;
    this.gymUser = gymUser;
  }
}
