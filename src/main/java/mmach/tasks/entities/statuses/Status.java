package mmach.tasks.entities.statuses;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "statuses")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusid")
    private Integer statusId;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @Column(name = "createdat")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "lastmod")
    @UpdateTimestamp
    private LocalDateTime lastMod;

    public Status(){}

    public Status(Integer statusId, String name, LocalDateTime createdAt, LocalDateTime lastMod) {
        this.statusId = statusId;
        this.name = name;
        this.createdAt = createdAt;
        this.lastMod = lastMod;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastMod() {
        return lastMod;
    }

    public void setLastMod(LocalDateTime lastMod) {
        this.lastMod = lastMod;
    }

}
