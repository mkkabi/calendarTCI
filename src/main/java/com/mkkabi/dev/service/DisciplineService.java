package com.mkkabi.dev.service;

import com.mkkabi.dev.dto.DisciplineDTO;
import com.mkkabi.dev.model.Discipline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DisciplineService {
    Discipline create(Discipline discipline);
    Discipline readById(long id);
    Discipline update(Discipline discipline);
    void delete(long id);
    List<Discipline> getAll();
    List<DisciplineDTO>getAllAsDto();
    List<DisciplineDTO> getAllForGroupAsDto(long id);

    Page<Discipline> getPaginatedDisciplines(int page, int size);
    Page<Discipline> getPaginatedDisciplines(Pageable pageable);
    Page<Discipline> searchPaginatedDisciplines(String keyword, int page, int size);

}
