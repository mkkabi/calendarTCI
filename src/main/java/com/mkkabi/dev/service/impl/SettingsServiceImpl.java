package com.mkkabi.dev.service.impl;

import com.mkkabi.dev.exception.NullEntityReferenceException;
import com.mkkabi.dev.model.Settings;
import com.mkkabi.dev.repository.SettingsRepository;
import com.mkkabi.dev.tools.AppLogger;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingsServiceImpl {
    private final Logger logger = new AppLogger("SettingsServiceImpl.class");
    private final SettingsRepository repository;

    public SettingsServiceImpl(SettingsRepository repository) {
        this.repository = repository;
    }

    public Settings create(Settings date) {
        try{
            date.setId(1);
            Settings savedDate =  repository.save(date);
            logger.info("Created Settings "+savedDate.getStudyYearStartDate());
            return savedDate;
        }catch (IllegalArgumentException e){
            logger.warning("Settings was null, no date created");
            throw new NullEntityReferenceException("Settings  was not created "+e.getMessage());
        }
    }

    public Settings findLatest(){
        Optional<Settings> optional = repository.findById(1L);
        return optional.orElse(null);
    }

}
