package msa.schedule_service.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import msa.schedule_service.domain.schedule.ScheduleService;
import msa.schedule_service.domain.schedule.dto.ScheduleCreateRequestDto;
import msa.schedule_service.domain.schedule.dto.ScheduleResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 새로운 스케줄 등록
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleCreateRequestDto requestDto) {
        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 스케줄 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long id) {
        ScheduleResponseDto responseDto = scheduleService.getScheduleDtoById(id);
        return ResponseEntity.ok(responseDto);
    }

    // 모든 스케줄 조회
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getAllSchedules() {
        List<ScheduleResponseDto> responseDtos = scheduleService.getAllScheduleDtos();
        return ResponseEntity.ok(responseDtos);
    }

    // 스케줄 업데이트 (cron 표현식 변경)
    @PutMapping("/{id}/cron")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id,
                                                              @RequestBody String newCronExpression,
                                                              @RequestParam Long updatedBy) {
        ScheduleResponseDto responseDto = scheduleService.updateSchedule(id, newCronExpression, updatedBy);
        return ResponseEntity.ok(responseDto);
    }

    // 스케줄 활성화/비활성화 업데이트
    @PutMapping("/{id}/status")
    public ResponseEntity<ScheduleResponseDto> updateScheduleStatus(@PathVariable Long id,
                                                                    @RequestBody Boolean isActive,
                                                                    @RequestParam Long updatedBy) {
        ScheduleResponseDto responseDto = scheduleService.updateScheduleStatus(id, isActive, updatedBy);
        return ResponseEntity.ok(responseDto);
    }

    // 스케줄 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
