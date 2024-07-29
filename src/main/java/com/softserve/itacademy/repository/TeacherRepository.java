package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.Lesson;
import com.softserve.itacademy.model.Teacher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {


//    @Query( value = "select * from teachers t left join disciplines_teachers dt on dt.teacher_id = t.id " +
//            "left join disciplines d on d.id=dt.discipline_id " +
//            "left join groups_teachers gt on t.id = gt.teacher_id "+
//            "left outer join `groups` g on g.id = gt.group_id "+
//            "where t.id = ?1", nativeQuery = true)
//    Optional<Teacher> findById(long id);


    /***
    // this setup actually works
    //@EntityGraph is used to specify an entity graph that eagerly loads disciplines and groups.
    // both associations will be eagerly loaded in a single query.
    @EntityGraph(attributePaths = {"disciplines", "groups"})
    @Query("SELECT t FROM Teacher t left JOIN FETCH t.disciplines where t.id = :id")
    Optional<Teacher> findById(long id);

    // this setup actually works
    @EntityGraph(attributePaths = {"disciplines", "groups"})
    @Query("SELECT distinct t FROM Teacher t left JOIN FETCH  t.disciplines")
    Set<Teacher> getAll();
*/

    @EntityGraph(attributePaths = {"disciplines"})
    @Query("select t from Teacher t where t.id = :id")
    Optional<Teacher> findById(long id);


    @EntityGraph(attributePaths = {"disciplines"})
    @Query("SELECT distinct t from Teacher t")
    Set<Teacher> getAll();

}
