package pl.gym.bpmn.demo;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Worker {
  private final Logger logger = LoggerFactory.getLogger(Worker.class);

  @Qualifier("zeebeClientLifecycle")
  private final ZeebeClient zeebeClient;

  public Worker(ZeebeClient zeebeClient) {
    this.zeebeClient = zeebeClient;
  }

  @JobWorker(type = "worker")
  public void handleWorker(JobClient client, ActivatedJob job) {
    logger.info("Task type: {}", job.getType());

    Map<String, Object> jobVariables = job.getVariablesAsMap();

    for(Map.Entry<String, Object> entry : jobVariables.entrySet()) {
      logger.info("Process variables {}, {}", entry.getKey(), entry.getValue());
    }

    client.newCompleteCommand(job).variables(jobVariables).send().join();
  }
}
