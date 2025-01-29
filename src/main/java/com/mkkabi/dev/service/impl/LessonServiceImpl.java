package com.mkkabi.dev.service.impl;

import com.fasterxml.jackson.databind.deser.BasicDeserializerFactory;
import com.mkkabi.dev.dto.GroupDto;
import com.mkkabi.dev.dto.LessonDto;
import com.mkkabi.dev.dto.LessonDtoConverter;
import com.mkkabi.dev.exception.DuplicateEventException;
import com.mkkabi.dev.exception.NullEntityReferenceException;
import com.mkkabi.dev.model.Discipline;
import com.mkkabi.dev.model.Group;
import com.mkkabi.dev.model.Lesson;
import com.mkkabi.dev.model.TimeFrame;
import com.mkkabi.dev.repository.LessonRepository;
import com.mkkabi.dev.tools.AppLogger;
import com.mkkabi.dev.tools.DateTimeService;
import com.mkkabi.dev.service.LessonService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

import com.mkkabi.dev.service.TimeFrameService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final Logger logger = new AppLogger(this.getClass().getSimpleName());

    private LessonRepository repository;
    private final TimeFrameService timeFrameService;

    @Transactional
    @Override
    public Lesson create(Lesson lesson) {
        try {
            Lesson savedLesson = repository.save(lesson);
            logger.info("saved lesson " + savedLesson.getId());
            return savedLesson;
        } catch (IllegalArgumentException e) {
            logger.warning("lesson was null, no lesson created");
            throw new NullEntityReferenceException("Lesson was not created, try again");
        }
    }

    @Transactional
    @Override
    public Lesson readById(long id) {
        Optional<Lesson> optional = repository.findById(id);

        if (optional.isPresent()) {
            logger.info("reading lesson, found lesson " + optional.get().toString());
            return optional.get();
        }
        logger.warning("could not find lesson with the specified ID " + id);
        throw new EntityNotFoundException("Could not find lesson with ID " + id);
    }

    @Transactional
    @Override
    public Lesson readByIdWithTeacher(long id) {
        Optional<Lesson> optional = repository.getById(id);

        if (optional.isPresent()) {
            logger.info("reading lesson, found lesson " + optional.get().toString());
            return optional.get();
        }
        logger.warning("could not find lesson with the specified ID " + id);
        throw new EntityNotFoundException("Could not find lesson with ID " + id);
    }

    @Transactional
    @Override
    public Lesson update(Lesson lesson) {
        if (lesson != null) {
            logger.info("updating valid lesson " + lesson.getId());
            Lesson oldLesson = readById(lesson.getId());
            if (oldLesson != null) {

                logger.info("updating lesson id = " + lesson.getId() + " lesson toString = " + lesson.toString());
                return repository.save(lesson);
            }
        }
        logger.warning("lesson was not found in DB while updating, lesson does not exist");
        throw new NullEntityReferenceException("Something went wrong, try again");
    }

    @Transactional
    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    @Transactional
    @Override
    public void swapDates(long id1, long id2) throws DuplicateEventException {
        Lesson lesson1 = repository.getById(id1).orElse(null);
        Lesson lesson2 = repository.getById(id2).orElse(null);


        if (lesson1 != null && lesson2 != null) {
            System.out.println(lesson1.getStartDateTime());
            System.out.println("===");
            System.out.println(lesson2.getStartDateTime());
            //getting list of lessons for teacher from lesson1 and date from lesson2
            List<LessonDto> lessonsOfTeacher1 = repository.getLessonByTeacherAndStartDate(lesson1.getTeacher().getId(), lesson2.getStartDateTime().toLocalDate())
                    .stream().map(LessonDtoConverter::convertToDto).collect(Collectors.toList());
            List<LessonDto> lessonsOfTeacher2 = repository.getLessonByTeacherAndStartDate(lesson2.getTeacher().getId(), lesson1.getStartDateTime().toLocalDate())
                    .stream().map(LessonDtoConverter::convertToDto).collect(Collectors.toList());
            System.out.println("List of lessonsOfTeacher1 = " + lessonsOfTeacher1.size());
            System.out.println("List of lessonsOfTeacher2 = " + lessonsOfTeacher2.size());

            Optional<LessonDto> optional1 = DateTimeService.checkIfLessonClashesWithAnother(lesson2, lessonsOfTeacher1);
            Optional<LessonDto> optional2 = DateTimeService.checkIfLessonClashesWithAnother(lesson1, lessonsOfTeacher2);

            if (optional1.isPresent()) {
                logger.warning("option lesson1 is not empty");
                throw new DuplicateEventException("teacher " + lesson1.getTeacher().getFio() + " is busy on " + optional1.get().getStartDateTime());
            }

            if (optional2.isPresent()) {
                logger.warning("option lesson2 is not empty");
                throw new DuplicateEventException("teacher " + lesson2.getTeacher().getFio() + " is busy on " + optional2.get().getStartDateTime());
            }

            LocalDateTime startDateTime = lesson1.getStartDateTime();
            LocalDateTime endDateTime = lesson1.getEndDateTime();

            lesson1.setStartDateTime(lesson2.getStartDateTime());
            lesson1.setEndDateTime(lesson2.getEndDateTime());
            lesson1.setLessonDataFromStartDateTime(lesson2.getStartDateTime());

            lesson2.setStartDateTime(startDateTime);
            lesson2.setEndDateTime(endDateTime);
            lesson2.setLessonDataFromStartDateTime(lesson2.getStartDateTime());

            repository.save(lesson1);
            repository.save(lesson2);
        } else {
            logger.info("while trying to swap 2 lessons one or 2 lessons were not found in DB");
            throw new EntityNotFoundException("while trying to swap 2 lessons one or 2 lessons were not found in DB");
        }
    }


    @Transactional
    @Override
    public void moveToOtherDate(long lessonId, long timeFrameId, LocalDate date) throws DuplicateEventException {
        TimeFrame timeFrame = timeFrameService.getTimeFrameById(timeFrameId);
        System.out.println("lessonService.moveToOtherDate - timeframe found " + timeFrame);
        Optional<Lesson> original = repository.getById(lessonId);
        if (original.isPresent()) {
            System.out.println("original lesson found in LessonServiceImpl = " + original.get().getStartDateTime());
            System.out.println("moving lesson " + lessonId + " with timeStart " + LocalDateTime.of(date, timeFrame.getStartTime()) + " and timeEnd " + LocalDateTime.of(date, timeFrame.getEndTime()));
            moveToOtherDate(original.get(), LocalDateTime.of(date, timeFrame.getStartTime()), LocalDateTime.of(date, timeFrame.getEndTime()));
        } else {
            throw new EntityNotFoundException("Lesson " + lessonId + " not found in DB");
        }
    }

    private Lesson changeLessonDate(Lesson lesson, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        lesson.setStartDateTime(newStartTime);
        lesson.setEndDateTime(newEndTime);
        lesson.setLessonDataFromStartDateTime(newStartTime);
        return lesson;
    }

    private void moveToOtherDate(Lesson original, LocalDateTime start, LocalDateTime end) throws DuplicateEventException {
        Lesson copy = makeCopy(original);
        changeLessonDate(copy, start, end);

        List<LessonDto> teacherLessonDtosForNewDate = repository.getLessonByTeacherAndStartDate(copy.getTeacher().getId(), start.toLocalDate())
                .stream().map(LessonDtoConverter::convertToDto).collect(Collectors.toList());
        System.out.println("found teacherlesson " + teacherLessonDtosForNewDate + " for date " + start.toLocalDate());
        // Fetch lessons that overlap with any of the groups in the lesson
        List<LessonDto> groupLessonDtosForNewDate = repository.getLessonsByGroupIdsAndStartDate(
                        copy.getGroups().stream().map(Group::getId).collect(Collectors.toList()), start.toLocalDate())
                .stream().map(LessonDtoConverter::convertToDto).collect(Collectors.toList());
        System.out.println("groupLessonDtosForNewDate = " + groupLessonDtosForNewDate.size());

        Optional<LessonDto> optionalTeacherLessonDto = DateTimeService.checkIfLessonClashesWithAnother(copy, teacherLessonDtosForNewDate);
        Optional<LessonDto> optionalLessonDtoOfGroup = DateTimeService.checkIfLessonClashesWithAnother(copy, groupLessonDtosForNewDate);

        if (optionalTeacherLessonDto.isPresent()) {
            System.out.println("optionalTeacherLessonDto is present");
            logger.info("optionalTeacherLessonDto is present");
            throw new DuplicateEventException("Teacher already has class on that date in group(s): " + optionalTeacherLessonDto.get().getGroups());
        }

        if (optionalLessonDtoOfGroup.isPresent()) {
            System.out.println("optionalLessonDtoOfGroup is present");
            logger.info("optionalLessonDtoOfGroup is present");
            throw new DuplicateEventException("Group(s) already have class on that date with teacher: " + optionalLessonDtoOfGroup.get().getTeacher());
        }

        repository.save(changeLessonDate(original, start, end));
    }

    @Transactional
    @Override
    public void duplicateLessonToAnotherDate(long lessonId, long timeFrameId, LocalDate date) throws DuplicateEventException {
        TimeFrame timeFrame = timeFrameService.getTimeFrameById(timeFrameId);
        System.out.println("time frame found " + timeFrame);
        Optional<Lesson> optionalLesson = repository.getById(lessonId);
        System.out.println("optionalLesson found " + optionalLesson.orElse(null));
        if (optionalLesson.isPresent()) {
            Lesson lessonExample = optionalLesson.get();
            System.out.println("lessonExample " + lessonExample);
            Lesson lessonCopy = makeCopy(lessonExample);
            System.out.println("lessonCopy " + lessonCopy);
            LocalDateTime newDateTimeStart = LocalDateTime.of(date, timeFrame.getStartTime());
            LocalDateTime newDateTimeEnd = LocalDateTime.of(date, timeFrame.getEndTime());

            lessonCopy.setStartDateTime(newDateTimeStart);
            System.out.println("lessonCopy.getStartDateTime() " + lessonCopy.getStartDateTime());
            lessonCopy.setEndDateTime(newDateTimeEnd);
            System.out.println("lessonCopy.getEndDateTime() " + lessonCopy.getEndDateTime());
            lessonCopy.setLessonDataFromStartDateTime(newDateTimeStart);
            System.out.println("moving to other date " + lessonCopy);
            moveToOtherDate(lessonCopy, newDateTimeStart, newDateTimeEnd);
        }
    }

    @Transactional
    @Override
    public void duplicateWeek(long groupId, int weekNumber, int year) throws DuplicateEventException {
        List<Lesson> lessonSet = repository.getLessonByGroupAndWeekNumberAndYear(groupId, weekNumber, year);
        List<String> errors = new ArrayList<>();
        for (Lesson l : lessonSet) {
            LocalDateTime startDate = l.getStartDateTime().plusWeeks(1);
            LocalDateTime endDate = l.getEndDateTime().plusWeeks(1);
            Lesson newLesson = makeCopy(l);
            try {
                moveToOtherDate(newLesson, startDate, endDate);
            } catch (DuplicateEventException e) {
                errors.add("Could not copy lesson from date " + l.getStartDateTime() + " " + e.getMessage() + " | ");
            }
        }
        if (!errors.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            errors.forEach(sb::append);
            throw new DuplicateEventException(sb.toString());
        }
    }

    private Lesson makeCopy(Lesson original) {
        Lesson copy = new Lesson();
//        copy.setGroups(original.getGroups());
        List<Group> groups = new ArrayList<>(original.getGroups());
        copy.setGroups(groups);
        copy.setDiscipline(original.getDiscipline());
        copy.setTeacher(original.getTeacher());
        copy.setOnline(original.isOnline());
        copy.setClassType(original.getClassType());
        copy.setAuditoriumNumber(copy.getAuditoriumNumber());
        copy.setStartDateTime(original.getStartDateTime());
        copy.setEndDateTime(original.getEndDateTime());
        copy.setLessonDataFromStartDateTime(original.getStartDateTime());
        return copy;
    }

    @Transactional
    @Override
    public List<Lesson> getAll() {
        List<Lesson> lessons = repository.findAll();
        logger.info("found " + lessons.size() + " lessons in DB");
        return lessons.isEmpty() ? new ArrayList<>() : lessons;
    }


    @Transactional
    @Override
    public List<LessonDto> getAllAsDto() {
        return getAll().stream().map(LessonDtoConverter::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<Lesson> getLessonByTeacherAndWeekNumberAndYear(long id, int weekNumber, int year) {
        List<Lesson> lessons = repository.getLessonByTeacherAndWeekNumberAndYear(id, weekNumber, year);
        logger.info("searching for lessons of teacher ID " + id + "found " + lessons.size() + " lessons in DB");
        return lessons.isEmpty() ? new ArrayList<>() : lessons;
    }

    @Transactional
    @Override
    public List<LessonDto> getLessonDtoByTeacherAndWeekNumberAndYear(long id, int weekNumber, int year) {
        List<LessonDto> lessons = repository.getLessonByTeacherAndWeekNumberAndYear(id, weekNumber, year).stream().map(LessonDtoConverter::convertToDto).collect(Collectors.toList());
        logger.info("searching for DTO lessons of teacher ID " + id + "found " + lessons.size() + " lessons in DB");
        return lessons.isEmpty() ? new ArrayList<>() : lessons;
    }

    @Transactional
    @Override
    public List<LessonDto> getLessonByTeacherIdAndStartDate(long id, LocalDate startDate) {
        List<LessonDto> lessons = repository.getLessonByTeacherAndStartDate(id, startDate).stream().map(LessonDtoConverter::convertToDto).collect(Collectors.toList());
        logger.info("searching for lessons of Teacher ID " + id + " on startDate " + startDate + " found " + lessons.size() + " lessons in DB");
        return lessons.isEmpty() ? new ArrayList<>() : lessons;
    }

    @Transactional
    @Override
    public List<LessonDto> getLessonByGroupIdAndStartDate(long id, LocalDate startDate) {
        List<LessonDto> lessons = repository.getLessonByGroupIdAndStartDate(id, startDate).stream().map(LessonDtoConverter::convertToDto).collect(Collectors.toList());
        logger.info("searching for lessons of group ID " + id + " startDate " + startDate + " found " + lessons.size() + " lessons in DB");
        return lessons.isEmpty() ? new ArrayList<>() : lessons;
    }

    @Transactional
    @Override
    public List<LessonDto> getLessonByGroupAndWeekNumberAndYear(long id, int weekNumber, int year) {
        List<LessonDto> lessons = repository.getLessonByGroupAndWeekNumberAndYear(id, weekNumber, year).stream().map(LessonDtoConverter::convertToDto).collect(Collectors.toList());
        logger.info("searching for lessons group id " + id + " weeknumber = " + weekNumber + " found " + lessons.size() + " lessons in DB");
        return lessons.isEmpty() ? new ArrayList<>() : lessons;
    }

    @Transactional
    @Override
    public List<Lesson> getAllByDisciplineAndYear(Discipline discipline, int year) {
        List<Lesson> lessons = repository.getAllByDisciplineAndYear(discipline, year);
        logger.info("searching for lessons of " + discipline.getName() + "found " + lessons.size() + " lessons in DB");
        return lessons.isEmpty() ? new ArrayList<>() : lessons;
    }

    @Transactional
    @Override
    public List<LessonDto> getLessonByTeacherAndMonthAndYear(long id, int month, int year) {
        List<LessonDto> lessons = repository.getLessonByTeacherAndMonthAndYear(id, month, year).stream().map(LessonDtoConverter::convertToDto).collect(Collectors.toList());
        logger.info("searching for lessons group id " + id + " month = " + month + " year = " + year + " found " + lessons.size() + " lessons in DB");
        return lessons.isEmpty() ? new ArrayList<>() : lessons;
    }

}
