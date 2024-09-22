package msa.schedule_service.domain.schedule.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import msa.schedule_service.domain.schedule.entity.enums.TaskType;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cronExpression;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Long createdBy;

    @Column(nullable = true)
    private Instant updatedAt;

    @Column(nullable = true)
    private Long updatedBy;

    @Builder
    public Schedule(String cronExpression, TaskType taskType, String name, String description, Boolean isActive, Long createdBy) {
        this.cronExpression = cronExpression;
        this.taskType = taskType;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = Instant.now();
        this.createdBy = createdBy;
        this.updatedAt = null;
        this.updatedBy = null;
    }

    public void updateCronExpression(String cronExpression, Long updatedBy) {
        this.cronExpression = cronExpression;
        this.updatedAt = Instant.now();
        this.updatedBy = updatedBy;
    }

    public void updateIsActive(Boolean isActive, Long updatedBy) {
        this.isActive = isActive;
        this.updatedAt = Instant.now();
        this.updatedBy = updatedBy;
    }

}
