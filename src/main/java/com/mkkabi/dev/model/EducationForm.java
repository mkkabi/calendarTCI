package com.mkkabi.dev.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

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
}
