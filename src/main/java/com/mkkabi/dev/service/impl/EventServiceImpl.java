package com.mkkabi.dev.service.impl;

import com.mkkabi.dev.exception.NullEntityReferenceException;
import com.mkkabi.dev.model.Event;
import com.mkkabi.dev.repository.EventRepository;
import com.mkkabi.dev.service.EventService;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository repository;

    public EventServiceImpl(EventRepository repository) {
        this.repository = repository;
    }

    @Override
    public Event create(Event event) {
        try {
            return repository.save(event);
        } catch (IllegalArgumentException e) {
            throw new NullEntityReferenceException("Event cannot be 'null'");
        }
    }

    @Override
    public Event readById(long id) {
        Optional<Event> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new EntityNotFoundException("Event with id " + id + " not found");
    }

    @Override
    public Event update(Event event) {
        if (event != null) {
            Event oldTodo = readById(event.getId());
            if (oldTodo != null) {
                return repository.save(event);
            }
        }
        throw new NullEntityReferenceException("Event cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        Event event = readById(id);
        if (event != null) {
            repository.delete(event);
        } else {
            throw new EntityNotFoundException("Event with id " + id + " not found");
        }
    }

    @Override
    public List<Event> getAll() {
        List<Event> event = repository.findAll();
        return event.isEmpty() ? new ArrayList<>() : event;
    }


    @Override
    public List<Event> getOwnerevents(long id) {
        List<Event> events = repository.getOwnerevents(id);
        return events.isEmpty() ? new ArrayList<>() : events;
    }

    @Override
    public List<Event> getByUserAndWeek(Long userId, int weekNumber){
        return repository.getByUserAndWeek(userId, weekNumber);
    }

    @Override
    public List<Event> getAllByUserId(long userId){
        return repository.getAllByUserId(userId);
    }

}
