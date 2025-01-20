package pl.gym.bpmn.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import java.util.*;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import pl.gym.bpmn.demo.controller.TrainingAssignmentController;
import pl.gym.bpmn.demo.model.GymUser;
import pl.gym.bpmn.demo.model.Training;
import pl.gym.bpmn.demo.model.TrainingAssignment;
import pl.gym.bpmn.demo.repository.GymUserRepository;
import pl.gym.bpmn.demo.repository.TrainingRepository;
import pl.gym.bpmn.demo.repository.TrainingUserRepository;
import pl.gym.bpmn.demo.service.TrainingAssignmentService;

@SpringBootTest
class TrainingAssignmentTest {

	@Mock
	private ZeebeClient zeebeClient;

	@Mock
	private TrainingUserRepository trainingUserRepository;

	@Mock
	private TrainingRepository trainingRepository;

	@Mock
	private GymUserRepository gymUserRepository;

	private TrainingAssignmentController controller;
	private TrainingAssignmentService service;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		controller = new TrainingAssignmentController(zeebeClient, trainingUserRepository, trainingRepository, gymUserRepository);
		service = new TrainingAssignmentService(trainingUserRepository, trainingRepository, gymUserRepository);
	}

	@Test
	void testStartProcess_UserNotFound() {
		Map<String, Object> variables = new HashMap<>();
		variables.put("email", "nonexistent@example.com");

		when(gymUserRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.startProcess(variables));
		assertEquals("User not found", exception.getMessage());
	}

	@Test
	void testAssignTrainingJobWorker() {
		JobClient mockJobClient = mock(JobClient.class);
		ActivatedJob mockJob = mock(ActivatedJob.class);

		Map<String, Object> jobVariables = new HashMap<>();
		jobVariables.put("email", "test@example.com");
		jobVariables.put("trainingsList", "Yoga");

		when(mockJob.getVariablesAsMap()).thenReturn(jobVariables);

		GymUser mockUser = new GymUser("John", "Doe", "test@example.com", 30);
		Training mockTraining = new Training(1L, "Cardio", 60, "Medium intense workout");

		when(gymUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
		when(trainingRepository.findByName("Yoga")).thenReturn(Optional.of(mockTraining));

		Map<String, Object> result = service.assignTraining(mockJobClient, mockJob);

		verify(trainingUserRepository, times(1)).save(any(TrainingAssignment.class));
		assertNotNull(result.get("assignment"));
	}

	@Test
	void testAssignTrainingJobWorker_TrainingNotFound() {
		JobClient mockJobClient = mock(JobClient.class);
		ActivatedJob mockJob = mock(ActivatedJob.class);

		Map<String, Object> jobVariables = new HashMap<>();
		jobVariables.put("email", "test@example.com");
		jobVariables.put("trainingsList", "Nonexistent");

		when(mockJob.getVariablesAsMap()).thenReturn(jobVariables);

		GymUser mockUser = new GymUser("John", "Doe", "test@example.com", 30);
		when(gymUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
		when(trainingRepository.findByName("Nonexistent")).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> service.assignTraining(mockJobClient, mockJob));
		assertEquals("No training found", exception.getMessage());
	}
}