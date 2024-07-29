package com.softserve.itacademy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "discipline_names")
public class DisciplineName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "disciplinename-sequence")
    @GenericGenerator(
            name = "disciplinename-sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "disciplinename_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "30"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long Id;

//    @Pattern(regexp = "[A-Z\\u0400-\\u04FF][\\u0430-\\u0457. /?'\\\":;`!()]{1,65}",
//            message = "Must start with a capital Latin or Cyrillic letter followed by one or more lowercase latin or Cyrillic letters. Underscore, dash and quotations allowed")
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
