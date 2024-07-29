package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.*;
import com.softserve.itacademy.model.*;
import com.softserve.itacademy.service.*;
import com.softserve.itacademy.service.impl.SettingsServiceImpl;
import com.softserve.itacademy.tools.AppLogger;
import com.softserve.itacademy.tools.DateTimeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/lessons")
public class LessonController {
    private final LessonService lessonService;
    private final ClassTypeService classTypeService;
    private final TimeFrameService timeFrameService;
    private final TeacherService teacherService;
    private final DisciplineService disciplineService;
    private final GroupService groupService;
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
    public String create(@PathVariable("groupId") long groupId, @PathVariable("timeframeId") Optional<Long> timeframeId, @PathVariable("dateStart") Optional<String> dateStart, Model model) {

        if (timeframeId.isPresent() && dateStart.isPresent()) {
            LocalDate date = LocalDate.parse(dateStart.get());
            model.addAttribute("startDate", date);
            model.addAttribute("timeFrameId", timeframeId.get());
        }
        model.addAttribute("newLesson", new Lesson());
        model.addAttribute("groupId", groupId);
        model.addAttribute("onlineColor", settingsService.findLatest().getColorForOnlineClasses());

        setAllModelDataForNewLesson(model, groupId);
        return "create-lesson-from-group";
    }

    private void setAllModelDataForNewLesson(Model model, long groupId) {
        model.addAttribute("classTypes", classTypeService.getAll());
        model.addAttribute("timeFrames", timeFrameService.getAll());
//        model.addAttribute("teachers", teacherService.getAllAsDto());
//        model.addAttribute("disciplines", disciplineService.getAllAsDto());
        Group group = groupService.readById(groupId);
        List<Discipline> disciplines = group.getDisciplines().stream()
                .map(discipline -> disciplineService.readByIdWithTeachers(discipline.getId()))
                .collect(Collectors.toList());
        Set<TeacherDtoNoDisciplines> teachers = disciplines.stream().map(Discipline::getTeachers)
                .flatMap(List::stream).distinct()
                .map(TeacherDtoTransformer::convertToDtoNoDisciplines)
                .collect(Collectors.toSet());
        model.addAttribute("disciplines", disciplines);
        model.addAttribute("teachers", teachers);
    }

