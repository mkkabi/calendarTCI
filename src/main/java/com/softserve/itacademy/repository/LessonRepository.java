package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

//    select p
//    from Post p
//    left join fetch p.comments
//    where p.title like :titlePattern

//    @Query( "select l from Lesson l left join fetch l.teacher, l.discipline, l.group, l.classType where l.id = :id ")
    @Query( value = "select * from lessons l left join disciplines d on l.discipline_id = d.id " +
            "left join teachers t2 on l.teacher_id = t2.id " +
            "where l.id = ?1", nativeQuery = true)
    Optional<Lesson> getById(long id);

    @EntityGraph(attributePaths = {"teachers"})
    @Query("select l from Lesson l where l.id = :id")
    Optional<Lesson> getByIdWithTeacher(long id);

    @Query(value = "select * from lessons where teacher_id = ?1 and week_number = ?2 and year = ?3", nativeQuery = true)
    List<Lesson> getLessonByTeacherAndWeekNumberAndYear(long id, int weekNumber, int year);

    @Query(value = "select * from lessons where teacher_id = ?1 and DATE(start_date_time) = ?2", nativeQuery = true)
    List<Lesson>  getLessonByTeacherAndStartDate(long id, LocalDate date);

    @Query(value = "select * from lessons where group_id = ?1 and DATE(start_date_time) = ?2", nativeQuery = true)
    List<Lesson> getLessonByGroupIdAndStartDate(long id, LocalDate date);

    @Query(value = "select * from lessons where group_id = ?1 and week_number = ?2 and year = ?3", nativeQuery = true)
    List<Lesson> getLessonByGroupAndWeekNumberAndYear(long id, int weekNumber, int year);

    @Query(value = "select * from lessons where teacher_id = ?1 and month = ?2 and year = ?3", nativeQuery = true)
    List<Lesson> getLessonByTeacherAndMonthAndYear(long id, int month, int year);

    @Query(value = "select * from lessons where discipline_id = ?1 and year = ?2", nativeQuery = true)
    List<Lesson> getAllByDisciplineAndYear(Discipline discipline, int year);


}
