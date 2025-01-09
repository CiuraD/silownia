package pl.gym.bpmn.demo.service;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.gym.bpmn.demo.model.GymUser;
import pl.gym.bpmn.demo.model.Training;
import pl.gym.bpmn.demo.model.TrainingAssignment;
import pl.gym.bpmn.demo.repository.GymUserRepository;
import pl.gym.bpmn.demo.repository.TrainingRepository;
import pl.gym.bpmn.demo.repository.TrainingUserRepository;

@Component
public class TrainingAssignmentService {
  private final Logger LOGGER = LoggerFactory.getLogger(TrainingAssignmentService.class);
  private final TrainingUserRepository trainingUserRepository;
  private final TrainingRepository trainingRepository;
  private final GymUserRepository gymUserRepository;

  public TrainingAssignmentService(
      TrainingUserRepository trainingUserRepository,
      TrainingRepository trainingRepository,
      GymUserRepository gymUserRepository) {
    this.trainingUserRepository = trainingUserRepository;
    this.trainingRepository = trainingRepository;
    this.gymUserRepository = gymUserRepository;
  }

  @JobWorker(type = "assignTraining")
  public Map<String, Object> assignTraining(final JobClient client, final ActivatedJob job) {
    HashMap<String, Object> jobResultVariables = new HashMap<>();
    final Map<String, Object> jobVariables = job.getVariablesAsMap();

    LOGGER.info("Started process 'assignTraining' {}", job);

    Long id = Long.parseLong(jobVariables.get("gymUserId").toString());

    GymUser gymUser =
        gymUserRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No user found"));

    if (jobVariables.get("trainingsList") != null) {
      String trainingName = jobVariables.get("trainingsList").toString();
      Training training =
          trainingRepository
              .findByName(trainingName)
              .orElseThrow(() -> new IllegalArgumentException("No training found"));
      TrainingAssignment assignment = new TrainingAssignment(gymUser, training);
      trainingUserRepository.save(assignment);
      LOGGER.info("Saved training for user to database");
      jobResultVariables.put("assignment", assignment);
      LOGGER.info("Finished assinging training");
    }

    return jobResultVariables;
  }
}
