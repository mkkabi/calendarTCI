package com.softserve.itacademy.service;

import com.softserve.itacademy.model.Lesson;
import com.softserve.itacademy.model.TimeFrame;

import java.util.List;
import java.util.Optional;

public interface TimeFrameService {
    TimeFrame create(TimeFrame timeFrame);
//    TimeFrame readById(long id);
    TimeFrame update(TimeFrame timeFrame);
    void deleteByOrdinalNumber(int id);
    void deleteById(long id);
    List<TimeFrame> getAll();
    TimeFrame getTimeFrameByOrderNumber(int number);
    TimeFrame getTimeFrameById(long id);

}
