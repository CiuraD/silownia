package pl.gym.bpmn.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gym.bpmn.demo.model.Training;
import pl.gym.bpmn.demo.repository.TrainingRepository;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TrainingController {
  private final TrainingRepository trainingRepository;

  public TrainingController(TrainingRepository trainingRepository) {
    this.trainingRepository = trainingRepository;
  }

  @GetMapping("/trainings")
  public List<Training> getTrainings() {
    return trainingRepository.findAll();
  }
}
