package msa.schedule_service.domain.schedule;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.schedule_service.domain.schedule.dto.ScheduleCreateRequestDto;
import msa.schedule_service.domain.schedule.dto.ScheduleResponseDto;
import msa.schedule_service.domain.schedule.entity.Schedule;
import msa.schedule_service.domain.schedule.entity.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduledTaskManager scheduledTaskManager;
    private final ScheduleRepository scheduleRepository;

    // 스프링 시작 시 DB에 있는 활성화된 모든 스케줄을 등록
    @PostConstruct
    public void initializeTasks() {
        List<Schedule> activeSchedules = scheduleRepository.findAllByIsActiveTrue();
        for (Schedule schedule : activeSchedules) {
            scheduledTaskManager.addTask(schedule);
        }

        log.info("Initialized {} active tasks", activeSchedules.size());
    }

    // 새로운 스케줄을 등록
    public ScheduleResponseDto createSchedule(ScheduleCreateRequestDto scheduleDto) {
        if (scheduleRepository.existsByName(scheduleDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 스케줄 이름입니다.");
        }

        Schedule schedule = Schedule.builder()
                .cronExpression(scheduleDto.getCronExpression())
                .taskType(scheduleDto.getTaskType())
                .name(scheduleDto.getName())
                .description(scheduleDto.getDescription())
                .isActive(scheduleDto.getIsActive())
                .createdBy(scheduleDto.getCreatedBy())
                .build();

        Schedule savedSchedule = scheduleRepository.save(schedule);
        if (savedSchedule.getIsActive()) {
            scheduledTaskManager.addTask(savedSchedule);
        }

        log.info("Created new schedule: {}", savedSchedule.getName());
        return new ScheduleResponseDto(savedSchedule);
    }

    // 스케줄 업데이트 (cron 표현식 변경)
    public ScheduleResponseDto updateSchedule(Long scheduleId, String newCronExpression, Long updatedBy) {
        Schedule schedule = getScheduleById(scheduleId);
        schedule.updateCronExpression(newCronExpression, updatedBy);

        // 스케줄이 활성화되어 있으면 기존 작업 중단 후 새로운 작업 등록
        if (schedule.getIsActive()) {
            scheduledTaskManager.stopTask(scheduleId);
            scheduledTaskManager.addTask(schedule);
        }

        log.info("Updated schedule: {}", schedule.getName());
        return new ScheduleResponseDto(schedule);
    }

    // 스케줄 활성화/비활성화 업데이트
    public ScheduleResponseDto updateScheduleStatus(Long scheduleId, Boolean isActive, Long updatedBy) {
        Schedule schedule = getScheduleById(scheduleId);

        schedule.updateIsActive(isActive, updatedBy);

        // 활성화/비활성화에 따른 스케줄 작업 추가/중단
        if (isActive) {
            scheduledTaskManager.addTask(schedule);
        } else {
            scheduledTaskManager.stopTask(scheduleId);
        }

        log.info("Updated schedule status for: {}", schedule.getName());
        return new ScheduleResponseDto(schedule);
    }

    // 스케줄 삭제
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = getScheduleById(scheduleId);

        // 스케줄이 활성화 상태면 작업 중단
        if (schedule.getIsActive()) {
            scheduledTaskManager.stopTask(scheduleId);
        }

        scheduleRepository.delete(schedule);
        log.info("Deleted schedule: {}", schedule.getName());
    }

    // 스케줄 단건 조회
    public ScheduleResponseDto getScheduleDtoById(Long scheduleId) {
        Schedule schedule = getScheduleById(scheduleId);
        return new ScheduleResponseDto(schedule);
    }

    // 모든 스케줄 조회
    public List<ScheduleResponseDto> getAllScheduleDtos() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(ScheduleResponseDto::new)
                .toList();
    }

    public Schedule getScheduleById(Long scheduleId) {

        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID"));
    }
}
