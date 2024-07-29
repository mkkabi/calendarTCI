package com.softserve.itacademy.dto;

import com.softserve.itacademy.model.Discipline;
import com.softserve.itacademy.model.Group;
import com.softserve.itacademy.model.Lesson;
import com.softserve.itacademy.model.Teacher;

import java.util.List;

public class DisciplineDtoTransformer {

    public static DisciplineDTO convertToDto(Discipline discipline){
        String disciplineGroup = discipline.getDisciplineGroup()==null?"":discipline.getDisciplineGroup().getGroupNumber();
        return new DisciplineDTO(discipline.getId(), discipline.getSemester(), discipline.getCredits(), discipline.getCredits()*15,
                disciplineGroup, discipline.getEducationForm().getTitle(),
                discipline.getName().getName(), discipline.getControlForm().getType());
    }


    // TODO this may never be required by the app, will give it some time to see
    public static Discipline convertToEntity(DisciplineDTO disciplineDTO, List<Lesson> lessons, List<Teacher> teachers, Group group){
        return null;
    }


}
