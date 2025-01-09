package pl.gym.bpmn.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.gym.bpmn.demo.model.TrainingAssignment;

@Repository
public interface TrainingUserRepository extends JpaRepository<TrainingAssignment, Long> {}
