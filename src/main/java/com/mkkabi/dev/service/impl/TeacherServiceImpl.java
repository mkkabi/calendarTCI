package com.mkkabi.dev.service.impl;

import com.mkkabi.dev.dto.TeacherDto;
import com.mkkabi.dev.dto.TeacherDtoTransformer;
import com.mkkabi.dev.exception.NullEntityReferenceException;
import com.mkkabi.dev.model.Discipline;
import com.mkkabi.dev.model.Teacher;
import com.mkkabi.dev.repository.TeacherRepository;
import com.mkkabi.dev.tools.AppLogger;
import com.mkkabi.dev.service.TeacherService;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
//import javax.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
//@Transactional
//@Data is a convenient shortcut annotation that bundles the features of @ToString , @EqualsAndHashCode , @Getter / @Setter and @RequiredArgsConstructor
@Data
public class TeacherServiceImpl implements TeacherService {
    private final Logger logger = new AppLogger(this.getClass().getSimpleName());

    private TeacherRepository repository;

    public TeacherServiceImpl(TeacherRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public Teacher create(Teacher teacher) {
        try {
            Teacher savedTeacher = repository.save(teacher);
            logger.info("saved teacher " + savedTeacher.getId());
            return savedTeacher;
        } catch (IllegalArgumentException e) {
            logger.warning("teacher was null, no teacher created");
            throw new NullEntityReferenceException("Teacher was not created, try again");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher readById(long id) {
        Optional<Teacher> optional = repository.findById(id);
        if (optional.isPresent()) {
            logger.info("reading teacher, found teacher " + optional.get().getFio());
            return optional.get();
        }
        logger.warning("could not find teacher with the specified ID " + id);
        throw new EntityNotFoundException("Could not find teacher with ID " + id);
    }

    @Override
    public TeacherDto readByIdAsDto(long id) {
        return TeacherDtoTransformer.convertToDto(readById(id));
    }

    @Transactional
    @Override
    public Teacher update(Teacher teacher) {
        if (teacher != null) {
            logger.info("updating valid teacher " + teacher.getId());
            Teacher oldTeacher = readById(teacher.getId());
            if (oldTeacher != null) {
                logger.info("updating uer id = " + teacher.getId() + " teacher role = " + teacher.getRole().getName());
                return repository.save(teacher);
            }
        }
        logger.warning("teacher was not found in DB while updating, teacher does not exist");
        throw new NullEntityReferenceException("Something went wrong, try again");
    }

    @Override
    public void delete(long id) {
        Teacher teacher = readById(id);
        if (teacher != null) {
            repository.delete(teacher);
        } else {
            throw new EntityNotFoundException("Could not find teacher with ID " + id);
        }
    }

    @Override
    public Set<Teacher> getAll() {
        Set<Teacher> teachers = repository.getAll();
        logger.info("found " + teachers.size() + " teachers in DB");
        return teachers.isEmpty() ? new HashSet<>() : teachers;
    }

    @Override
    public Set<TeacherDto> getAllAsDto() {
        return getAll().stream().map(TeacherDtoTransformer::convertToDto).collect(Collectors.toSet());
    }

    @Override
    public Set<Teacher> findActiveTeachersForGroup(long id) {
        return repository.findActiveTeachersForGroup(id);
    }

    @Transactional
    @Override
    public Page<TeacherDto> getPaginatedTeachersActiveOnly(Pageable pageable) {
        Page<Teacher> teachers = repository.findAllActivePages(pageable);

        return new PageImpl<>(teachers.map(TeacherDtoTransformer::convertToDto)
                .stream()
                .collect(Collectors.toList()), teachers.getPageable(), teachers.getTotalElements());
    }

}
