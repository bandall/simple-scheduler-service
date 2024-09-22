package msa.schedule_service.domain.schedule.task;

import msa.schedule_service.domain.schedule.entity.enums.TaskType;

import java.util.Map;

public interface ScheduledTask {
    void execute(Map<String, Object> data);

    TaskType getTaskType();
}
