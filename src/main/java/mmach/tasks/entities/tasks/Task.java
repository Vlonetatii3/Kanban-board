package mmach.tasks.entities.tasks;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskid")
    private Integer taskId;

    @Column(name = "name", length = 80)
    private String name;

    @Column(name = "statusid", nullable = false)
    private Integer statusId;

    @Column(name = "createdat")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "lastmod")
    @UpdateTimestamp
    private LocalDateTime lastmod;

    public Task(){}

    public Task(Integer taskId, String name, Integer statusId, LocalDateTime createdAt, LocalDateTime lastmod) {
        this.taskId = taskId;
        this.name = name;
        this.statusId = statusId;
        this.createdAt = createdAt;
        this.lastmod = lastmod;
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

    public LocalDateTime getLastmod() {
        return lastmod;
    }

    public void setLastmod(LocalDateTime lastmod) {
        this.lastmod = lastmod;
    }
}
