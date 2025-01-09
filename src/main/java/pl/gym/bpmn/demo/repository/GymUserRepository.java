package pl.gym.bpmn.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.gym.bpmn.demo.model.GymUser;

@Repository
public interface GymUserRepository extends JpaRepository<GymUser, Long> {}
