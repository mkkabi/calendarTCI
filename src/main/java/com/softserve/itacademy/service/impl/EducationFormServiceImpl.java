package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.DisciplineName;
import com.softserve.itacademy.model.EducationForm;
import com.softserve.itacademy.repository.DisciplineNameRepository;
import com.softserve.itacademy.repository.EducationFormRepository;
import com.softserve.itacademy.service.DisciplineNameService;
import com.softserve.itacademy.service.EducationFormService;
import java.util.logging.Logger;
import com.softserve.itacademy.tools.AppLogger;
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
