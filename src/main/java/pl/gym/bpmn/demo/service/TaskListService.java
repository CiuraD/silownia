package pl.gym.bpmn.demo.service;

import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SaasAuthentication;
import io.camunda.tasklist.dto.TaskList;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.dto.Variable;
import io.camunda.tasklist.exception.TaskListException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.gym.bpmn.demo.model.Task;

@Service
public class TaskListService {
  private static final Logger LOG = LoggerFactory.getLogger(TaskListService.class);

  @Value("${zeebe.client.cloud.clientId:N/A}")
  private String clientId;

  @Value("${zeebe.client.cloud.clientSecret:N/A}")
  private String clientSecret;

  @Value("${zeebe.client.cloud.clusterId:N/A}")
  private String clusterId;

  @Value("${zeebe.client.cloud.region:N/A}")
  private String region;

  @Value("${tasklistUrl:N/A}")
  private String tasklistUrl;

  private CamundaTaskListClient client;

  public TaskList getTaskList(TaskState state, Integer pageSize) throws TaskListException {
    return getCamundaTaskListClient().getTasks(null, state, pageSize);
  }

  private CamundaTaskListClient getCamundaTaskListClient() throws TaskListException {
    LOG.info("Creating Camunda Client");

    if (client == null) {
      if (!"N/A".equals(clientId)) {
        SaasAuthentication sa = new SaasAuthentication(clientId, clientSecret);
        client =
            new CamundaTaskListClient.Builder()
                .shouldReturnVariables()
                .taskListUrl("https://" + region + ".tasklist.camunda.io/" + clusterId)
                .authentication(sa)
                .build();
      }
    }
    return client;
  }

  private Task convert(io.camunda.tasklist.dto.Task task) {
    Task result = new Task();
    BeanUtils.copyProperties(task, result);
    if (task.getVariables() != null) {
      result.setVariables(new HashMap<>());
      for (Variable var : task.getVariables()) {
        result.getVariables().put(var.getName(), var.getValue());
      }
    }
    return result;
  }

  private List<Task> convertTasks(io.camunda.tasklist.dto.TaskList tasks) {
    List<Task> result = new ArrayList<>();
    for (io.camunda.tasklist.dto.Task task : tasks) {
      result.add(convert(task));
    }
    return result;
  }
}
