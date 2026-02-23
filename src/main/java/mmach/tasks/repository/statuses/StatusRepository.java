package mmach.tasks.repository.statuses;

import mmach.tasks.entities.statuses.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
}
