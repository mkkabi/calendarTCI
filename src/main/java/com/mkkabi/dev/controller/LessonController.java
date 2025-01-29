package com.mkkabi.dev.controller;

import com.mkkabi.dev.dto.*;
import com.mkkabi.dev.model.*;
import com.mkkabi.dev.service.*;
import com.mkkabi.dev.service.impl.*;
import com.mkkabi.dev.tools.AppLogger;
import com.mkkabi.dev.tools.DateTimeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Controller
@AllArgsConstructor
@RequestMapping("/lessons")
public class LessonController {
    private final LessonServiceImpl lessonService;
    private final ClassTypeServiceImpl classTypeService;
    private final TimeFrameServiceImpl timeFrameService;
    private final TeacherServiceImpl teacherService;
    private final GroupServiceImpl groupService;
    private final Logger logger = new AppLogger(this.getClass().getSimpleName());
    private final SettingsServiceImpl settingsService;


    @GetMapping("/all")
    public String getAll(Model model) {
        List<LessonDto> lessonsDto = lessonService.getAllAsDto();
        model.addAttribute("lessonsDto", lessonsDto);
        if (lessonsDto == null || lessonsDto.isEmpty())
            model.addAttribute("message", "No lessons found");
        return "lesson-list";
    }

    @GetMapping(value = {"/create/group/{groupId}", "/create/group/{groupId}/{timeframeId}/{dateStart}"})
    public String create(@PathVariable("groupId") long groupId,
                         @PathVariable("timeframeId") Optional<Long> timeframeId,
                         @PathVariable("dateStart") Optional<String> dateStart,
                         Model model) {

        timeframeId.ifPresent(id -> model.addAttribute("timeFrameId", id));
        dateStart.ifPresent(date -> model.addAttribute("startDate", LocalDate.parse(date)));

        model.addAttribute("newLesson", new Lesson());
        setAllModelDataForNewLesson(model, groupId);
        return "create-lesson-from-group";
    }

    private void setAllModelDataForNewLesson(Model model, long groupId) {
        Group group = groupService.readById(groupId);
        //todo check
        List<Discipline> disciplines = group.getDisciplines().stream()
                .filter(discipline -> {
                    int disciplineCourseNumber = discipline.getSemester() % 2 == 0 ? discipline.getSemester() / 2 : (discipline.getSemester() + 1) / 2;
                    return disciplineCourseNumber == group.getCourseNumber();
                })
                .sorted(Comparator.comparing(o -> o.getName().getName()))
                .collect(Collectors.toList());
        List<TeacherDtoNoDisciplines> teachers = teacherService.findActiveTeachersForGroup(groupId)
                .stream().map(TeacherDtoTransformer::convertToDtoNoDisciplines)
                .distinct()
                .sorted(Comparator.comparing(TeacherDtoNoDisciplines::getName))
                .collect(Collectors.toList());
        model.addAttribute("groupId", groupId);
        model.addAttribute("onlineColor", settingsService.findLatest().getColorForOnlineClasses());
        model.addAttribute("classTypes", classTypeService.getAll());
        model.addAttribute("timeFrames", timeFrameService.getAll());
        model.addAttribute("disciplines", disciplines);
        model.addAttribute("teachers", teachers);
        Set<GroupDto> allGroups = groupService.getAllAsDto();
        model.addAttribute("allGroups", allGroups);
    }


    @PostMapping("/create/group/{groupId}")
    public String create(@Validated @ModelAttribute("newLesson") Lesson newLesson,
                         @PathVariable("groupId") long groupId,
                         @Min(1) @RequestParam("timeFrameId") long timeFrameId,
                         @NotEmpty @Pattern(regexp = "\\n{4}-\\n{2}-\\n{2}") @RequestParam("startDate") String startDate,
                         BindingResult result,
                         Model model,
                         @RequestParam(value = "groupsList", required = false) List<Long> groupDtoSet) {
        // checking for errors
        if (newLesson.getDiscipline() == null || newLesson.getTeacher() == null) {
            return handleModelError("Check Discipline and Teacher fields", model, newLesson, startDate, groupId);
        }
        if (result.hasErrors()) {
            logger.warning("Lesson data has validation errors: " + result.getAllErrors());
            return handleModelError(null, model, newLesson, startDate, groupId);
        }
        if (startDate.isEmpty()) {
            return handleModelError("Date and timeFrame need to be filled out", model, newLesson, startDate, groupId);
        }

        TimeFrame timeFrame = timeFrameService.getTimeFrameById(timeFrameId);
        LocalDate parsedDate = LocalDate.parse(startDate);
        LocalDateTime startDateTime = LocalDateTime.of(parsedDate, timeFrame.getStartTime());
        LocalDateTime endDateTime = LocalDateTime.of(parsedDate, timeFrame.getEndTime());

        newLesson.setStartDateTime(startDateTime);
        newLesson.setEndDateTime(endDateTime);
        newLesson.setLessonDataFromStartDateTime(startDateTime);

        if (!checkForClashingLessonsForTeacher(model, newLesson).isEmpty()) {
            return "create-lesson-from-group";
        }
        return (groupDtoSet != null && !groupDtoSet.isEmpty()) ?
                handleGroupDtoSet(newLesson, model, groupDtoSet) :
                handleSingleGroup(newLesson, model, groupId);
    }

