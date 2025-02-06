package com.mkkabi.dev.service.impl;

import com.mkkabi.dev.exception.NullEntityReferenceException;
import com.mkkabi.dev.model.TimeFrame;
import com.mkkabi.dev.repository.TimeFrameRepository;
import com.mkkabi.dev.tools.AppLogger;
import com.mkkabi.dev.service.TimeFrameService;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TimeFrameServiceImpl implements TimeFrameService {
    private final Logger logger = new AppLogger("TimeFrameService.class");
    private final TimeFrameRepository repository;

    public TimeFrameServiceImpl(TimeFrameRepository repository) {
        this.repository = repository;
    }

    @Override
    public TimeFrame create(TimeFrame timeFrame) {
        try {
            TimeFrame savedTimeFrame = repository.save(timeFrame);
            logger.info("saved TimeFrame "+savedTimeFrame.getStartTime());
            return savedTimeFrame;
        } catch (IllegalArgumentException e) {
            logger.warning("TimeFrame was null, no savedTimeFrame created "+e.getMessage());
            throw new NullEntityReferenceException("savedTimeFrame was not created, try again");
        }
    }

    @Override
    public TimeFrame update(TimeFrame timeFrame) {
        if (timeFrame != null) {
            logger.info("updating valid timeFrame "+timeFrame.getOrderNumber());
            TimeFrame oldTimeFrame = getTimeFrameByOrderNumber(timeFrame.getOrderNumber());
            if (oldTimeFrame != null) {

                logger.info("updating oldTimeFrame id = "+oldTimeFrame.getOrderNumber()+" starts at = "+oldTimeFrame.getStartTime());
                return repository.save(timeFrame);
            }
        }
        logger.warning("timeFrame was not found in DB while updating, timeFrame does not exist");
        throw new NullEntityReferenceException("Something went wrong, try again");
    }

    @Override
    public void deleteById(long id) {
        TimeFrame timeFrame = getTimeFrameById(id);
        if (timeFrame != null) {
            repository.delete(timeFrame);
        } else {
            throw new EntityNotFoundException("Could not find timeFrame with id "+id);
        }
    }

    @Override
    public List<TimeFrame> getAll() {
        List<TimeFrame> timeFrames = repository.findAll();
        logger.info("found "+timeFrames.size()+" timeFrames in DB");
        return timeFrames.isEmpty() ? new ArrayList<>() : timeFrames;
    }

    @Override
    public TimeFrame getTimeFrameById(long id) {
        Optional<TimeFrame> optional = repository.findById(id);
        if (optional.isPresent()) {
            logger.info("reading TimeFrame by orderNumber, found TimeFrame "+optional.get().getStartTime());
            return optional.get();
        }
        logger.warning("could not find TimeFrame with the specified id "+id);
        throw new EntityNotFoundException("Could not find TimeFrame with id "+id);
    }

    @Override
    public TimeFrame getTimeFrameByOrderNumber(int number) {
        Optional<TimeFrame> optional = repository.getTimeFrameByOrderNumber(number);
        if (optional.isPresent()) {
            logger.info("reading TimeFrame by orderNumber, found TimeFrame "+optional.get().getStartTime());
            return optional.get();
        }
        logger.warning("could not find TimeFrame with the specified number "+number);
        throw new EntityNotFoundException("Could not find TimeFrame with number "+number);
    }
}
