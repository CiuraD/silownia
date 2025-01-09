package pl.gym.bpmn.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.gym.bpmn.demo.model.Training;

import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    Optional<Training> findByName(String name);
}
