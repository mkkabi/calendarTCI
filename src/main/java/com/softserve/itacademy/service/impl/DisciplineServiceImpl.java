package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.dto.DisciplineDTO;
import com.softserve.itacademy.dto.DisciplineDtoTransformer;
import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Discipline;
import com.softserve.itacademy.model.Group;
import com.softserve.itacademy.repository.DisciplineRepository;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.service.DisciplineService;

import java.util.*;
import java.util.logging.Logger;

import com.softserve.itacademy.tools.AppLogger;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.TransactionScoped;
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

//        if (group != null) {
//            logger.info("group in not null");
//            List<Discipline> disciplinesFromParent = group.getDisciplines();
//            if (disciplinesFromParent != null) {
//                logger.info("disciplines FromParent in not null, size = "+disciplinesFromParent.size());
//                if (!disciplinesFromParent.isEmpty()) {
//                    logger.info("disciplines FromParent in not empty");
//                    for (int i = 0; i < disciplinesFromParent.size() - 1; i++) {
//                        if (disciplinesFromParent.get(i).getId() == discipline.getId()) {
//                            logger.info("found discipline " + discipline.getName().getName() + " in group " + group.getId());
//                            disciplinesFromParent.set(i, null);
//                         }
//                    }
//                    logger.info("before repository.delete discipline ID = "+discipline.getId());
//                    repository.deleteById(discipline.getId());
////                    logger.info("after repository.delete discipline ID = "+discipline.getId());
//                }
//            }
//
//            discipline.setDisciplineGroup(null);
//            repository.save(discipline);
//            groupRepository.save(group);
//
//        }

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
    public Discipline readByIdWithTeachers(long id){
        Optional<Discipline> optional = repository.readByIdWithTeachers(id);
        if (optional.isPresent()) {
            logger.info("reading discipline, found discipline " + optional.get().getName().getName());
            return optional.get();
        }
        logger.warning("could not find discipline with the specified ID " + id);
        throw new EntityNotFoundException("Could not find discipline with ID " + id);
    }

    @Override
    public Set<Discipline> getAllWithTeachersByGroup(long id) {
        Set<Discipline> disciplines = repository.getAllWithTeachersByGroup(id);
        logger.info("found " + disciplines.size() + " disciplines in DB");
        return disciplines.isEmpty() ? new HashSet<>() : disciplines;
    }

}