    private String handleSingleGroup(Lesson newLesson, Model model, long groupId) {
        if (checkForClashingLessonsForGroup(model, groupId, newLesson)) {
            return "create-lesson-from-group";
        }

        Group group = groupService.readById(groupId);
        newLesson.setGroups(Collections.singletonList(group));
        lessonService.create(newLesson);
        return redirectToGroupWithWeekOffset(newLesson.getStartDateTime().toLocalDate());
    }

    private String handleGroupDtoSet(Lesson newLesson, Model model, List<Long> groupDtoSet) {
        if (!checkForClashingLessonsForMultipleGroup(model, newLesson, groupDtoSet).isEmpty()) {
            return "create-lesson-from-group";
        }
        List<Group> groups = groupDtoSet.stream().map(groupService::readById)
                .collect(Collectors.toList());
        newLesson.setGroups(groups);
        lessonService.create(newLesson);
        return redirectToGroupWithWeekOffset(newLesson.getStartDateTime().toLocalDate());
    }

    private String redirectToGroupWithWeekOffset(LocalDate startDate) {
        int weekOffset = calculateWeekOffset(startDate);
        return "redirect:/groups/{groupId}/read/" + weekOffset;
    }

    private int calculateWeekOffset(LocalDate startDate) {
        WeekFields weekFields = WeekFields.of(Locale.GERMANY);
        int currentWeek = LocalDate.now().get(weekFields.weekOfWeekBasedYear());
        int lessonWeek = startDate.get(weekFields.weekOfWeekBasedYear());
        return lessonWeek - currentWeek;
    }

    private String handleModelError(String warning, Model model, Lesson newLesson, String startDate, long groupId) {
        if (warning != null) {
            model.addAttribute("warning", warning);
        }
        model.addAttribute("newLesson", newLesson);
        model.addAttribute("startDate", startDate);
        setAllModelDataForNewLesson(model, groupId);
        return "create-lesson-from-group";
    }

    private String checkForClashingLessonsForTeacher(Model model, Lesson newLesson) {
        long teacherId = newLesson.getTeacher().getId();
        LocalDate parsedDate = newLesson.getStartDateTime().toLocalDate();
        List<LessonDto> lessonsInSameDayByTeacher = lessonService.getLessonByTeacherIdAndStartDate(teacherId, parsedDate);
        Optional<LessonDto> optionalLessonOfTeacher = DateTimeService.checkIfLessonClashesWithAnother(newLesson, lessonsInSameDayByTeacher);
        // todo testing
        logger.info("optionalLessonOfTeacher = " + (optionalLessonOfTeacher.isPresent() ? optionalLessonOfTeacher.get() : " empty"));
        if (optionalLessonOfTeacher.isPresent()) {
            model.addAttribute("duplicateLessonOfTeacher", optionalLessonOfTeacher.get());
            model.addAttribute("newLesson", newLesson);
            model.addAttribute("warning", "Teacher is busy on that date");
            return "create-lesson-from-group";
        }
        return "";
    }

    private String checkForClashingLessonsForMultipleGroup(Model model, Lesson newLesson, List<Long> groups) {
        for (long groupId : groups) {
            GroupDto group = groupService.readByIdAsDto(groupId);
            if (checkForClashingLessonsForGroup(model, group.getId(), newLesson))
                return "create-lesson-from-group";
        }
        return "";
    }

    private boolean checkForClashingLessonsForGroup(Model model, long groupId, Lesson newLesson) {
        LocalDate parsedDate = newLesson.getStartDateTime().toLocalDate();
        List<LessonDto> lessonsInSameDayByGroup = lessonService.getLessonByGroupIdAndStartDate(groupId, parsedDate);
        Optional<LessonDto> optionalLessonOfGroup = DateTimeService.checkIfLessonClashesWithAnother(newLesson, lessonsInSameDayByGroup);
        logger.info("optionalLessonOfGroup = " + (optionalLessonOfGroup.isPresent() ? optionalLessonOfGroup.get() : " empty"));

        if (optionalLessonOfGroup.isPresent()) {
            model.addAttribute("duplicateLessonOfGroup", optionalLessonOfGroup.get());
            model.addAttribute("newLesson", newLesson);
            model.addAttribute("warning", "Group is busy on that date");
            setAllModelDataForNewLesson(model, groupId);
            return true;
        }
        return false;
    }


