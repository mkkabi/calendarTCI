package com.softserve.itacademy.service;

import com.softserve.itacademy.model.DisciplineName;
import com.softserve.itacademy.model.User;

import java.util.List;

public interface DisciplineNameService {
    DisciplineName create(DisciplineName disciplineName);
    DisciplineName readByName(String name);
    DisciplineName readById(long id);
    DisciplineName update(DisciplineName disciplineName);
    void delete(long id);
    List<DisciplineName> getAll();

}
