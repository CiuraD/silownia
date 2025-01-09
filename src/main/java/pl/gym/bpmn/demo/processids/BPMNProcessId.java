package pl.gym.bpmn.demo.processids;

public enum BPMNProcessId {
  ASSIGN_TRAINING("assign-training");

  private final String processId;

  BPMNProcessId(String processId) {
    this.processId = processId;
  }

  public String getProcessId() {
    return processId;
  }
}
