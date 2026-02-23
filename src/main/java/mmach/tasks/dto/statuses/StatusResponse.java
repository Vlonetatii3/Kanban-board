package mmach.tasks.dto.statuses;

import java.time.LocalDateTime;

public class StatusResponse {

    private Integer statusId;

    private String name;

    private LocalDateTime createdAt;

    public StatusResponse(){}

    public StatusResponse(Integer statusId, String name, LocalDateTime createdAt) {
        this.statusId = statusId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getName() {
        return name;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