    @GetMapping("/create/teacher-off/{teacherId}")
    public String createTeacherTimeOffAsLesson(@PathVariable("teacherId") long teacherId, Model model) {
        model.addAttribute("newLesson", new Lesson());
        model.addAttribute("classTypes", classTypeService.getAll());
        return "create-time-off";
    }


    @PostMapping("/create/teacher-off/{teacherId}")
    public String createTeacherTimeOffAsLesson(@Validated @ModelAttribute("newLesson") Lesson newLesson,
                                               @PathVariable("teacherId") long teacherId,
                                               BindingResult result, Model model) {

        newLesson.setGroups(null);
        newLesson.setDiscipline(null);
        newLesson.setLessonDataFromStartDateTime(newLesson.getStartDateTime());
        newLesson.setTeacher(teacherService.readById(teacherId));

        if (checkForClashingLessonsForTeacher(model, newLesson).isEmpty()) {
            lessonService.create(newLesson);
            return "redirect:/teachers/{teacherId}/open";
        }

        model.addAttribute("newLesson", newLesson);
        model.addAttribute("classTypes", classTypeService.getAll());
        return "create-time-off";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") long id, @RequestParam("urlFrom") String url) {
        try {
            lessonService.delete(id);
        } catch (RuntimeException e) {
            logger.warning(e.getMessage());
        }
        return "redirect:" + url;
    }


    @GetMapping(value = {"/{id}/group/{groupId}/edit", "/{id}/group/{groupId}/update"})
    public String edit(@PathVariable long id, @PathVariable long groupId, Model model) {
        LessonDto lessonDto = LessonDtoConverter.convertToDto(lessonService.readById(id));
        model.addAttribute("classTypes", classTypeService.getAll());
        model.addAttribute("timeFrames", timeFrameService.getAll());
        model.addAttribute("groupId", groupId);
        model.addAttribute("lesson", lessonDto);
        return "edit-lesson-for-group";
    }

    @PostMapping(value = {"/{id}/group/{groupId}/edit", "/{id}/group/{groupId}/update"})
    public String edit(@PathVariable long id, @Validated @ModelAttribute("lesson") Lesson lesson, BindingResult result) {
        if (result.hasErrors()) {
            return "edit-lesson-for-group";
        }
        lessonService.update(lesson);
        return "redirect:/groups/{groupId}/open";
    }

    @GetMapping(value = "/check-duplicates/")
    public String checkDuplicateLessons(RedirectAttributes rat) {
        List<LessonDto> lessonDtos = lessonService.getAllAsDto();
        Map<String, List<LessonDto>> lessonsMappedToTeachers = new HashMap<>();
        Map<String, List<LessonDto>> lessonsMappedToGroups = new HashMap<>();
        Set<LessonDto> lessonsThatClashTeachers = new HashSet<>();
        Set<LessonDto> lessonsThatClashGroups = new HashSet<>();
        StringBuffer errors = new StringBuffer();
        for (LessonDto lsn : lessonDtos) {
            for (Long g : lsn.getGroups()) {
                String key = lsn.getTeacher() + " - " + lsn.getStartDateTime();
                String groupKey = g + " - " + lsn.getStartDateTime();
                lessonsMappedToTeacher(lessonsMappedToTeachers, lessonsThatClashTeachers, lsn, key);
                lessonsMappedToTeacher(lessonsMappedToGroups, lessonsThatClashGroups, lsn, groupKey);
            }
        }
        if (errors.length() > 0) {
            rat.addFlashAttribute("warning", errors);
        }
        rat.addFlashAttribute("lessonsDto", lessonsThatClashTeachers.stream().sorted(Comparator.comparing(LessonDto::getStartDateTime)).collect(Collectors.toCollection(LinkedHashSet::new)));
        rat.addFlashAttribute("lessonsDtoGroup", lessonsThatClashGroups.stream().sorted(Comparator.comparing(LessonDto::getStartDateTime)).collect(Collectors.toCollection(LinkedHashSet::new)));
        return "redirect:/";
    }

    private void lessonsMappedToTeacher(Map<String, List<LessonDto>> lessonsMappedToGroups, Set<LessonDto> lessonsThatClashGroups, LessonDto lsn, String groupKey) {
        if (!lessonsMappedToGroups.containsKey(groupKey)) {
            List<LessonDto> groupValue = new ArrayList<>();
            groupValue.add(lsn);
            lessonsMappedToGroups.put(groupKey, groupValue);
        } else {
            lessonsMappedToGroups.get(groupKey).add(lsn);
            lessonsThatClashGroups.addAll(lessonsMappedToGroups.get(groupKey));
        }
    }
}