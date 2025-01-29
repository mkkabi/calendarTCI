package com.mkkabi.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class DisciplineDTO {
    private long id;
    private int semester, credits, hours;
    private String disciplineGroup, educationForm, name, controlForm;

    public DisciplineDTO(long id, int semester, int credits, int hours, String disciplineGroup, String educationForm, String name, String controlForm) {
        this.id = id;
        this.semester = semester;
        this.credits = credits;
        this.hours = hours;
        this.disciplineGroup = disciplineGroup;
        this.educationForm = educationForm;
        this.name = name;
        this.controlForm = controlForm;
    }
}
