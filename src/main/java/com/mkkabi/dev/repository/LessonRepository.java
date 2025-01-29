package com.mkkabi.dev.repository;

import com.mkkabi.dev.model.Discipline;
import com.mkkabi.dev.model.Lesson;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query(value = "select * from lessons l left join disciplines d on l.discipline_id = d.id " +
            "left join teachers t2 on l.teacher_id = t2.id " +
            "where l.id = ?1", nativeQuery = true)
    Optional<Lesson> getById(long id);


    @Query(value = "select * from lessons where teacher_id = ?1 and DATE(start_date_time) = ?2", nativeQuery = true)
    List<Lesson> getLessonByTeacherAndStartDate(long id, LocalDate date);

    @Query(value = "select distinct l.* from lessons l " +
            "left OUTER join lesson_group lg on l.id = lg.lesson_id " +
            "left OUTER join groups g on g.id = lg.group_id " +
            "where lg.group_id = :groupId and DATE(l.start_date_time) = :date", nativeQuery = true)
    List<Lesson> getLessonByGroupIdAndStartDate(@Param("groupId")long id, @Param("date") LocalDate date);

    @Query(value = "select distinct l.* from lessons l " +
            "left outer join lesson_group lg on l.id = lg.lesson_id " +
            "where lg.group_id in (:groupIds) and DATE(l.start_date_time) = :date", nativeQuery = true)
    List<Lesson> getLessonsByGroupIdsAndStartDate(@Param("groupIds") List<Long> groupIds, @Param("date") LocalDate date);


    //    @Query(value = "select * from lessons where group_id = ?1 and week_number = ?2 and year = ?3", nativeQuery = true)
//    List<Lesson> getLessonByGroupAndWeekNumberAndYear(long id, int weekNumber, int year);
    @Query(value = "select distinct l.* from lessons l " +
            "left outer join lesson_group lg on l.id = lg.lesson_id " +
            "where lg.group_id = :groupId and l.week_number = :weekNumber and l.year = :year", nativeQuery = true)
    List<Lesson> getLessonByGroupAndWeekNumberAndYear(@Param("groupId") long groupId,
                                                      @Param("weekNumber") int weekNumber,
                                                      @Param("year") int year);


    @Query(value = "select * from lessons where discipline_id = ?1 and year = ?2", nativeQuery = true)
    List<Lesson> getAllByDisciplineAndYear(Discipline discipline, int year);

    // todo updated to comply with newly added List<Group> in Lesson
//    @Query(value = "select * from lessons where teacher_id = ?1 and week_number = ?2 and year = ?3", nativeQuery = true)
    @Query(value = "select distinct l.* from lessons l " +
            "left OUTER join lesson_group lg on l.id = lg.lesson_id " +
            "left OUTER join groups g on g.id = lg.group_id " +
            "where l.teacher_id = ?1 and l.week_number = ?2 and l.year = ?3", nativeQuery = true)
    List<Lesson> getLessonByTeacherAndWeekNumberAndYear(long id, int weekNumber, int year);

    //    @Query(value = "select * from lessons where teacher_id = ?1 and month = ?2 and year = ?3", nativeQuery = true)
    @Query(value = "select distinct l.* from lessons l " +
            "left OUTER join lesson_group lg on l.id = lg.lesson_id " +
            "left OUTER join groups g on g.id = lg.group_id " +
            "where l.teacher_id = ?1 and l.month = ?2 and l.year = ?3", nativeQuery = true)
    List<Lesson> getLessonByTeacherAndMonthAndYear(long id, int month, int year);
}
