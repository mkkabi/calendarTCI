package com.mkkabi.dev.controller;

import com.mkkabi.dev.dto.GroupDto;
import com.mkkabi.dev.dto.LessonDto;
import com.mkkabi.dev.model.Group;
import com.mkkabi.dev.model.Lesson;
import com.mkkabi.dev.model.TimeFrame;
import com.mkkabi.dev.service.*;
import com.mkkabi.dev.service.impl.LessonServiceImpl;
import com.mkkabi.dev.tools.AppLogger;
import com.mkkabi.dev.tools.DateTimeService;
import com.mkkabi.dev.tools.Settings;
import com.mkkabi.dev.exception.DuplicateEventException;
import com.mkkabi.dev.service.impl.SettingsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/groups")
public class GroupController {
    private final Logger logger = new AppLogger(this.getClass().getSimpleName());
    private final EducationFormService educationFormService;
    private final GroupService groupService;
    private final LessonService lessonService;
    private final SettingsServiceImpl settingsService;
    private final TimeFrameService timeFrameService;


    @GetMapping("/all")
    public String getAll(Model model) {
        Set<Group> groups = groupService.getAll();
        model.addAttribute("groups", groups);
        if(groups==null || groups.isEmpty())
            model.addAttribute("message", "No groups found, add new");
        return "group-list";
    }

    @GetMapping("/create")
    public String create(Model model){

        model.addAttribute("newGroup", new Group());
        setModelDataForCreateEdit(model);
        return "create-group";
    }

    @PostMapping("/create")
    public String create(@RequestParam("educationForm_id")long educationFormId,
                         @Validated @ModelAttribute("newGroup") Group group, BindingResult result, Model model){

        if (result.hasErrors()) {
            return "create-group";
        }
        group.setEducationForm(educationFormService.readById(educationFormId));
        groupService.create(group);
        return "redirect:/groups/all";
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") long id, Model model) {
        try{
            groupService.delete(id);
            return "redirect:/groups/all";
        }catch (DataIntegrityViolationException dev){
            logger.log(Level.WARNING, "error deleting Group = "+dev.getMessage());
            model.addAttribute("message", "This group is already assigned to someone "+dev.getMessage());
        }
        catch (Exception e){
            logger.log(Level.WARNING, "error deleting Group = "+e.getMessage()+" Class = "+e.getClass());
            model.addAttribute("message", e.getMessage()+" Class = "+e.getClass());
        }finally {
            Set<Group> groups = groupService.getAll();
            model.addAttribute("groups", groups);
            if(groups==null || groups.isEmpty())
                model.addAttribute("message", "No groups found, add new");
        }
        return "group-list";
    }

