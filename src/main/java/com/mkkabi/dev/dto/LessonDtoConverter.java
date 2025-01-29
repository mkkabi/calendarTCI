package com.mkkabi.dev.dto;

import com.mkkabi.dev.model.Group;
import com.mkkabi.dev.model.Lesson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LessonDtoConverter {

    public static LessonDto convertToDto(Lesson l) {
        long disciplineID = 0;
        int disciplineHours = 0;
        String disciplineName = "";

        List<Long> groupIds = new ArrayList<>();
        List<String> groupNames = new ArrayList<>();
        String groupAlternativeName = "";

        if (l.getGroups() != null && !l.getGroups().isEmpty()) {
            for (Group group : l.getGroups()) {
                groupIds.add(group.getId());
                groupNames.add(group.getGroupNumber());
            }

            // Assuming the alternative name is derived from the first group in the list, modify as needed
            Group firstGroup = l.getGroups().get(0);
            if (firstGroup.getAlternativeName() != null) {
                groupAlternativeName = firstGroup.getAlternativeName();
            }
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

         return new LessonDto(
                l.getId(),
                l.getTeacher().getId(),
                disciplineID,
                l.getWeekNumber(),
                l.getMonth(),
                l.getYear(),
                disciplineName,
                l.getTeacher().getFio(),
                groupAlternativeName,
                timeStart,
                timeEnd,
                dateStart,
                dateEnd,
                l.getStartDateTime(),
                l.getEndDateTime(),
                l.isOnline(),
                l.getClassType().isShowOnSchedule(),
                l.getClassType().isDoNOTCalculateHours(),
                l.getClassType().getColor(),
                l.getAuditoriumNumber(),
                l.getClassType().getType(),
                disciplineHours,
                groupIds,
                 groupNames

        );
    }
}
