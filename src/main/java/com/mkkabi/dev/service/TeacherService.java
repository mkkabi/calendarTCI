package com.mkkabi.dev.service;

import com.mkkabi.dev.dto.TeacherDto;
import com.mkkabi.dev.model.Discipline;
import com.mkkabi.dev.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface TeacherService {
    Teacher create(Teacher teacher);
    Teacher readById(long id);
    TeacherDto readByIdAsDto(long id);
    Teacher update(Teacher teacher);
    void delete(long id);
    Set<Teacher> getAll();
    Set<TeacherDto> getAllAsDto();
    Set<Teacher> findActiveTeachersForGroup(long id);
    Page<TeacherDto> getPaginatedTeachersActiveOnly(Pageable pageable);
}
