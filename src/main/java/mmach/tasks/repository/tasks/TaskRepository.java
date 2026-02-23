package mmach.tasks.repository.tasks;

import mmach.tasks.entities.tasks.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
