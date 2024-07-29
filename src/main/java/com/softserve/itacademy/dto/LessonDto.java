package com.softserve.itacademy.dto;

import com.softserve.itacademy.model.TimeFrame;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(of={"id", "teacher", "dateStart"})
public class LessonDto {
    private long id, teacherId, groupId, disciplineId;
    private String groupNumber;
    private int weekNumber, month, year;
    private String discipline, teacher, groupAlternativeName;
    private LocalTime timeStart, timeEnd;
    private LocalDate dateStart, dateEnd;
    private LocalDateTime startDateTime, endDateTime;
    private boolean isOnline, showClassTypeOnSchedule, doNotCalculateHours;
    String classTypeColor, auditoriumNumber, classType;
    int disciplineHours;

}
