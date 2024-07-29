package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Settings;
import com.softserve.itacademy.model.DisciplineName;
import com.softserve.itacademy.repository.SettingsRepository;
import com.softserve.itacademy.repository.DisciplineNameRepository;
import com.softserve.itacademy.repository.SettingsRepository;
import com.softserve.itacademy.service.DisciplineNameService;
import java.util.logging.Logger;

import com.softserve.itacademy.tools.AppLogger;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
//        throw new EntityNotFoundException("Settings with ID 1 not found");
    }

//    public void updateColorForOnlineClasses(String color){
//        repository.updateColor(color);
//    }
}
