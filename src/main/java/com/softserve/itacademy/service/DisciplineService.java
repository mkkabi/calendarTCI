package com.softserve.itacademy.service;

import com.softserve.itacademy.dto.DisciplineDTO;
import com.softserve.itacademy.model.Discipline;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DisciplineService {
    Discipline create(Discipline discipline);
    Discipline readById(long id);
    Discipline update(Discipline discipline);
    void delete(long id);
    List<Discipline> getAll();
    List<DisciplineDTO>getAllAsDto();

    Discipline readByIdWithTeachers(long id);
    Set<Discipline> getAllWithTeachersByGroup(long id);
}