    @GetMapping(value = {"/{id}/open", "/{id}/read/{plusWeek}", "/{id}/simple/{plusWeek}"})
    public String read(@PathVariable long id, Model model, @PathVariable Optional<Integer> plusWeek) {

        UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
        String path = builder.buildAndExpand().getPath();

        DateTimeService dateTimeService = new DateTimeService();
        Locale locale = Settings.locale;
        int weeksToAddOrSubtractToQuery = plusWeek.orElse(0);
        LocalDate localDateNowPlusWeeks = LocalDate.now().plusWeeks(weeksToAddOrSubtractToQuery);
        LocalDate startOfStudyYear = settingsService.findLatest().getStudyYearStartDate();

        // because Locale.forLanguageTag("uk-UA") gives same result as US locale
        WeekFields weekFields = WeekFields.of(Locale.GERMANY);
        int weekNumberOfLocalDateNowPlusWeeks = localDateNowPlusWeeks.get(weekFields.weekOfWeekBasedYear());
        int yearNumberOfLocalDateNowPlusWeeks = localDateNowPlusWeeks.getYear();

        List<LocalDate> weekDatesList = dateTimeService.getLocalDatesOfWeek(localDateNowPlusWeeks, 1, 6);

        long daysBetweenTwoDates = ChronoUnit.DAYS.between(startOfStudyYear, localDateNowPlusWeeks);
        int weekNumberOfStudyCalendar = (int) (Math.ceil(daysBetweenTwoDates / 7.0)+1);

        GroupDto groupDto = groupService.readByIdAsDto(id);

        List<LessonDto> weeklyLessons = lessonService.getLessonByGroupAndWeekNumberAndYear(id, weekNumberOfLocalDateNowPlusWeeks, yearNumberOfLocalDateNowPlusWeeks);
        logger.info("found number of lessons for this week = " + weeklyLessons.size());

        Map<String, Map<LocalDate, LessonDto>> lessonsByTimeFrameAndLocalDate = new LinkedHashMap<>();
        List<TimeFrame> timeFramesList = timeFrameService.getAll().stream().sorted(Comparator.comparing(TimeFrame::getStartTime)).collect(Collectors.toList());


        for (TimeFrame timeFrame : timeFramesList) {
            String timeFrameStr = timeFrame.getStartTime() + " - " + timeFrame.getEndTime();
            lessonsByTimeFrameAndLocalDate.put(timeFrameStr, new LinkedHashMap<>());
            for (LocalDate localDate : weekDatesList) {
                lessonsByTimeFrameAndLocalDate.get(timeFrameStr)
                        .put(localDate, null);
            }
        }

        for (LessonDto weeklyLesson : weeklyLessons) {
            String timeFrameString = weeklyLesson.getStartDateTime().toLocalTime() + " - " + weeklyLesson.getEndDateTime().toLocalTime();
            LocalDate lessonDate = weeklyLesson.getDateStart();
            if (lessonsByTimeFrameAndLocalDate.get(timeFrameString) != null)
                lessonsByTimeFrameAndLocalDate.get(timeFrameString).put(lessonDate, weeklyLesson);
        }
        model.addAttribute("weeksToAddOrSubtractToQuery", weeksToAddOrSubtractToQuery);
        model.addAttribute("group", groupDto);
        model.addAttribute("monthOfTheWeekOfQuery", localDateNowPlusWeeks.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Settings.locale).toUpperCase());
        model.addAttribute("weekNumberOfStudyCalendar", weekNumberOfStudyCalendar);
        model.addAttribute("daysOfQueryWeekWithDates", dateTimeService.getDaysOfWeekWithDatesInLocale(localDateNowPlusWeeks, 1, 6, locale));
        model.addAttribute("onlineClassColor", settingsService.findLatest().getColorForOnlineClasses());
        model.addAttribute("path", path);
        model.addAttribute("timeFramesList", timeFramesList);
        model.addAttribute("lessonsByTimeFrameAndLocalDate", lessonsByTimeFrameAndLocalDate);
        model.addAttribute("weekDatesList", weekDatesList);
        model.addAttribute("weekNumberOfLocalDateNowPlusWeeks", weekNumberOfLocalDateNowPlusWeeks);
        model.addAttribute("year", yearNumberOfLocalDateNowPlusWeeks);


        if(path.contains("simple")){
            return "simple_schedule";
        }
        return "group-page";
    }

    @GetMapping(value = {"/{id}/duplicate/{plusWeek}/{calendarWeek}/{year}"})
    public String duplicateWeek(@PathVariable long id, @PathVariable int plusWeek, @PathVariable int calendarWeek, @PathVariable int year, RedirectAttributes rat){
        try {
            lessonService.duplicateWeek(id, calendarWeek, year);
        }catch (DuplicateEventException e){
            rat.addFlashAttribute("warning", e.getMessage());
        }
        return "redirect:/groups/{id}/read/{plusWeek}";
    }

    @GetMapping(value = {"/{id}/edit", "/{id}/update"})
    public String edit(@PathVariable long id, Model model){
        setModelDataForCreateEdit(model);
        Group updateGroup = groupService.readById(id);
        model.addAttribute("updateGroup", updateGroup);
        return "edit-group";
    }

    @PostMapping(value = {"/{id}/edit", "/{id}/update"})
    public String update(@PathVariable long id, @Validated @ModelAttribute("updateGroup") Group updateGroup, BindingResult result, Model model) {
        if (result.hasErrors()) {
            setModelDataForCreateEdit(model);
            model.addAttribute("updateGroup", updateGroup);
            return "edit-group";
        }
        groupService.update(updateGroup);
        return "redirect:/groups/{id}/open";
    }

    private void setModelDataForCreateEdit(Model model) {
        model.addAttribute("educationForms", educationFormService.getAll());
    }

}
