package marius.ma.jenkinstodoapp.repository;

import marius.ma.jenkinstodoapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
