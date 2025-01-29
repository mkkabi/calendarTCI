package com.mkkabi.dev.repository;


import com.mkkabi.dev.model.DisciplineName;
import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisciplineNameRepository extends JpaRepository<DisciplineName, Long> {
        Optional<DisciplineName> findDisciplineNameByName(String name);
}
