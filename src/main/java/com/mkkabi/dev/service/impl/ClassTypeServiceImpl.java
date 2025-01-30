package com.mkkabi.dev.service.impl;

import com.mkkabi.dev.exception.NullEntityReferenceException;
import com.mkkabi.dev.model.ClassType;
import com.mkkabi.dev.repository.ClassTypeRepository;
import com.mkkabi.dev.service.ClassTypeService;
import com.mkkabi.dev.tools.AppLogger;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClassTypeServiceImpl implements ClassTypeService {
    private final Logger logger = new AppLogger("ClassTypeServiceImpl.class");

    private final ClassTypeRepository repository;

    public ClassTypeServiceImpl(ClassTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClassType create(ClassType classType) {
        try {
            ClassType savedClassType = repository.save(classType);
            logger.info("saved classType "+savedClassType.getId());
            return savedClassType;
        } catch (IllegalArgumentException e) {
            logger.warning("classType was null, no classType created");
            throw new NullEntityReferenceException("ClassType was not created, try again");
        }
    }

    @Override
    public ClassType readById(long id) {
        Optional<ClassType> optional = repository.findById(id);
        if (optional.isPresent()) {
            logger.info("reading classType, found classType "+optional.get().getType());
            return optional.get();
        }
        logger.warning("could not find classType with the specified ID "+id);
        throw new EntityNotFoundException("Could not find classType with ID "+id);
    }

    @Override
    public ClassType update(ClassType classType) {
        if (classType != null) {
            logger.info("updating valid classType "+classType.getId());
            ClassType oldClassType = readById(classType.getId());
            if (oldClassType != null) {

                logger.info("updating uer id = "+classType.getId()+" classType role = "+classType.getType());
                return repository.save(classType);
            }
        }
        logger.warning("classType was not found in DB while updating, classType does not exist");
        throw new NullEntityReferenceException("Something went wrong, try again");
    }

    @Override
    public void delete(long id) {
        ClassType classType = readById(id);
        if (classType != null) {
            repository.delete(classType);
        } else {
            throw new EntityNotFoundException("Could not find classType with ID "+id);
        }
    }

    @Override
    public List<ClassType> getAll() {
        List<ClassType> classTypes = repository.findAll();
        logger.info("found "+classTypes.size()+" classTypes in DB");
        return classTypes.isEmpty() ? new ArrayList<>() : classTypes;
    }
}
