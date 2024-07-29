package com.softserve.itacademy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@DynamicUpdate
@Table(name = "disciplines")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "discipline-sequence")
    @GenericGenerator(
            name = "discipline-sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "discipline_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "20"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;

    @ToString.Exclude
    @Range(min = 1, max = 10, message = "only numbers in range from 1 to 10")
    @Column(name = "semester", nullable = false)
    private int semester;

    @ToString.Exclude
    @Range(min = 1, max = 120, message = "only numbers between 1 and 200 allowed")
    @Column(name = "credits", nullable = false)
    private int credits;

//    @ToString.Exclude
//    @Range(min = 1, max = 200, message = "only numbers between 1 and 200 allowed")
//    @Column(name = "hours", nullable = false)
//    private int hours;

    @ToString.Include
    // ALL detached entity passed to persist:
    // Detach Unable to find com.softserve.itacademy.model.EducationForm with id 2
    // REMOVE Unable to find com.softserve.itacademy.model.EducationForm with id 2
    // PERSISt detached entity passed to persist:
    // MERGE - lets delete education form, but error 500 on discupline
    // REFRESH - lets delete education form, but error 500 on discupline
    @ManyToOne
    @JoinColumn(name = "education_form_id", nullable = true)
    private EducationForm educationForm;

    @ManyToOne
    @JoinColumn(name = "control_form_id", nullable = true)
    private ClassType controlForm;

    @ToString.Include
    @ManyToOne
    @JoinColumn(name = "discipline_name_id", nullable = false)
    private DisciplineName name;

    //DETACH, MERGE REFRESH Unable to find com.softserve.itacademy.model.Discipline with id 1;
    //REMOVE, DELETE Can't remove Group that has Disciplines.
    //ALL could not execute statement; SQL [n/a]; constraint [UK_s25dkat05i0m4b5ks5thtsomi];
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "group_id")
    private Group disciplineGroup;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "disciplines_teachers",
            joinColumns = @JoinColumn(name = "discipline_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    private List<Teacher> teachers;

    @ToString.Exclude
    @OneToMany(mappedBy = "discipline", cascade = CascadeType.MERGE)
    private List<Lesson> lessons;


    @PreRemove
    public void checkReviewAssociationBeforeRemoval() {
        if (!this.teachers.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            teachers.forEach(b->sb.append(b.getId()+" "));
            throw new RuntimeException("Can't remove Discipline that has teachers. teachers IDs: "+sb);
        }
        if (!this.lessons.isEmpty()) {
            throw new RuntimeException("Can't remove Discipline that has Lessons.");
        }
    }

}
