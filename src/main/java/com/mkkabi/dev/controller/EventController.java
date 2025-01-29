package com.mkkabi.dev.controller;

import com.mkkabi.dev.model.Event;
import com.mkkabi.dev.model.User;
import com.mkkabi.dev.service.UserService;
import com.mkkabi.dev.tools.AppLogger;
import com.mkkabi.dev.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final UserService userService;
    private final Logger logger = new AppLogger(this.getClass().getSimpleName());
    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping("/create/users/{owner_id}")
    public String create(@PathVariable("owner_id") long ownerId, Model model) {
        Event event = new Event();
        event.setStartTime(LocalDateTime.now());
        event.setEndTime(LocalDateTime.now());
        model.addAttribute("event", event);
        model.addAttribute("ownerId", ownerId);
        model.addAttribute("users", userService.getAll());
        return "create-event";
    }

    // ToDo change final redirect link to list of users events
    @PostMapping("/create/users/{owner_id}")
    public String create(@RequestParam("attendee_id") long attendeeId, @PathVariable("owner_id") long ownerId, Model model,
                         @Validated @ModelAttribute("event") Event event, BindingResult result) {
        LocalDateTime newEventStartTime = event.getStartTime();
        LocalDateTime newEventEndTime = event.getEndTime();
        List<Event> events = eventService.getAllByUserId(ownerId);


        Optional<Event> optionalEvent = events.stream()
                .filter(e -> e.getStartTime().isBefore(newEventEndTime) && newEventStartTime.isBefore(e.getEndTime()))
                .findFirst();

        if (optionalEvent.isPresent()) {
            result.rejectValue("startTime", "event", "This user is busy on " + optionalEvent.get().getDayOfWeek()
                    + " for " + optionalEvent.get().getTitle());
            model.addAttribute("duplicateEvent", optionalEvent.get());
        }
        if (event.getStartTime().isAfter(event.getEndTime())) {
            result.rejectValue("startTime", "event", "Start date cannot be after end date");
            result.rejectValue("endTime", "event", "Start date cannot be after end date");
        }
        if (result.hasErrors()) {
            logger.warning("event dada has result errors " + result.getAllErrors());
            model.addAttribute("ownerId", ownerId);
            return "create-event";
        }
        event.setOwner(userService.readById(ownerId));
        int week = event.getStartTime().get(WeekFields.of(Locale.getDefault()).weekOfYear());
        event.setWeek(week);
        event.setDayOfWeek(event.getStartTime().getDayOfWeek().toString());
        event.setDuration(event.getStartTime(), event.getEndTime());

        List<User> attendees = new ArrayList<>();
        attendees.add(userService.readById(attendeeId));
        event.setAttendees(attendees);
        event = eventService.create(event);

        logger.log(Level.INFO, "created event ID " + event.getId());
        return "redirect:/";
    }

    @GetMapping("/all/weekly/{user}/{week}")
    public String getAttendeesEventsByWeek(@PathVariable("week") String stringDate, @PathVariable("user") long ownerId, Model model) {
        int weekOfYear = weekNumberFromString(stringDate, "yyyy-MM-dd");

        logger.log(Level.INFO, "getting events for week " + weekOfYear);
        List<Event> eventList = eventService.getOwnerevents(ownerId);
        List<Event> weekly = eventList.stream().filter(e -> e.getWeek() == weekOfYear).collect(Collectors.toList());

        Map<String, List<Event>> eventMap = weekly.stream().collect(Collectors.groupingBy(e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return e.getStartTime().format(formatter);
        }));

        System.out.println("================================");
        for (String key : eventMap.keySet()) {
            System.out.println("events for key " + key);
            for (Event e : eventMap.get(key)) {
                System.out.println(e.getTitle() + " " + e.getStartTime());
            }
        }

        model.addAttribute("events", weekly);
        model.addAttribute("user", userService.readById(ownerId));
        return "events-weekly";
    }

    private static int weekNumberFromString(String stringDate, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date = LocalDate.parse(stringDate, formatter);
        return date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }

    @GetMapping("/delete/{eventId}")
    public String deleteEvent(@PathVariable("eventId")long eventId, HttpServletRequest request){
        logger.log(Level.INFO, "deleteEvent() context path = "+request.getContextPath());
        logger.log(Level.INFO, "deleteEvent() getRequestURL = "+request.getRequestURL().toString());
        logger.log(Level.INFO, "deleteEvent() getContextPath = "+request.getContextPath());
        logger.log(Level.INFO, "deleteEvent() getRequestURI = "+request.getRequestURI());
        logger.log(Level.INFO, "deleteEvent() getQueryString() = "+request.getQueryString());

        eventService.delete(eventId);

        return "redirect:/";
    }


}
