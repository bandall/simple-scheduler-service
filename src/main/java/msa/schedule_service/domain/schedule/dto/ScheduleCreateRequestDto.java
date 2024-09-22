package msa.schedule_service.domain.schedule.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import msa.schedule_service.domain.schedule.entity.enums.TaskType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleCreateRequestDto {

    private String cronExpression;
    
    private TaskType taskType;

    private String name;

    private String description;

    private Boolean isActive;

    private Long createdBy;

}
