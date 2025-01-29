package com.mkkabi.dev.repository;

import com.mkkabi.dev.model.Discipline;
import com.mkkabi.dev.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @EntityGraph(attributePaths = {"disciplines"})
    @Query("select t from Teacher t where t.id = :id")
    Optional<Teacher> findById(long id);

    @EntityGraph(attributePaths = {"disciplines"})
    @Query("SELECT distinct t from Teacher t")
    Set<Teacher> getAll();


    @Query(value = "select distinct * from teachers t left outer join disciplines_teachers dt on dt.teacher_id=t.id " +
            "left outer join disciplines d on d.id = dt.discipline_id " +
            "left outer join groups g on g.id = d.group_id " +
            "where g.id =?1 and t.hidden=0 ORDER BY `dt`.`teacher_id` ASC;", nativeQuery = true)
    Set<Teacher> findActiveTeachersForGroup(@Param("groupId") Long groupId);

//@Query(value = "select distinct * from groups g left outer join disciplines d on d.group_id=g.id" +
//        "left outer join disciplines_teachers dt on dt.teacher_id=d.id" +
//        "left outer join teachers t on t.id=dt.teacher_id" +
//        "where g.id=?1", nativeQuery = true)
//    Set<Teacher> findActiveTeachersForGroup(@Param("groupId") Long groupId);

    @Query(value = "select * from teachers t where t.hidden=0", nativeQuery = true)
    Page<Teacher> findAllActivePages(Pageable pageable);

//    @Query(value = "SELECT DISTINCT *   " +
//            "FROM teachers t " +
//            "LEFT OUTER JOIN disciplines_teachers dt ON dt.teacher_id = t.id " +
//            "LEFT OUTER JOIN disciplines d ON d.id = dt.discipline_id",
//            countQuery = "SELECT COUNT(DISTINCT t.id) " +
//                    "FROM teachers t " +
//                    "LEFT OUTER JOIN disciplines_teachers dt ON dt.teacher_id = t.id " +
//                    "LEFT OUTER JOIN disciplines d ON d.id = dt.discipline_id",
//            nativeQuery = true)
//    Page<Teacher> findAllPages(Pageable pageable);

}
