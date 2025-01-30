package com.mkkabi.dev.controller;

import com.mkkabi.dev.dto.*;
import com.mkkabi.dev.model.Teacher;
import com.mkkabi.dev.service.impl.*;
import com.mkkabi.dev.tools.AppLogger;
import com.mkkabi.dev.tools.DateTimeService;
import com.mkkabi.dev.tools.DisciplineStatsUtil;
import com.mkkabi.dev.tools.Settings;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {
    private final Logger logger = new AppLogger(this.getClass().getSimpleName());
    private final DisciplineServiceImpl disciplineService;
    private final LessonServiceImpl lessonService;
    private final TeacherServiceImpl teacherService;
    private final SettingsServiceImpl settingsService;
    private final RoleServiceImpl roleService;


    @GetMapping("/create")
    public String create(Model model) {
        setModelDataForCreateEdit(model);
        model.addAttribute("newTeacher", new Teacher());
        return "create-teacher";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("newTeacher") Teacher teacher, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create-teacher";
        }
        teacherService.create(teacher);
        return "redirect:/teachers/all";
    }

    @GetMapping("/all")
    public String getAll(Model model,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         @RequestParam(defaultValue = "id") String sortBy,
                         @RequestParam(defaultValue = "desc") String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TeacherDto> teachersDto = teacherService.getPaginatedTeachersActiveOnly(pageable);

        model.addAttribute("teachersDto", teachersDto);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("pages", true);
        model.addAttribute("sortDirection", sortDirection);

        if (teachersDto == null || teachersDto.isEmpty())
            model.addAttribute("message", "No teachers found, add new");
        return "teachers-list";
    }

    @GetMapping("/{id}/hide")
    public String delete(@PathVariable("id") long id) {
        Teacher teacher = teacherService.readById(id);
        teacher.setHidden(true);
        teacherService.create(teacher);
        return "redirect:/teachers/all";
    }

    @GetMapping(value = {"/{id}/open", "/{id}/read/{plusWeek}"})
    public String read(@PathVariable long id, Model model, @PathVariable Optional<Integer> plusWeek) {
        // START generating data for the schedule
        DateTimeService dateTimeService = new DateTimeService();
        Locale locale = Settings.locale;
        int weeksToAddOrSubtractToQuery = plusWeek.orElse(0);
        LocalDate localDateNowPlusWeeks = LocalDate.now().plusWeeks(weeksToAddOrSubtractToQuery);

        com.mkkabi.dev.model.Settings settings =  settingsService.findLatest();
        LocalDate startOfStudyYear = LocalDate.now();
        if(settings!=null)
            startOfStudyYear = settingsService.findLatest().getStudyYearStartDate();
        else{
            model.addAttribute("warning", "add Study date in settings first!");
        }

        // because Locale.forLanguageTag("uk-UA") gives same result as US locale
        WeekFields weekFields = WeekFields.of(Locale.GERMANY);
        int weekNumberOfLocalDateNowPlusWeeks = localDateNowPlusWeeks.get(weekFields.weekOfWeekBasedYear());
        int monthNumberOfLocalDateNowPlusWeeks = localDateNowPlusWeeks.getMonthValue();
        int yearNumberOfLocalDateNowPlusWeeks = localDateNowPlusWeeks.getYear();
        LocalDate firstDateOfQueryingWeek = localDateNowPlusWeeks.with(DayOfWeek.MONDAY);
        LocalDate seventhDayWeekName = localDateNowPlusWeeks.with(DayOfWeek.SATURDAY);

        long daysBetweenTwoDates = ChronoUnit.DAYS.between(startOfStudyYear, localDateNowPlusWeeks);
        int weekNumberOfStudyCalendar = (int) (Math.ceil(daysBetweenTwoDates / 7.0)+1 );

        List<LessonDto> weeklyLessons = lessonService.getLessonDtoByTeacherAndWeekNumberAndYear(id, weekNumberOfLocalDateNowPlusWeeks, yearNumberOfLocalDateNowPlusWeeks);
        List<LessonDto> monthlyLessons = lessonService.getLessonByTeacherAndMonthAndYear(id, monthNumberOfLocalDateNowPlusWeeks, yearNumberOfLocalDateNowPlusWeeks);

        logger.info("found number of lessons for this week = " + weeklyLessons.size());

        List<String> timeFrames = !weeklyLessons.isEmpty() ? weeklyLessons.stream()
                .map(l->l.getStartDateTime().format(dateTimeService.timeFormatter)+" - "+l.getEndDateTime().format(dateTimeService.timeFormatter))
                .distinct().sorted()
                .collect(Collectors.toList()):Arrays.asList(" ", "  ", " ");


        // an array of days of week in current locate from Mon-Sat
        String[] daysOfWeekArr = dateTimeService.getWeekDays(2, 7);
        Map<String, Map<String, LessonDto>> lessonsByOrderAndDayOfWeek =
                dateTimeService.createAndInitializeMapOfLessonsByOrderAndDayOfWeek(timeFrames, daysOfWeekArr,  weeklyLessons, locale);
        logger.info("set of time form lessonsByOrderAndDayOfWeek.keySet() " + lessonsByOrderAndDayOfWeek.keySet());


        TeacherDto teacherDto = teacherService.readByIdAsDto(id);

        // END total number of hours for lessons and disciplines stats
        Map<Long, DisciplineStatsUtil> mapOfDiscIdsToDisciplineUtil = weeklyLessons.stream()
                .collect(Collectors.toMap(LessonDto::getDisciplineId,
                DisciplineStatsUtil::new,
                DisciplineStatsUtil::merge
        ));

        Map<Long, DisciplineStatsUtil> mapOfMonthlyDiscIdsToDisciplineUtil = monthlyLessons.stream()
                .collect(Collectors.toMap(LessonDto::getDisciplineId,
                        DisciplineStatsUtil::new,
                        DisciplineStatsUtil::merge
                ));
        // END total number of hours for lessons and disciplines stats

        model.addAttribute("weeksToAddOrSubtractToQuery", weeksToAddOrSubtractToQuery);
        model.addAttribute("mapOfDiscIdsToDisciplineUtil", mapOfDiscIdsToDisciplineUtil);
        model.addAttribute("mapOfMonthlyDiscIdsToDisciplineUtil", mapOfMonthlyDiscIdsToDisciplineUtil);
        model.addAttribute("teacher", teacherDto);
        model.addAttribute("monthOfTheWeekOfQuery", localDateNowPlusWeeks.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Settings.locale).toUpperCase());
        model.addAttribute("weekNumberOfStudyCalendar", weekNumberOfStudyCalendar);
        model.addAttribute("daysOfQueryWeekWithDates", dateTimeService.getDaysOfWeekWithDatesInLocale(localDateNowPlusWeeks, 1, 6, locale));
        model.addAttribute("daysOfWeekMap", dateTimeService.getDaysOfWeekInLocaleMappedToNumberOfTheirOrder(localDateNowPlusWeeks, 1, 6, locale));
        model.addAttribute("lessonsMap", lessonsByOrderAndDayOfWeek);
        model.addAttribute("daysOfWeekArr", daysOfWeekArr);
        model.addAttribute("firstDayWeekName", dateTimeService.getDayNameInLocale(firstDateOfQueryingWeek, locale));
        model.addAttribute("seventhDayWeekName", dateTimeService.getDayNameInLocale(seventhDayWeekName, locale));
        model.addAttribute("timeFrames", timeFrames);
        model.addAttribute("onlineClassColor", settings!=null?settingsService.findLatest().getColorForOnlineClasses():"#0f0");



        return "teacher-page";
    }

    @GetMapping(value = {"/{id}/edit", "/{id}/update"})
    public String edit(@PathVariable long id, Model model){
        setModelDataForCreateEdit(model);
        Teacher teacher = teacherService.readById(id);
        model.addAttribute("updateTeacher", teacher);

        return "update-teacher";
    }

    @PostMapping(value = {"/{id}/edit", "/{id}/update"})
    public String update(@PathVariable long id, @Validated @ModelAttribute("updateTeacher") Teacher teacher, BindingResult result, Model model) {
        if (result.hasErrors()) {
            setModelDataForCreateEdit(model);
            return "update-teacher";
        }
        teacher.setRole(roleService.readById(2));
        teacherService.update(teacher);
        return "redirect:/teachers/{id}/open";
    }



    private void setModelDataForCreateEdit(Model model){
        model.addAttribute("disciplines", disciplineService.getAllAsDto()
                .stream().sorted(Comparator.comparing(DisciplineDTO::getName)).collect(Collectors.toList()));
    }

}
