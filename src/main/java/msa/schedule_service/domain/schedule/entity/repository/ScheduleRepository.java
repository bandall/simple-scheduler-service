package msa.schedule_service.domain.schedule.entity.repository;

import msa.schedule_service.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.isActive = true")
    List<Schedule> findAllByIsActiveTrue();

    boolean existsByName(String name);
}
