package mmach.tasks.dto.tasks;

import jakarta.validation.constraints.Size;

public class TaskUpdateRequest {

    @Size(max = 80)
    private String name;

    private Integer statusId;

    public TaskUpdateRequest(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
}