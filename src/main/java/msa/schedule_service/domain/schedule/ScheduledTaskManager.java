package msa.schedule_service.domain.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.schedule_service.domain.schedule.entity.Schedule;
import msa.schedule_service.domain.schedule.entity.enums.TaskType;
import msa.schedule_service.domain.schedule.task.ScheduledTask;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTaskManager {

    private final TaskScheduler taskScheduler;
    private final ApplicationContext applicationContext;

    // 실제로 실행되는 스케줄을 관리하는 맵
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    // 스케줄 작업 추가
    public void addTask(Schedule schedule) {
        // 이미 스케줄 작업이 존재하는지 확인
        if (scheduledTasks.containsKey(schedule.getId())) {
            log.warn("Task already exists for schedule: {}", schedule.getName());
            return;
        }

        Map<String, Object> taskData = new HashMap<>();
        taskData.put("jobName", schedule.getName());
        taskData.put("scheduleId", schedule.getId());
        taskData.put("taskType", schedule.getTaskType().name());

        // 스케줄 작업 추가
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(() -> {
            try {
                ScheduledTask scheduledTaskImpl = getTaskByType(schedule.getTaskType());
                scheduledTaskImpl.execute(taskData);
                log.info("Task executed: {}", schedule.getName());
            } catch (Exception e) {
                log.error("Task execution failed: {}", schedule.getName(), e);
            }
        }, new CronTrigger(schedule.getCronExpression()));

        // 맵에 작업을 저장
        scheduledTasks.put(schedule.getId(), scheduledTask);
    }

    // 스케줄 작업 중단
    public void stopTask(Long scheduleId) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(scheduleId);
        if (scheduledTask == null) {
            log.warn("Task does not exist for schedule: {}", scheduleId);
            return;
        }

        scheduledTask.cancel(false);
        scheduledTasks.remove(scheduleId);
    }

    // 모든 스케줄 작업 중단
    public void stopAllTasks() {
        for (ScheduledFuture<?> scheduledTask : scheduledTasks.values()) {
            scheduledTask.cancel(false);
        }
        scheduledTasks.clear();
    }

    private ScheduledTask getTaskByType(TaskType taskType) {
        return (ScheduledTask) applicationContext.getBean(taskType + "JobTask");
    }
}
