package pl.gym.bpmn.demo.controller;

import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.TaskList;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.exception.TaskListException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gym.bpmn.demo.service.TaskListService;

@RestController
@RequestMapping("/api/task")
public class TaskController {
  private static final Logger LOG = LoggerFactory.getLogger(TaskController.class);
  private final TaskListService taskListService;

  public TaskController(TaskListService taskListService) {
    this.taskListService = taskListService;
  }

  @GetMapping("/tasks-list")
  public ResponseEntity<TaskList> getAllTasksList() {
    TaskList tasks = new TaskList();
    try {

      tasks = taskListService.getTaskList(TaskState.CREATED, null);

      for (Task task : tasks) {
        LOG.info("Task {}", task.getId());
      }
    } catch (TaskListException e) {
      LOG.error("Task exception", e);
    }
    return new ResponseEntity<>(tasks, HttpStatus.OK);
  }
}
