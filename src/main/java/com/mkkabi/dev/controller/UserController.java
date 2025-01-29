package com.mkkabi.dev.controller;

import com.mkkabi.dev.model.Event;
import com.mkkabi.dev.model.User;
import com.mkkabi.dev.service.RoleService;
import com.mkkabi.dev.service.UserService;
import com.mkkabi.dev.tools.DateTimeService;
import com.mkkabi.dev.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final EventService eventService;
    private final Logger logger = Logger.getLogger("UserController.class");

    public UserController(UserService userService, RoleService roleService, EventService eventService) {
        this.userService = userService;
        this.roleService = roleService;
        this.eventService = eventService;
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAll());
        return "create-user";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "create-user";
        }
        user.setRole(roleService.readById(3));
        User newUser = userService.create(user);
        return "redirect:/users/" + newUser.getId() + "/read/";
    }

    @GetMapping(value = {"/{id}/read", "/{id}/read/{weekNumber}"})
    public String read(@PathVariable long id, Model model, @PathVariable Optional<Integer> weekNumber) {
        int weekOfYear = weekNumber.orElseGet(() -> LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfYear()));

        User user = userService.readById(id);

        Set<Event> weekly = eventService.getByUserAndWeek(id, weekOfYear).stream()
                .sorted(Comparator.comparing(Event::getStartTime))
                .collect(Collectors.toCollection(TreeSet::new));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        TreeMap<String, List<Event>> eventMap = weekly.stream()
                .collect(Collectors.toMap(event -> event.getStartTime().format(formatter)+" - "+event.getEndTime().format(formatter), event -> {
            List<Event> el = new ArrayList<>();
            el.add(event);
            return el;
        }, (events, events2) -> {
            events.addAll(events2);
            return events;
        }, TreeMap::new));

        String[] weekdays = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};

        for (String s : eventMap.keySet()) {
            for (Event e : eventMap.get(s)) {
                logger.log(Level.INFO, "got event from eventMap = " + e.getTitle() + " " + e.getStartTime() + " " + e.getDayOfWeek());
            }
        }

        DateTimeService dateTimeService = new DateTimeService(LocalDate.of(2023, 9, 4));
        ValueRange range = dateTimeService.getCalendarStart().range(WeekFields.of(Locale.getDefault()).weekOfYear());

        model.addAttribute("events", weekly);
        model.addAttribute("eventMap", eventMap);
        model.addAttribute("weekdays", weekdays);
        model.addAttribute("weekOfYear", weekOfYear);
        model.addAttribute("studyWeek", dateTimeService.getStudyWeekFromCalendarWeek(weekNumber.orElse(weekOfYear)));
        model.addAttribute("user", user);

        int weekOfSeptFirst = LocalDate.of(2023,9,04).get(WeekFields.of(Locale.getDefault()).weekOfYear());
        int weekOfDecLast = LocalDate.of(2024,06,30).get(WeekFields.of(Locale.getDefault()).weekOfYear());

        model.addAttribute("message", "calendarStart = " + dateTimeService.getCalendarStart() +
                " actualWeekOfCalendarStartDate = "+dateTimeService.getActualWeekOfCalendarStartDate()+
                " actualCurrentWeek = "+dateTimeService.getActualCurrentWeek()+
                " numberOfWeeksInYear = "+dateTimeService.getNumberOfWeeksInYear()+
                " weeksPassedFromCalendarStart = "+dateTimeService.getWeeksPassedFromCalendarStart()+
                " calendarCurrentWeek = "+dateTimeService.getCalendarCurrentWeek()
        );

        return "user-info";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable long id, Model model) {
        User user = userService.readById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAll());
        return "update-user";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable long id, Model model, @Validated @ModelAttribute("user") User user, @RequestParam("roleId") long roleId, BindingResult result) {
        User oldUser = userService.readById(id);
        if (result.hasErrors()) {
            user.setRole(oldUser.getRole());
            model.addAttribute("roles", roleService.getAll());
            return "update-user";
        }
        if (oldUser.getRole().getName().equals("USER")) {
            user.setRole(oldUser.getRole());
        } else {
            user.setRole(roleService.readById(roleId));
        }
        userService.update(user);
        return "redirect:/users/" + id + "/read";
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/users/all";
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users-list";
    }
}
