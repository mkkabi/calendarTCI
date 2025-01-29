package com.mkkabi.dev.service.impl;

import com.mkkabi.dev.dto.DisciplineDTO;
import com.mkkabi.dev.dto.DisciplineDtoTransformer;
import com.mkkabi.dev.exception.NullEntityReferenceException;
import com.mkkabi.dev.model.Discipline;
import com.mkkabi.dev.repository.DisciplineRepository;
import com.mkkabi.dev.repository.GroupRepository;
import com.mkkabi.dev.tools.AppLogger;
import com.mkkabi.dev.service.DisciplineService;

import java.util.*;
import java.util.logging.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
public class DisciplineServiceImpl implements DisciplineService {
    private final Logger logger = new AppLogger("DisciplineServiceImpl.class");

    private final DisciplineRepository repository;
    private final GroupRepository groupRepository;

    @PersistenceContext
    EntityManager entityManager;

    public DisciplineServiceImpl(DisciplineRepository repository, GroupRepository groupRepository) {
        this.repository = repository;
        this.groupRepository = groupRepository;
    }

    @Override
    public Discipline create(Discipline discipline) {
        try {
            Discipline savedDiscipline = repository.save(discipline);
            logger.info("saved discipline " + savedDiscipline.getId());
            return savedDiscipline;
        } catch (IllegalArgumentException e) {
            logger.warning("discipline was null, no discipline created");
            throw new NullEntityReferenceException("Discipline was not created, try again");
        }
    }

    @Override
    public Discipline readById(long id) {
        Optional<Discipline> optional = repository.findById(id);
        if (optional.isPresent()) {
            logger.info("reading discipline, found discipline " + optional.get().getName().getName());
            return optional.get();
        }
        logger.warning("could not find discipline with the specified ID " + id);
        throw new EntityNotFoundException("Could not find discipline with ID " + id);
    }

    @Override
    public Discipline update(Discipline discipline) {
        if (discipline != null) {
            logger.info("updating valid discipline " + discipline.getId());
            Discipline oldDiscipline = readById(discipline.getId());
            if (oldDiscipline != null) {

                logger.info("updating uer id = " + discipline.getId() + " discipline name = " + discipline.getName().getName());
                return repository.save(discipline);
            }
        }
        logger.warning("discipline was not found in DB while updating, discipline does not exist");
        throw new NullEntityReferenceException("Something went wrong, try again");
    }

    @Override
    @Transactional
    public void delete(long id) {
        Discipline discipline = readById(id);
        repository.delete(discipline);

    }


    @Override
    public List<Discipline> getAll() {
        List<Discipline> disciplines = repository.findAll();
        logger.info("found " + disciplines.size() + " disciplines in DB");
        return disciplines.isEmpty() ? new ArrayList<>() : disciplines;
    }

    @Override
    public List<DisciplineDTO> getAllAsDto() {
        return getAll().stream().map(DisciplineDtoTransformer::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<DisciplineDTO> getAllForGroupAsDto(long id) {
        return repository.getAllForGroup(id).stream().map(DisciplineDtoTransformer::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Page<Discipline> getPaginatedDisciplines(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    @Override
    public Page<Discipline> getPaginatedDisciplines(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Discipline> searchPaginatedDisciplines(String keyword, int page, int size) {
        return repository.findByNameContaining(keyword, PageRequest.of(page, size));
    }

}
