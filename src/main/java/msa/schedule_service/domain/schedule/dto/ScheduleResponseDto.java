package msa.schedule_service.domain.schedule.dto;

import lombok.Getter;
import msa.schedule_service.domain.schedule.entity.Schedule;
import msa.schedule_service.domain.schedule.entity.enums.TaskType;

import java.time.Instant;

@Getter
public class ScheduleResponseDto {
    private final Long id;
    private final String cronExpression;
    private final TaskType taskType;
    private final String name;
    private final String description;
    private final Boolean isActive;
    private final Instant createdAt;
    private final Long createdBy;
    private final Instant updatedAt;
    private final Long updatedBy;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.cronExpression = schedule.getCronExpression();
        this.taskType = schedule.getTaskType();
        this.name = schedule.getName();
        this.description = schedule.getDescription();
        this.isActive = schedule.getIsActive();
        this.createdAt = schedule.getCreatedAt();
        this.createdBy = schedule.getCreatedBy();
        this.updatedAt = schedule.getUpdatedAt();
        this.updatedBy = schedule.getUpdatedBy();
    }
}
