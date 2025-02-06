package com.mkkabi.dev.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(of={"id", "teacher", "dateStart"})
public class LessonDto {
    private long id, teacherId, disciplineId;
    private int weekNumber, month, year;
    private String discipline, teacher, groupAlternativeName;
    private LocalTime timeStart, timeEnd;
    private LocalDate dateStart, dateEnd;
    private LocalDateTime startDateTime, endDateTime;
    private boolean isOnline, showClassTypeOnSchedule, doNotCalculateHours;
    String classTypeColor, auditoriumNumber, classType;
    int disciplineHours;
    List<Long> groups;
    public List<Long> groups() {
        return groups;
    }
    List<String> groupNames;
    String comment;


}