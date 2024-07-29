package com.softserve.itacademy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;


@NamedEntityGraph(
        name = "teacher-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("role"),
                @NamedAttributeNode("disciplines")
        }
)
@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "teacher-sequence")
    @GenericGenerator(
            name = "teacher-sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "teacher_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "30"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;

    @Pattern(regexp = "[A-Z,\\u0400-\\u042FF][\\u0430-\\u0457 \\w . - `]{1,10}[A-Z,\\u0400-\\u042FF]{1}.[A-Z,\\u0400-\\u042FF]{1}.",
            message = "First, last and father's name in format Марюхно В.В.")
    @Column(name = "fio", nullable = false)
    private String fio;

//    @Pattern(regexp = "[A-Z, \\u0400-\\u042F][\\u0430-\\u0457,\\w,\\-,_(,), ,\\\"]{1,10}",
//            message = "Must start with a capital letter followed by one or more lowercase letters")
//    @Column(name = "last_name", nullable = false)
//    private String lastName;
//
//    @Pattern(regexp = "[A-Z, \\u0400-\\u042F][\\u0430-\\u0457,\\w,\\-,_(,), ,\\\"]{1,10}",
//            message = "Must start with a capital letter followed by one or more lowercase letters")
//    @Column(name = "fathers_name", nullable = false)
//    private String fathersName;

    @Column(name = "contact", nullable = true)
    private String contact;

    @Column(name = "comment", nullable = true)
    private String commentWishes;

    @ManyToOne
    //(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany
//    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "disciplines_teachers",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id"))
    private List<Discipline> disciplines;

//    fetch = FetchType.EAGER may be an alternative to lazy initialization ani-pattern
//    @ManyToMany
//    // supposed to stop loading disciplines recursively from groups. Used for JSON serialization
//    @JsonIgnoreProperties(value = "disciplines")
//    @Fetch(FetchMode.SUBSELECT)
//    @JoinTable(name = "groups_teachers",
//            joinColumns = @JoinColumn(name = "teacher_id"),
//            inverseJoinColumns = @JoinColumn(name = "group_id"))
//    private Set<Group> groups;


    @PreRemove
    public void checkReviewAssociationBeforeRemoval() {
        if (!this.disciplines.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            disciplines.stream().forEach(b->sb.append(b.getId()+" "));
            throw new RuntimeException("Can't remove Teacher that has Disciplines. Disciplines IDs: "+sb);
        }
//        if (!this.groups.isEmpty()) {
//            throw new RuntimeException("Can't remove Teacher form that has Lessons.");
//        }
    }
}
