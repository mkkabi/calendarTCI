package com.mkkabi.dev.service;

import com.mkkabi.dev.model.Event;

import java.util.List;

public interface EventService {
    Event create(Event event);
    Event readById(long id);
    Event update(Event event);
    void delete(long id);
    List<Event> getAll();
    List<Event> getOwnerevents(long id);
    List<Event> getByUserAndWeek(Long userId, int weekNumber);
    List<Event> getAllByUserId(long userId);
}
