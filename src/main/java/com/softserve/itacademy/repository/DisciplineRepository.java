package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.Discipline;
import com.softserve.itacademy.model.Group;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
//    @EntityGraph(attributePaths = {"disciplines"})

//    @Query("select d from Discipline d left join fetch d.teachers where d.id = :id")
//    Optional<Discipline> getByIdWithTeacher(long id);

    @Query("SELECT distinct d FROM Discipline d left join fetch d.teachers where d.id = :id")
    Set<Discipline> getAllWithTeachersByGroup(long id);

    @Query("SELECT d FROM Discipline d left join fetch d.teachers where d.id = :id")
    Optional<Discipline> readByIdWithTeachers(long id);

}
