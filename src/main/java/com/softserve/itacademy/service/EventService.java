package com.softserve.itacademy.service;

import com.softserve.itacademy.model.Event;

import java.util.List;

public interface EventService {
    Event create(Event event);
    Event readById(long id);
    Event update(Event event);
    void delete(long id);
    List<Event> getAll();
//    List<Event> getAllByUserId(long id);
//    List<Event> getAttendeesEvents(long id);
    List<Event> getOwnerevents(long id);
    //    List<Event> getByDate(LocalDateTime date);
    List<Event> getByUserAndWeek(Long userId, int weekNumber);

    List<Event> getAllByUserId(long userId);
}
