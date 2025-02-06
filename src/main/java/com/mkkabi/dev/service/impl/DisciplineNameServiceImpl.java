package com.mkkabi.dev.service.impl;

import com.mkkabi.dev.exception.NullEntityReferenceException;
import com.mkkabi.dev.model.DisciplineName;
import com.mkkabi.dev.repository.DisciplineNameRepository;
import com.mkkabi.dev.tools.AppLogger;
import com.mkkabi.dev.service.DisciplineNameService;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DisciplineNameServiceImpl implements DisciplineNameService {

    private final Logger logger = new AppLogger("DisciplineNameImpl.class");

    private final DisciplineNameRepository repository;


    public DisciplineNameServiceImpl(DisciplineNameRepository disciplineNameRepository) {
        this.repository = disciplineNameRepository;
    }

    @Override
    public DisciplineName create(DisciplineName disciplineName) {
        try{
            DisciplineName discName =  repository.save(disciplineName);
            logger.info("Created DisciplineName "+discName.getName());
            return discName;
        }catch (IllegalArgumentException e){
            logger.warning("Discipline Name was null, no user created");
            throw new NullEntityReferenceException("Discipline Name  was not created "+e.getMessage());
        }
    }

    @Override
    public DisciplineName readByName(String name) {
        Optional<DisciplineName> optional = repository.findDisciplineNameByName(name);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new EntityNotFoundException("Discipline Name with name " + name + " not found");
    }

    @Override
    public DisciplineName readById(long id) {
        Optional<DisciplineName> optional = repository.findById(id);
        if (optional.isPresent()) {
            logger.info("reading user, found user "+optional.get().getName());
            return optional.get();
        }
        logger.warning("could not find user with the specified ID "+id);
        throw new EntityNotFoundException("Could not find user with ID "+id);
    }

    @Override
    public DisciplineName update(DisciplineName disciplineName) {
        if (disciplineName != null) {
            DisciplineName oldTodo = readByName(disciplineName.getName());
            if (oldTodo != null) {
                return repository.save(disciplineName);
            }
        }
        throw new NullEntityReferenceException("Discipline Name cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        DisciplineName disciplineName = readById(id);
        if (disciplineName != null) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Discipline Name with Id " + id + " not found");
        }
    }

    @Override
    public List<DisciplineName> getAll() {
        List<DisciplineName> disciplineNames = repository.findAll();
        return disciplineNames.isEmpty() ? new ArrayList<>() : disciplineNames;
    }


}
