package com.mkkabi.dev.repository;

import com.mkkabi.dev.model.Group;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("SELECT g FROM Group g left join fetch g.disciplines where g.id = :id")
    Optional<Group> readById(long id);

    @EntityGraph(attributePaths = {"disciplines", "educationForm"})
    @Query("SELECT distinct g FROM Group g")
    Set<Group> getAll();

    @Query(value = "select distinct g.id from lesson_group lg " +
            "left join `groups` g on g.id=lg.group_id where lg.lesson_id =?1", nativeQuery = true)
    List<Long> getLessonGroupsIDs(long id);
}
