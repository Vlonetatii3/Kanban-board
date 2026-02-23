package mmach.tasks.controllers.tasks;

import jakarta.validation.Valid;
import mmach.tasks.dto.tasks.TaskCreateRequest;
import mmach.tasks.dto.tasks.TaskResponse;
import mmach.tasks.dto.tasks.TaskUpdateRequest;
import mmach.tasks.services.tasks.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid  @RequestBody TaskCreateRequest request){
        return ResponseEntity.status(201).body(service.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> patch(@PathVariable("id") Integer id,
                                              @Valid  @RequestBody TaskUpdateRequest request){
        return ResponseEntity.ok(service.patch(id, request));
    }
}
