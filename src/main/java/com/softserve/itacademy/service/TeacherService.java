package com.softserve.itacademy.service;

import com.softserve.itacademy.dto.TeacherDto;
import com.softserve.itacademy.model.Teacher;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface TeacherService {
    Teacher create(Teacher teacher);
    Teacher readById(long id);
    TeacherDto readByIdAsDto(long id);
    Teacher update(Teacher teacher);
    void delete(long id);
    Set<Teacher> getAll();
    Set<TeacherDto> getAllAsDto();
}
