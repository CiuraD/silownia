package pl.gym.bpmn.demo.model;

import io.camunda.tasklist.dto.TaskState;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
  private String id;

  private String name;

  private String processName;

  private String assignee;

  private String creationTime;

  private TaskState taskState;

  private List<String> candidateGroups;

  private List<String> sortValues;

  private Boolean isFirst;

  private Map<String, Object> variables;

  private String formKey;

  private String processDefinitionId;
}
