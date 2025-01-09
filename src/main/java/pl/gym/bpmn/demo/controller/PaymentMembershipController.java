package pl.gym.bpmn.demo.controller;

import io.camunda.zeebe.client.ZeebeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.gym.bpmn.demo.processids.BPMNProcessId;
import pl.gym.bpmn.demo.repository.GymUserRepository;

import java.util.Map;

@RestController("/api/process")
public class PaymentMembershipController {
  private final Logger LOGGER = LoggerFactory.getLogger(PaymentMembershipController.class);

  @Qualifier("zeebeClientLifecycle")
  private final ZeebeClient client;

  private final GymUserRepository gymUserRepository;

  public PaymentMembershipController(ZeebeClient client, GymUserRepository gymUserRepository) {
    this.client = client;
    this.gymUserRepository = gymUserRepository;
  }

  @PostMapping("/pay-membership")
  public ResponseEntity<String> startProcess(@RequestBody Map<String, Object> variables) {
    client
        .newCreateInstanceCommand()
        .bpmnProcessId(BPMNProcessId.PAYMENT_MEMBERSHIP.getProcessId())
        .latestVersion()
        .variables(variables)
        .send();

    return ResponseEntity.ok(variables.toString());
  }
}
