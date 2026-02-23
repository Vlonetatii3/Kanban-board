package mmach.tasks.services.statuses;

import jakarta.transaction.Transactional;
import mmach.tasks.entities.statuses.Status;
import mmach.tasks.dto.statuses.StatusCreateRequest;
import mmach.tasks.dto.statuses.StatusResponse;
import mmach.tasks.exceptions.NoDataFound;
import mmach.tasks.repository.statuses.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatusService {

    private final StatusRepository repository;

    public StatusService(StatusRepository repository){
        this.repository = repository;
    }

    public List<StatusResponse> findAll(){
        return repository.findAll()
                .stream()
                .map(
                        status -> new StatusResponse(
                                status.getStatusId(),
                                status.getName(),
                                status.getCreatedAt()
                        )
                )
                .collect(Collectors.toList());
    }

    public StatusResponse findById(Integer id){
        Status status = repository.findById(id)
                .orElseThrow(() -> new NoDataFound("No status with id: " + id));

        return new StatusResponse(
                status.getStatusId(),
                status.getName(),
                status.getCreatedAt()
        );
    }

    public StatusResponse create(StatusCreateRequest request){
        Status status = new Status();

        status.setName(request.getName());

        Status saved = repository.save(status);

        return new StatusResponse(
                saved.getStatusId(),
                saved.getName(),
                saved.getCreatedAt()
        );
    }

    @Transactional
    public void delete(Integer id){
        Status status = repository.findById(id)
                .orElseThrow(() -> new NoDataFound("No status with id: " + id));

        repository.delete(status);
    }

    @Transactional
    public StatusResponse patch(Integer id, StatusCreateRequest request){
        Status status = repository.findById(id)
                .orElseThrow(() -> new NoDataFound("No status with id: " + id));

        if (request.getName() != null) {
            status.setName(request.getName());
        }

        Status saved = repository.save(status);
        return new StatusResponse(
                saved.getStatusId(),
                saved.getName(),
                saved.getCreatedAt()
        );
    }
}
