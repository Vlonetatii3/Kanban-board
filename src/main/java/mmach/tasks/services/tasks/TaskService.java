package mmach.tasks.services.tasks;

import jakarta.transaction.Transactional;
import mmach.tasks.dto.tasks.TaskCreateRequest;
import mmach.tasks.dto.tasks.TaskResponse;
import mmach.tasks.dto.tasks.TaskUpdateRequest;
import mmach.tasks.entities.tasks.Task;
import mmach.tasks.exceptions.NoDataFound;
import mmach.tasks.repository.tasks.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<TaskResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(task -> new TaskResponse(
                        task.getTaskId(),
                        task.getName(),
                        task.getStatusId(),
                        task.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public TaskResponse findById(Integer id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new NoDataFound("No task found for id: " + id));

        return new TaskResponse(
                task.getTaskId(),
                task.getName(),
                task.getStatusId(),
                task.getCreatedAt()
        );
    }


    public TaskResponse create(TaskCreateRequest request) {
        Task task = new Task();

        task.setName(request.getName());
        task.setStatusId(request.getStatusId());

        Task saved = repository.save(task);

        return new TaskResponse(
                saved.getTaskId(),
                saved.getName(),
                saved.getStatusId(),
                saved.getCreatedAt());


    }

    @Transactional
    public void delete(Integer id){
        Task task = repository.findById(id)
                .orElseThrow(() -> new NoDataFound("No task found for id: " + id));

        repository.delete(task);
    }

    @Transactional
    public TaskResponse patch(Integer id, TaskUpdateRequest request){
        Task task = repository.findById(id)
                .orElseThrow(() -> new NoDataFound("No task found for id: " + id));

        if (request.getName() != null){
            task.setName(request.getName());
        }

        if(request.getStatusId() != null){
            task.setStatusId(request.getStatusId());
        }

        Task saved = repository.save(task);
        return new TaskResponse(
                saved.getTaskId(),
                saved.getName(),
                saved.getStatusId(),
                saved.getCreatedAt());
    }
}
