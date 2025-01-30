package com.mkkabi.dev.repository;

import com.mkkabi.dev.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

@Query(value = "select id, day_of_week, duration_minutes, end_time, start_time, title, week_number, owner_id " +
        "from events where owner_id = ?1 union " +
        "select id, day_of_week, duration_minutes, end_time, start_time, title, week_number, owner_id from events inner join event_attendees on id = event_id and " +
        "attendee_id = ?1", nativeQuery = true)
    List<Event> getAllByUserId(long userId);

    @Query(value = "select id, title, start_time, end_time, day_of_week, week_number, owner_id from events where owner_id = ?1", nativeQuery = true)
    List<Event> getOwnerevents(long userId);

    @Query(value = "select id, day_of_week, duration_minutes, end_time, start_time, title, week_number, owner_id " +
            "from events where owner_id = ?1 and week_number = ?2 union " +
            "select id, day_of_week, duration_minutes, end_time, start_time, title, week_number, owner_id from events inner join event_attendees on id = event_id and " +
            "attendee_id = ?1 where week_number = ?2", nativeQuery = true)
    List<Event> getByUserAndWeek(Long userId, int weekNumber);
}
