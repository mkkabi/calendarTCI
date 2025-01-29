package com.mkkabi.dev.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "users")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user-sequence")
    @GenericGenerator(
            name = "user-sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "10"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
//    @SequenceGenerator(name = "toDo_Gen", sequenceName = "toDo_sequence", initialValue = 10, allocationSize = 1)
    private long id;

    @Pattern(regexp = "[A-Z][a-z]+",
            message = "Must start with a capital letter followed by one or more lowercase letters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Pattern(regexp = "[A-Z][a-z]+",
            message = "Must start with a capital letter followed by one or more lowercase letters")
    @Column(name = "last_name", nullable = false)
    private String lastName;


    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    // oThe mappedBy property is what we use to tell Hibernate which variable we are using to represent the parent class in our child class.
    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private List<Event> myEvents;

    @ManyToMany
    @JoinTable(name = "event_attendees",
            joinColumns = @JoinColumn(name = "attendee_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> attendingEvents;

}
