package com.mkkabi.dev.service;

import com.mkkabi.dev.model.DisciplineName;

import java.util.List;

public interface DisciplineNameService {
    DisciplineName create(DisciplineName disciplineName);
    DisciplineName readByName(String name);
    DisciplineName readById(long id);
    DisciplineName update(DisciplineName disciplineName);
    void delete(long id);
    List<DisciplineName> getAll();

}
