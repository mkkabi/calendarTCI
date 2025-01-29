package com.mkkabi.dev.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Event implements Comparable<Event>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "event_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "10"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;

    @NotBlank(message = "The 'title' cannot be empty")
    @Column(name = "title", nullable = false)
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    @Column(name = "week_number", nullable = false)
    private int week;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany
    @JoinTable(name = "event_attendees",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "attendee_id"))
    private List<User> attendees;

    @Column(name="durationMinutes", nullable = true)
    private long durationMinutes;

    public void setDuration(LocalDateTime start, LocalDateTime end){
        Duration tmp = Duration.between(start, end);
        this.durationMinutes = tmp.toMinutes();
    }

    public Event(LocalDateTime start, LocalDateTime end){
        this.startTime = start;
        this.endTime = end;
        Duration tmp = Duration.between(start, end);
        this.durationMinutes = tmp.toMinutes();
    }

    @Override
    public int compareTo(Event o) {
        return this.getStartTime().compareTo(o.getStartTime());
    }
}
