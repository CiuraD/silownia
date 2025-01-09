package pl.gym.bpmn.demo.processids;

import lombok.Getter;

@Getter
public enum BPMNProcessId {
  ASSIGN_TRAINING("assign-training"),
  PAYMENT_MEMBERSHIP("payment-membership");

  private final String processId;

  BPMNProcessId(String processId) {
    this.processId = processId;
  }

}
