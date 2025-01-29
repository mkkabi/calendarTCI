package com.mkkabi.dev.controller;

import com.mkkabi.dev.model.*;
import com.mkkabi.dev.service.*;
import com.mkkabi.dev.tools.AppLogger;
import com.mkkabi.dev.service.impl.SettingsServiceImpl;
import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    private final DisciplineNameService disciplineNameService;
    private final EducationFormService educationFormService;
    private final TimeFrameService timeFrameService;
    private final SettingsServiceImpl settingsService;
    private final ClassTypeService classTypeService;
    private final Logger logger = new AppLogger(this.getClass().getSimpleName());

    public SettingsController(DisciplineNameService disciplineNameService, EducationFormService educationFormService, TimeFrameService timeFrameService, SettingsServiceImpl settingsService, ClassTypeService classTypeService) {
        this.disciplineNameService = disciplineNameService;
        this.educationFormService = educationFormService;
        this.timeFrameService = timeFrameService;
        this.settingsService = settingsService;
        this.classTypeService = classTypeService;
    }

    @PostMapping("/discipline_name/add")
    public String create(@Validated @ModelAttribute("newDisciplineName") DisciplineName newDisciplineName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addItemsToModel(model);
            model.addAttribute("newEducationForm", new EducationForm());
            model.addAttribute("newTimeFrame", new TimeFrame());
            model.addAttribute("newSettings", new Settings());
            model.addAttribute("newClassType", new ClassType());
            return "settings";
        }
        disciplineNameService.create(newDisciplineName);
        return "redirect:/settings/all";
    }

    @PostMapping("/educationform/add")
    public String create(@Validated @ModelAttribute("newEducationForm") EducationForm newEducationForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addItemsToModel(model);
            model.addAttribute("newDisciplineName", new DisciplineName());
            model.addAttribute("newTimeFrame", new TimeFrame());
            model.addAttribute("newSettings", new Settings());
            model.addAttribute("newClassType", new ClassType());
            return "settings";
        }
        educationFormService.create(newEducationForm);
        return "redirect:/settings/all";
    }

    @PostMapping("/time_frame/add")
    public String create(@Validated @ModelAttribute("newTimeFrame") TimeFrame newTimeFrame, BindingResult result, Model model) {
        if (newTimeFrame.getStartTime().isAfter(newTimeFrame.getEndTime())) {
            result.rejectValue("startTime", "timeFrame", "Start time cannot be after end time");
            result.rejectValue("endTime", "timeFrame", "End time cannot be before start time");
        }
        if (result.hasErrors()) {
            addItemsToModel(model);
            model.addAttribute("newDisciplineName", new DisciplineName());
            model.addAttribute("newEducationForm", new EducationForm());
            model.addAttribute("newSettings", new Settings());
            model.addAttribute("newClassType", new ClassType());
            return "settings";
        }
        timeFrameService.create(newTimeFrame);
        return "redirect:/settings/all";
    }

    @PostMapping("/general/set")
    public String create(@Validated @ModelAttribute("newSettings") Settings newSettings, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addItemsToModel(model);
            model.addAttribute("newDisciplineName", new DisciplineName());
            model.addAttribute("newEducationForm", new EducationForm());
            model.addAttribute("newTimeFrame", new TimeFrame());
            model.addAttribute("message", "Error submitting Beginning of Study Year");
            model.addAttribute("newClassType", new ClassType());
            return "settings";
        }
        try {
            settingsService.create(newSettings);
        } catch (Exception e) {
            model.addAttribute("message", "error in adding new Date for Study calendar: " + e.getMessage());
        }
        return "redirect:/settings/all";
    }

    @PostMapping("/classtype/add")
    public String create(@Validated @ModelAttribute("newClassType") ClassType newClassType, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addItemsToModel(model);
            model.addAttribute("newDisciplineName", new DisciplineName());
            model.addAttribute("newTimeFrame", new TimeFrame());
            model.addAttribute("newSettings", new Settings());
            model.addAttribute("newEducationForm", new EducationForm());
            return "settings";
        }
        classTypeService.create(newClassType);
        return "redirect:/settings/all";
    }

    @GetMapping(path = {"/all"})
    public String getAllDisciplineNames(Model model, @ModelAttribute("warning") String warning) {
        addItemsToModel(model);
        if(warning!=null && !warning.isEmpty())
            model.addAttribute("warning", warning);
        model.addAttribute("newDisciplineName", new DisciplineName());
        model.addAttribute("newEducationForm", new EducationForm());
        model.addAttribute("newTimeFrame", new TimeFrame());
        model.addAttribute("newSettings", new Settings());
        model.addAttribute("newClassType", new ClassType());

        return "settings";
    }

    TriConsumer<GenericService, Long, RedirectAttributes> deleteWithTry = (genericService, id, ratts) -> {
        try {
            genericService.delete(id);
            ratts.addFlashAttribute("message", "Item successfully removed");
        } catch (RuntimeException e) {
            logger.warning(e.getMessage());
            ratts.addFlashAttribute("warning", e.getMessage());
        }
    };


    @GetMapping("/disciplines/{id}/delete")
    public String deleteDisciplineName(@PathVariable("id") long id) {
        disciplineNameService.delete(id);
        return "redirect:/settings/all";
    }

    @GetMapping("/educationform/{id}/delete")
    public String deleteEducationForm(@PathVariable("id") long id, RedirectAttributes ratts) {
        deleteWithTry.accept(educationFormService, id, ratts);
        return "redirect:/settings/all";
    }

    @GetMapping("/timeframe/{id}/delete")
    public String deleteTimeFrame(@PathVariable("id") long id) {
        timeFrameService.deleteById(id);
        return "redirect:/settings/all";
    }

    @GetMapping("/classtype/{id}/delete")
    public String deleteClassType(@PathVariable("id") int id) {
        classTypeService.delete(id);
        return "redirect:/settings/all";
    }

    private void addItemsToModel(Model model) {
        model.addAttribute("disciplineNames", disciplineNameService.getAll());
        model.addAttribute("educationForms", educationFormService.getAll());
        model.addAttribute("timeFrames", timeFrameService.getAll());
        model.addAttribute("classTypes", classTypeService.getAll());

        Settings settings = settingsService.findLatest();
        model.addAttribute("settings", settings);

        if (settings != null) {
            LocalDate localDate = settings.getStudyYearStartDate();
            int daysInMonth = localDate.lengthOfMonth();
            int year = localDate.getYear();

            LocalDate startOfMonth = LocalDate.of(year, localDate.getMonthValue(), 1);
            DayOfWeek dayOfWeekOfFirstOfMonth = startOfMonth.getDayOfWeek();

            Map<Integer, String> weekMap = new HashMap<>(31);


            // filling initial spaces in the weekMap
            for (int i = 1; i < dayOfWeekOfFirstOfMonth.getValue(); i++) {
                if (i == dayOfWeekOfFirstOfMonth.getValue() - 1)
                    weekMap.put(i * -1, "START");
                else
                    weekMap.put(i * -1, "SPACE");
            }
            for (int i = 1; i <= daysInMonth; i++) {
                String day = localDate.withDayOfMonth(i).getDayOfWeek().toString();
                weekMap.put(i, day);
            }

            String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
            List<String> daysOfWeek = Arrays.asList(days);

            model.addAttribute("daysOfWeekList", daysOfWeek);
            model.addAttribute("weekMap", weekMap);
        }
    }
}