    @PostMapping("/create/group/{groupId}")
    public String create(@Validated @ModelAttribute("newLesson") Lesson newLesson,
                         @PathVariable("groupId") long groupId, @Min(1) @RequestParam("timeFrameId") long timeFrameId,
                         @NotEmpty @Pattern(regexp = "\\n{4}-\\n{2}-\\n{2}") @RequestParam("startDate") String startDate, BindingResult result, Model model) {
        System.out.println(startDate);
        System.out.println(timeFrameId);
        if (result.hasErrors()) {
            logger.warning("lesson dada has result errors " + result.getAllErrors());
            setAllModelDataForNewLesson(model, groupId);
//            model.addAttribute("newLesson", newLesson);
            model.addAttribute("startDate", startDate);
            return "create-lesson-from-group";
        }
        if (startDate.isEmpty()) {
//            model.addAttribute("newLesson", newLesson);
            model.addAttribute("warning", "Date and timeFrame need to be filled out");
            setAllModelDataForNewLesson(model, groupId);
            return "create-lesson-from-group";
        }

        TimeFrame timeFrame = timeFrameService.getTimeFrameById(timeFrameId);
        LocalDate parsedDate = LocalDate.parse(startDate);

        DateTimeService dateTimeService = new DateTimeService();
        LocalDateTime startDateTime = LocalDateTime.of(parsedDate, timeFrame.getStartTime());
        LocalDateTime endDateTime = LocalDateTime.of(parsedDate, timeFrame.getEndTime());
        long teacherId = newLesson.getTeacher().getId();
        List<LessonDto> lessonsInSameDayByGroup = lessonService.getLessonByGroupIdAndStartDate(groupId, parsedDate);
        List<LessonDto> lessonsInSameDayByTeacher = lessonService.getLessonByTeacherIdAndStartDate(teacherId, parsedDate);
        System.out.println("==========1");
        Group group = groupService.readById(groupId);
        newLesson.setGroup(group);
        newLesson.setStartDateTime(startDateTime);
        newLesson.setEndDateTime(endDateTime);

        newLesson.setLessonDataFromStartDateTime(startDateTime);

        logger.info("searching for optional Group lesson date = " + startDateTime.toLocalDate() + "| teacherID = " + teacherId + "| size of lessonsInSameDayByGroup = " +
                lessonsInSameDayByGroup.size() + " | size of lessonsInSameDayByTeacher = " + lessonsInSameDayByTeacher);


        Optional<LessonDto> optionalLessonOfTeacher = dateTimeService.checkIfLessonClashesWithAnother(newLesson, lessonsInSameDayByTeacher);
        logger.info("optionalLessonOfTeacher = " + (optionalLessonOfTeacher.isPresent() ? optionalLessonOfTeacher.get() : " empty"));

        Optional<LessonDto> optionalLessonOfGroup = dateTimeService.checkIfLessonClashesWithAnother(newLesson, lessonsInSameDayByGroup);
        logger.info("optionalLessonOfGroup = " + (optionalLessonOfGroup.isPresent() ? optionalLessonOfGroup.get() : " empty"));

        if (optionalLessonOfTeacher.isPresent()) {
//            result.rejectValue("startDate", "startDate", "Teacher "+optionalLessonOfTeacher.get().getTeacher()+" is busy on " + optionalLessonOfTeacher.get().getDateStart()
//                    + " for " + optionalLessonOfTeacher.get().getDiscipline());
            model.addAttribute("duplicateLessonOfTeacher", optionalLessonOfTeacher.get());
            model.addAttribute("newLesson", newLesson);
            model.addAttribute("warning", "Teacher is busy on that date");
            setAllModelDataForNewLesson(model, groupId);
            return "create-lesson-from-group";
        }

        if (optionalLessonOfGroup.isPresent()) {

//            result.rejectValue("startDate", "startDate", "This Group "+optionalLessonOfGroup.get().toString()+" is busy on " + optionalLessonOfGroup.get().getDateStart()
//                    + " for " + optionalLessonOfGroup.get().getDiscipline());
            model.addAttribute("duplicateLessonOfGroup", optionalLessonOfGroup.get());
            model.addAttribute("newLesson", newLesson);
            model.addAttribute("warning", "Group is busy on that date");
            setAllModelDataForNewLesson(model, groupId);
            return "create-lesson-from-group";
        }

        lessonService.create(newLesson);

        // because Locale.forLanguageTag("uk-UA") gives same result as US locale
        WeekFields weekFields = WeekFields.of(Locale.GERMANY);
        int currentWeekNumber = LocalDate.now().get(weekFields.weekOfWeekBasedYear());
        int newLessonWeekNumber = parsedDate.get(weekFields.weekOfWeekBasedYear());
        int plusWeekToGoTo = newLessonWeekNumber - currentWeekNumber;
        return "redirect:/groups/{groupId}/read/" + plusWeekToGoTo;
    }


    @GetMapping("/create/teacher-off/{teacherId}")
    public String createTeacherTimeOffAsLesson(@PathVariable("teacherId") long teacherId, Model model) {
        model.addAttribute("newLesson", new Lesson());
        model.addAttribute("classTypes", classTypeService.getAll());
        return "create-time-off";
    }


