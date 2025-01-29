package com.mkkabi.dev.model;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "lesson-sequence")
    @GenericGenerator(
            name = "lesson-sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "lesson_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "420"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "group_id", nullable = false)
//    private Group group;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "lesson_group",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private List<Group> groups;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(name = "online")
    private boolean online;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classtype_id", nullable = false)
    private ClassType classType;

    @Column(name = "week_number", nullable = false)
    private int weekNumber;

    @Column(name = "dayOfWeek", nullable = false)
    private String dayOfWeek;

    @Column(name = "auditorium_number", nullable = true)
    private String auditoriumNumber = null;

    @Column(name = "month", nullable = false)
    private int month;

    @Column(name = "year", nullable = false)
    private int year;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;


    public void setLessonDataFromStartDateTime(LocalDateTime startDateTime) {
        WeekFields weekFields = WeekFields.of(Locale.GERMANY);
        setWeekNumber(startDateTime.get(weekFields.weekOfWeekBasedYear()));
        setDayOfWeek(startDateTime.getDayOfWeek().name());
        setMonth(startDateTime.getMonthValue());
        setYear(startDateTime.getYear());
    }

}
