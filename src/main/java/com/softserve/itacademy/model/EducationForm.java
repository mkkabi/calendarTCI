package com.softserve.itacademy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "education_form")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EducationForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "educationform-sequence")
    @GenericGenerator(
            name = "educationform-sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "educationform_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "30"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private  Long Id;

    @Pattern(regexp = "[A-Z, \\u0400-\\u042F][\\u0430-\\u0457,\\w,\\-,_(,), ,\\\"]+",
            message = "Must start with a capital Latin or Cyrillic letter followed by one or more lowercase latin or Cyrillic letters. Underscore, dash and quotations allowed")
    @Column(name = "title", nullable = false, unique = true)
    private String title;

//    @OneToMany(mappedBy = "educationForm")
//    List<Group> groups;

//    @OneToMany(mappedBy = "educationForm")
//    List<Discipline> disciplines;

//    @PreRemove
//    public void checkReviewAssociationBeforeRemoval() {
//        if (!this.disciplines.isEmpty()) {
//            throw new RuntimeException("Can't remove education form that has Disciplines.");
//        }
//        if (!this.groups.isEmpty()) {
//            throw new RuntimeException("Can't remove education form that has Groups.");
//        }
//    }
}
