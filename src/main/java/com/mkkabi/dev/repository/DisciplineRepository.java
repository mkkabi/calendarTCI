package com.mkkabi.dev.repository;

import com.mkkabi.dev.model.Discipline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
//    @EntityGraph(attributePaths = {"disciplines"})

//    @Query("select d from Discipline d left join fetch d.teachers where d.id = :id")
//    Optional<Discipline> getByIdWithTeacher(long id);

//    @Query("SELECT distinct d FROM Discipline d left join fetch d.teachers where d.id = :id")
//    Set<Discipline> getAllWithTeachersByGroup(long id);

//    @Query("SELECT d FROM Discipline d left join fetch d.teachers where d.id = :id")
//    Optional<Discipline> readByIdWithTeachers(long id);

    @Query(value = "select distinct * from disciplines d left outer join groups g on d.group_id=g.id where g.id = ?1", nativeQuery = true)
    List<Discipline> getAllForGroup(long groupId);


    // Custom query with pagination
    @Query("select d from Discipline d")
    Page<Discipline> findAllPages(Pageable pageable);

    // If you want to filter with pagination
    Page<Discipline> findByNameContaining(String name, Pageable pageable);

}
