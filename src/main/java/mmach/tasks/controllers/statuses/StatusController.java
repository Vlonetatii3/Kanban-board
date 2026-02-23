package mmach.tasks.controllers.statuses;


import jakarta.validation.Valid;
import mmach.tasks.dto.statuses.StatusCreateRequest;
import mmach.tasks.dto.statuses.StatusResponse;
import mmach.tasks.services.statuses.StatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statuses")
public class StatusController {

    private final StatusService service;

    public StatusController(StatusService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<StatusResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusResponse> findById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<StatusResponse> create(@Valid @RequestBody StatusCreateRequest request){
        return ResponseEntity.status(201).body(service.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StatusResponse> patch(@PathVariable("id") Integer id,
                                                @Valid @RequestBody StatusCreateRequest request){
        return ResponseEntity.ok(service.patch(id, request));
    }
}
