package pl.gym.bpmn.demo;

import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/process")
public class Controller {
  @Qualifier("zeebeClientLifecycle")
  private final ZeebeClient client;


  public Controller(ZeebeClient client) {
    this.client = client;
  }

  @PostMapping("/start")
  public String startProcess(@RequestBody Map<String, Object> variables) {
    client
        .newCreateInstanceCommand()
        .bpmnProcessId("order-process")
        .latestVersion()
        .variables(variables)
        .send()
        .join();

    return "Process started successfully!";
  }
}
