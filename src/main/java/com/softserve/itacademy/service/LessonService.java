package com.softserve.itacademy.service;

import com.softserve.itacademy.dto.LessonDto;
import com.softserve.itacademy.exception.DuplicateEventException;
import com.softserve.itacademy.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LessonService {
    Lesson create(Lesson lesson);
    Lesson readById(long id);
    Lesson readByIdWithTeacher(long id);
    Lesson update(Lesson lesson);
    void delete(long id);
    List<Lesson> getAll();
    List<LessonDto> getAllAsDto();

    List<Lesson> getLessonByTeacherAndWeekNumberAndYear(long id, int weekNumber, int year);
    List<LessonDto> getLessonDtoByTeacherAndWeekNumberAndYear(long id, int weekNumber, int year);

    List<LessonDto> getLessonByTeacherIdAndStartDate(long id, LocalDate startDate);

    List<LessonDto> getLessonByGroupIdAndStartDate(long id, LocalDate startDate);
    List<LessonDto> getLessonByGroupAndWeekNumberAndYear(long id, int weekNumber, int year);
    List<Lesson> getAllByDisciplineAndYear(Discipline discipline, int year);

    List<LessonDto> getLessonByTeacherAndMonthAndYear(long id, int month, int year);

    void swapDates(long id1, long id2) throws DuplicateEventException;
    void moveToOtherDate(long lessonId, long timeFrameId, LocalDate date) throws DuplicateEventException;

    void duplicateLessonToAnotherDate(long lessonId, long timeFrameId, LocalDate date) throws DuplicateEventException;

    void duplicateWeek(long groupId, int weekNumber, int year) throws DuplicateEventException;
}