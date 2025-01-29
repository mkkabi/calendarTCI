package com.mkkabi.dev.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "classtypes")
public class ClassType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "classtype-sequence")
    @GenericGenerator(
            name = "classtype-sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "classtype_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "20"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long Id;

    @Pattern(regexp = "[A-Z, \\u0400-\\u042F][\\u0430-\\u0457,\\w,\\-,_(,), ,\\\"]+",
            message = "Must start with a capital letter followed by one or more lowercase letters")
    @Column(name="class_type", unique = true, nullable = false)
    private String type;

    @Column
    private boolean showOnSchedule;

    @Column
    private boolean doNOTCalculateHours;

    @ToString.Exclude
    @Pattern(regexp = "[\\w, #]{3,7}", message = "only colors in hex format, without leading #")
    @Column
    private String color;


}
