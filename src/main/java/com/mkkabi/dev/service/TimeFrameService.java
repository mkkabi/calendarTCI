package com.mkkabi.dev.service;

import com.mkkabi.dev.model.TimeFrame;

import java.util.List;

public interface TimeFrameService {
    TimeFrame create(TimeFrame timeFrame);
    TimeFrame update(TimeFrame timeFrame);
    void deleteById(long id);
    List<TimeFrame> getAll();
    TimeFrame getTimeFrameByOrderNumber(int number);
    TimeFrame getTimeFrameById(long id);

}
