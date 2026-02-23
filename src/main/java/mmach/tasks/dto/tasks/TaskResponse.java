package mmach.tasks.dto.tasks;



import java.time.LocalDateTime;

public class TaskResponse {

    private Integer taskId;

    private String name;

    private Integer statusId;

    private LocalDateTime createdAt;

    public TaskResponse(){}

    public TaskResponse(Integer taskId, String name, Integer statusId, LocalDateTime createdAt) {
        this.taskId = taskId;
        this.name = name;
        this.statusId = statusId;
        this.createdAt = createdAt;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
