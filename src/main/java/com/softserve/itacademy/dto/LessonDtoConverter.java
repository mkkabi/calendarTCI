package com.softserve.itacademy.dto;

import com.softserve.itacademy.model.Lesson;

import java.time.LocalDate;
import java.time.LocalTime;

public class LessonDtoConverter {

    public static LessonDto convertToDto(Lesson l) {
        long groupID = 0, disciplineID = 0;
        String groupNumber = "";
        int disciplineHours = 0;
        String disciplineName = "";
        if (l.getGroup() != null) {
            groupID = l.getGroup().getId();
            groupNumber = l.getGroup().getGroupNumber();
        }
        if (l.getDiscipline() != null) {
            disciplineID = l.getDiscipline().getId();
            disciplineHours = l.getDiscipline().getCredits() * 15;
            disciplineName = l.getDiscipline().getName().getName();
        }
        LocalTime timeStart = l.getStartDateTime().toLocalTime();
        LocalTime timeEnd = l.getEndDateTime().toLocalTime();
        LocalDate dateStart = l.getStartDateTime().toLocalDate();
        LocalDate dateEnd = l.getEndDateTime().toLocalDate();

        String groupAlternativeName = "";
        if (l.getGroup()!= null && l.getGroup().getAlternativeName() != null)
            groupAlternativeName = l.getGroup().getAlternativeName();

        return new LessonDto(l.getId(), l.getTeacher().getId(), groupID, disciplineID, groupNumber,
                l.getWeekNumber(), l.getMonth(), l.getYear(), disciplineName, l.getTeacher().getFio(), groupAlternativeName, timeStart, timeEnd,
                dateStart, dateEnd, l.getStartDateTime(), l.getEndDateTime(), l.isOnline(), l.getClassType().isShowOnSchedule(),
                l.getClassType().isDoNOTCalculateHours(), l.getClassType().getColor(), l.getAuditoriumNumber(), l.getClassType().getType(),
                disciplineHours
        );
    }

}