    @PostMapping("/create/teacher-off/{teacherId}")
    public String createTeacherTimeOffAsLesson(@Validated @ModelAttribute("newLesson") Lesson newLesson,
                                               @PathVariable("teacherId") long teacherId, BindingResult result, Model model) {
        newLesson.setGroup(null);
        newLesson.setDiscipline(null);
        LocalDateTime lessonStartDate = newLesson.getStartDateTime();
        LocalDateTime lessonEndDate = newLesson.getEndDateTime();
        newLesson.setLessonDataFromStartDateTime(lessonStartDate);
        newLesson.setTeacher(teacherService.readById(teacherId));

        List<LessonDto> lessonsInSameDayByTeacher = lessonService.getLessonByTeacherIdAndStartDate(teacherId, lessonStartDate.toLocalDate());
        DateTimeService dateTimeService = new DateTimeService();

        Optional<LessonDto> optionalLessonOfTeacher = dateTimeService.checkIfLessonClashesWithAnother(newLesson, lessonsInSameDayByTeacher);
        logger.info("optionalLessonOfTeacher = " + (optionalLessonOfTeacher.isPresent() ? optionalLessonOfTeacher.get() : " empty"));

        if (optionalLessonOfTeacher.isPresent()) {
            result.rejectValue("startDateTime", "lesson", "Teacher " + optionalLessonOfTeacher.get().getTeacher() + " is busy on " + optionalLessonOfTeacher.get().getDateStart()
                    + " for " + optionalLessonOfTeacher.get().getDiscipline());
            model.addAttribute("duplicateLessonOfTeacher", optionalLessonOfTeacher.get());
        }

        if (result.hasErrors()) {
            logger.warning("lesson dada has result errors " + result.getAllErrors());
            model.addAttribute("newLesson", newLesson);
            model.addAttribute("classTypes", classTypeService.getAll());
            return "create-time-off";
        }

        System.out.println("before create lesson for teacher");
        lessonService.create(newLesson);

        return "redirect:/teachers/{teacherId}/open";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") long id, @RequestParam("urlFrom") String url) {

        UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
        String path = builder.buildAndExpand().getPath();

        System.out.println("from delete of lessonController");
        try {
            lessonService.delete(id);
        } catch (RuntimeException e) {
            logger.warning(e.getMessage());
        }
        return "redirect:" + url;
    }


    @GetMapping(value = {"/{id}/group/{groupId}/edit", "/{id}/group/{groupId}/update"})
    public String edit(@PathVariable long id, @PathVariable long groupId, Model model) {
        Lesson lesson = lessonService.readById(id);
        LessonDto lessonDto = LessonDtoConverter.convertToDto(lesson);
        model.addAttribute("classTypes", classTypeService.getAll());
        model.addAttribute("timeFrames", timeFrameService.getAll());
//        model.addAttribute("teachers", teacherService.getAllAsDto());
//        model.addAttribute("disciplines", disciplineService.getAllAsDto());

//        model.addAttribute("disciplines", disciplineService.getAllWithTeachersByGroup(groupId).stream().map(DisciplineDtoTransformer::convertToDto));
//        model.addAttribute("teachers", teacherService.getAllAsDto());
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
        int count = 0;
        for (LessonDto lsn : lessonDtos) {
            count++;
            String key = lsn.getTeacher() + " - " + lsn.getStartDateTime();
            String groupKey = lsn.getGroupNumber() + " - " + lsn.getStartDateTime();
            if (!lessonsMappedToTeachers.containsKey(key)) {
                List<LessonDto> value = new ArrayList<>();
                value.add(lsn);
                lessonsMappedToTeachers.put(key, value);
            }else {
                lessonsMappedToTeachers.get(key).add(lsn);
                lessonsThatClashTeachers.addAll(lessonsMappedToTeachers.get(key));
            }

            if (!lessonsMappedToGroups.containsKey(groupKey)) {
                List<LessonDto> groupValue = new ArrayList<>();
                groupValue.add(lsn);
                lessonsMappedToGroups.put(groupKey, groupValue);
            } else {
                lessonsMappedToGroups.get(groupKey).add(lsn);
                lessonsThatClashGroups.addAll(lessonsMappedToGroups.get(groupKey));
            }

        }
        System.out.println("checked " + count + " lessons, found " + lessonsThatClashTeachers.size() + " duplicates");
        if (errors.length() > 0) {
            rat.addFlashAttribute("warning", errors);
        }
        rat.addFlashAttribute("lessonsDto", lessonsThatClashTeachers.stream().sorted(Comparator.comparing(LessonDto::getStartDateTime)).collect(Collectors.toCollection(LinkedHashSet::new)));
        rat.addFlashAttribute("lessonsDtoGroup", lessonsThatClashGroups.stream().sorted(Comparator.comparing(LessonDto::getStartDateTime)).collect(Collectors.toCollection(LinkedHashSet::new)));

        return "redirect:/";
    }


}
