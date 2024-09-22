package msa.schedule_service.domain.schedule.task;

import lombok.extern.slf4j.Slf4j;
import msa.schedule_service.domain.schedule.entity.enums.TaskType;

import java.util.Map;

@Slf4j
public class BatchScheduledTask implements ScheduledTask {
    @Override
    public void execute(Map<String, Object> data) {
        String jobName = (String) data.get("jobName");
        log.info("BatchScheduledTask.execute() jobName: {}", jobName);
        log.info("API CALL TO TRIGGER BATCH JOB: {}", jobName);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.BATCH_JOB_TRIGGER;
    }
}
