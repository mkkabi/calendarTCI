package com.softserve.itacademy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
//@ToString
@javax.persistence.Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "group-sequence")
    @GenericGenerator(
            name = "group-sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "group_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "30"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;

    @Range(min = 2018, max = 2040,
            message = "Only 4 digit year between 2018 and 2040")
    @Column(name = "admittance_year", nullable = false)
    private int admittanceYear;

    @Column
    private boolean graduated;

    @Range(min = 1, max = 10,
            message = "Only number between 1 and 10")
    @Column(name = "course_number", nullable = false)
    private int courseNumber;

//    @Range(min = 1, max = 9999,
//            message = "Only number year between 1 and 9999")
    @Column(name = "group_number", nullable = false, unique = true)
    private String groupNumber;

    @Column(name = "alternative_name", nullable = true)
    private String alternativeName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "session_start", nullable = true)
    private LocalDate sessionStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "session_end", nullable = true)
    private LocalDate sessionEnd;

    @ManyToOne
    @JoinColumn(name = "education_form_id")
    private EducationForm educationForm;

    //    DETACH, MERGE Unable to find com.softserve.itacademy.model.Discipline with id 1;
    // fetchtype eager helped to refresh coupling on parent-child relation
    // try orphanRemoval = true
//    cascade="persist,merge,save-update"
//    @OneToMany(mappedBy = "disciplineGroup", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.ALL},
    @JsonIgnore
    @OneToMany(mappedBy = "disciplineGroup", cascade = {CascadeType.MERGE})
    private List<Discipline> disciplines;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    List<Lesson> lessons;

//    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    @JoinTable(name = "groups_teachers",
//            joinColumns = @JoinColumn(name = "group_id"),
//            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
//    private Set<Teacher> teachers;

    @PreRemove
    public void checkReviewAssociationBeforeRemoval() {
        if (!this.disciplines.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            disciplines.stream().forEach(b -> sb.append(b.getId() + " "));
            throw new RuntimeException("Can't remove Group that has Disciplines. Disciplines IDs: " + sb);
        }
        if (!this.lessons.isEmpty()) {
            throw new RuntimeException("Can't remove Group form that has Lessons.");
        }
    }
}