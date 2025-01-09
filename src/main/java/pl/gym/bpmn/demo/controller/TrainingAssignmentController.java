package pl.gym.bpmn.demo.controller;

import io.camunda.zeebe.client.ZeebeClient;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gym.bpmn.demo.model.GymUser;
import pl.gym.bpmn.demo.processids.BPMNProcessId;
import pl.gym.bpmn.demo.model.Training;
import pl.gym.bpmn.demo.model.TrainingAssignment;
import pl.gym.bpmn.demo.repository.GymUserRepository;
import pl.gym.bpmn.demo.repository.TrainingRepository;
import pl.gym.bpmn.demo.repository.TrainingUserRepository;

@RestController
@RequestMapping("/api/process")
public class TrainingAssignmentController {
  private static final Logger LOG = LoggerFactory.getLogger(TrainingAssignmentController.class);

  @Qualifier("zeebeClientLifecycle")
  private final ZeebeClient client;

  private final TrainingUserRepository trainingUserRepository;
  private final TrainingRepository trainingRepository;
  private final GymUserRepository gymUserRepository;

  public TrainingAssignmentController(
      ZeebeClient client,
      TrainingUserRepository trainingUserRepository,
      TrainingRepository trainingRepository,
      GymUserRepository gymUserRepository) {
    this.client = client;
    this.trainingUserRepository = trainingUserRepository;
    this.trainingRepository = trainingRepository;
    this.gymUserRepository = gymUserRepository;
  }

  @PostMapping("/assign-training")
  public ResponseEntity<String> startProcess(@RequestBody Map<String, Object> variables) {
    List<Training> trainings = trainingRepository.findAll();
    List<String> trainingList = trainings.stream().map(Training::getName).toList();

    GymUser gymUser =
        gymUserRepository
            .findById(Long.parseLong(variables.get("gymUserId").toString()))
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    variables.put("nameField", gymUser.getName());
    variables.put("surnameField", gymUser.getSurname());
    variables.put("ageField", gymUser.getAge());
    variables.put("trainingList", trainingList);

    LOG.info("Variables {}", variables);

    client
        .newCreateInstanceCommand()
        .bpmnProcessId(BPMNProcessId.ASSIGN_TRAINING.getProcessId())
        .latestVersion()
        .variables(variables)
        .send();

    return ResponseEntity.ok(variables.toString());
  }

  @GetMapping("/assignments")
  public ResponseEntity<List<TrainingAssignment>> getAllAssignments() {
    List<TrainingAssignment> assignments = trainingUserRepository.findAll();
    return ResponseEntity.ok(assignments);
  }
}
