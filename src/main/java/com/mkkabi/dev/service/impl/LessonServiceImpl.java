package com.mkkabi.dev.service.impl;

import com.mkkabi.dev.dto.LessonDto;
import com.mkkabi.dev.dto.LessonDtoConverter;
import com.mkkabi.dev.exception.DuplicateEventException;
import com.mkkabi.dev.exception.NullEntityReferenceException;
import com.mkkabi.dev.model.*;
import com.mkkabi.dev.repository.GroupRepository;
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

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final Logger logger = new AppLogger(this.getClass().getSimpleName());

    private LessonRepository repository;
    private GroupRepository groupRepository;
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
            logger.info("reading lesson, found lesson " + optional.get());
            return optional.get();
        }
        logger.warning("could not find lesson with the specified ID " + id);
        throw new EntityNotFoundException("Could not find lesson with ID " + id);
    }

    @Transactional
    @Override
    public Lesson readByIdWithTeacher(long id) {
        Optional<Lesson> optional = repository.getByIdWithTeacher(id);

        if (optional.isPresent()) {
            logger.info("reading lesson, found lesson " + optional.get());
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

                logger.info("updating lesson id = " + lesson.getId() + " lesson toString = " + lesson);
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
            checkIfClashesForTeacher(lesson2, lesson1.getTeacher().getId());
            checkIfClashesForTeacher(lesson1, lesson2.getTeacher().getId());

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
        Lesson original = repository.getById(lessonId).orElse(null);
        if (original!=null) {
//            moveToOtherDate(original.get(), LocalDateTime.of(date, timeFrame.getStartTime()), LocalDateTime.of(date, timeFrame.getEndTime()));
            Lesson copy = makeCopy(original);
            changeLessonDate(copy, LocalDateTime.of(date, timeFrame.getStartTime()), LocalDateTime.of(date, timeFrame.getEndTime()));
            checkIfClashesForTeacher(copy, copy.getTeacher().getId());
            checkIfClashesForGroup(copy, copy.getGroups().stream().map(Group::getId).collect(Collectors.toList()));
            changeLessonDate(original, LocalDateTime.of(date, timeFrame.getStartTime()), LocalDateTime.of(date, timeFrame.getEndTime()));
            repository.save(original);
        } else {
            throw new EntityNotFoundException("Lesson " + lessonId + " not found in DB");
        }
    }

    private void changeLessonDate(Lesson lesson, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        lesson.setStartDateTime(newStartTime);
        lesson.setEndDateTime(newEndTime);
        lesson.setLessonDataFromStartDateTime(newStartTime);
    }

    private void checkIfClashesForTeacher(Lesson lesson, long teacherId) throws DuplicateEventException {
        LocalDate start = lesson.getStartDateTime().toLocalDate();
        List<LessonDto> teacherLessonDtosForNewDate = repository.getLessonByTeacherAndStartDate(teacherId, start)
                .stream().map(LessonDtoConverter::convertToDto).collect(Collectors.toList());
        Optional<LessonDto> optionalTeacherLessonDto = DateTimeService.checkIfLessonClashesWithAnother(lesson, teacherLessonDtosForNewDate);
        if (optionalTeacherLessonDto.isPresent()) {
            logger.info("optionalTeacherLessonDto is present");
            throw new DuplicateEventException("Teacher already has class on that date in group(s): " + optionalTeacherLessonDto.get().getGroups());
        }
    }

    private void checkIfClashesForGroup(Lesson lesson, List<Long> groupIds) throws DuplicateEventException {
        LocalDate start = lesson.getStartDateTime().toLocalDate();
        // Fetch lessons that overlap with any of the groups in the lesson
        List<LessonDto> groupLessonDtosForNewDate = repository.getLessonsByGroupIdsAndStartDate(groupIds, start)
                .stream().map(LessonDtoConverter::convertToDto).collect(Collectors.toList());
        Optional<LessonDto> optionalLessonDtoOfGroup = DateTimeService.checkIfLessonClashesWithAnother(lesson, groupLessonDtosForNewDate);
        if (optionalLessonDtoOfGroup.isPresent()) {
            logger.info("optionalLessonDtoOfGroup is present");
            throw new DuplicateEventException("Group(s) already have class on that date with teacher: " + optionalLessonDtoOfGroup.get().getTeacher());
        }
    }


    @Transactional
    public void duplicateLessonToAnotherDate(long lessonId, long timeFrameId, LocalDate date) throws DuplicateEventException {
        TimeFrame timeFrame = timeFrameService.getTimeFrameById(timeFrameId);
        Optional<Lesson> original = repository.getByIdWithTeacher(lessonId);
        if (original.isPresent()) {
            Lesson originalLesson = original.get();
            LessonDto lessonDto = LessonDtoConverter.convertToDto(originalLesson);
            Discipline discipline = originalLesson.getDiscipline();
            Teacher teacher = originalLesson.getTeacher();
            ClassType classType = originalLesson.getClassType();

            Lesson lessonCopy = new Lesson();
            List<Long> groupsIds = groupRepository.getLessonGroupsIDs(lessonDto.getId());
            List<Group> groups = new ArrayList<>();
            for (Long groupId : groupsIds) {
                groups.add(groupRepository.findById(groupId).orElse(null));
            }
            LocalDateTime newDateTimeStart = LocalDateTime.of(date, timeFrame.getStartTime());
            LocalDateTime newDateTimeEnd = LocalDateTime.of(date, timeFrame.getEndTime());
            lessonCopy.setStartDateTime(newDateTimeStart);
            lessonCopy.setEndDateTime(newDateTimeEnd);
            lessonCopy.setLessonDataFromStartDateTime(newDateTimeStart);
            lessonCopy.setGroups(groups);
            lessonCopy.setDiscipline(discipline);
            lessonCopy.setTeacher(teacher);
            lessonCopy.setOnline(lessonDto.isOnline());
            lessonCopy.setClassType(classType);
            lessonCopy.setAuditoriumNumber(lessonDto.getAuditoriumNumber());
            lessonCopy.setLessonDataFromStartDateTime(newDateTimeStart);
            checkIfClashesForTeacher(lessonCopy, teacher.getId());
            checkIfClashesForGroup(lessonCopy, groupsIds);
            repository.save(lessonCopy);
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
            Lesson copy = makeCopy(l);
            try {
//                moveToOtherDate(newLesson, startDate, endDate);
                changeLessonDate(copy, startDate, endDate);
                checkIfClashesForTeacher(copy, copy.getTeacher().getId());
                checkIfClashesForGroup(copy, copy.getGroups().stream().map(Group::getId).collect(Collectors.toList()));
                repository.save(copy);
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
