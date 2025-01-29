package com.mkkabi.dev.service.impl;

import com.mkkabi.dev.exception.NullEntityReferenceException;
import com.mkkabi.dev.model.EducationForm;
import com.mkkabi.dev.repository.EducationFormRepository;
import com.mkkabi.dev.service.EducationFormService;
import com.mkkabi.dev.tools.AppLogger;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EducationFormServiceImpl implements EducationFormService {
    private final Logger logger = new AppLogger(this.getClass().getSimpleName());

    private final EducationFormRepository repository;

    public EducationFormServiceImpl(EducationFormRepository repository) {
        this.repository = repository;
    }

    @Override
    public EducationForm create(EducationForm educationForm) {
        try{
            EducationForm eduForm =  repository.save(educationForm);
            logger.info("Created EducationForm "+educationForm.getTitle());
            return eduForm;
        }catch (IllegalArgumentException e){
            logger.warning("EducationForm was null, no user created");
            throw new NullEntityReferenceException("EducationForm was not created "+e.getMessage());
        }
    }

    @Override
    public EducationForm readByName(String name) {
        Optional<EducationForm> optional = repository.findByTitle(name);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new EntityNotFoundException("EducationForm with name " + name + " not found");
    }

    @Override
    public EducationForm readById(Long id) {
        Optional<EducationForm> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new EntityNotFoundException("EducationForm with name " + id + " not found");
    }

    @Override
    public EducationForm update(EducationForm educationForm) {
        if (educationForm != null) {
            EducationForm oldTodo = readByName(educationForm.getTitle());
            if (oldTodo != null) {
                return repository.save(educationForm);
            }
        }
        throw new NullEntityReferenceException("EducationForm cannot be 'null'");
    }

    @Override
    public void delete(long name) {
        EducationForm educationForm = readById(name);
        if (educationForm != null) {
            repository.delete(educationForm);
        } else {
            throw new EntityNotFoundException("EducationForm with name " + name + " not found");
        }
    }

    @Override
    public List<EducationForm> getAll() {
        List<EducationForm> educationForms = repository.findAll();
        return educationForms.isEmpty() ? new ArrayList<>() : educationForms;
    }

}
