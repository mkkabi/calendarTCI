package com.mkkabi.dev.controller;

import com.mkkabi.dev.service.*;
import com.mkkabi.dev.tools.AppLogger;
import com.mkkabi.dev.service.ClassTypeService;
import com.mkkabi.dev.service.impl.SettingsServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

@Controller
public class HomeController {
    private final UserService userService;
    private final EducationFormService educationFormService;
    private final DisciplineNameService disciplineNameService;
    private final TimeFrameService timeFrameService;
    private final RoleService roleService;
    private final LessonService lessonService;
    private final EventService eventService;
    private final SettingsServiceImpl settingsService;
    private final TeacherService teacherService;
    private final DisciplineService disciplineService;
    private final GroupService groupService;
    private final ClassTypeService classTypeService;
    private final Logger logger = new AppLogger(this.getClass().getSimpleName());
    public HomeController(UserService userService, EducationFormService educationFormService, DisciplineNameService disciplineNameService, TimeFrameService timeFrameService, RoleService roleService, LessonService lessonService, EventService eventService, SettingsServiceImpl settingsService, TeacherService teacherService, DisciplineService disciplineService, GroupService groupService, ClassTypeService classTypeService) {
        this.userService = userService;
        this.educationFormService = educationFormService;
        this.disciplineNameService = disciplineNameService;
        this.timeFrameService = timeFrameService;
        this.roleService = roleService;
        this.lessonService = lessonService;
        this.eventService = eventService;
        this.settingsService = settingsService;
        this.teacherService = teacherService;
        this.disciplineService = disciplineService;
        this.groupService = groupService;
        this.classTypeService = classTypeService;
    }

    @GetMapping({"/", "home"})
    public String home(Model model) {
        model.addAttribute("users", userService.getAll());
        return "home";
    }

    @GetMapping({"/setup-data"})
    public String setup(Model model) {
        model.addAttribute("warning", "nothing was set");
        return "redirect:/";
    }


}
