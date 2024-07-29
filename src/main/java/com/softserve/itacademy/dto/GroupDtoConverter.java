package com.softserve.itacademy.dto;

import com.softserve.itacademy.model.Discipline;
import com.softserve.itacademy.model.Group;
import com.softserve.itacademy.model.Teacher;

import java.time.LocalTime;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class GroupDtoConverter {

    public static GroupDto convertToDto(Group group){
        List<String> disciplines = group.getDisciplines().stream().map(d->d.getName().getName()).collect(Collectors.toList());
//        Map<Long, String> teachers = group.getTeachers().stream()
//                .collect(Collectors
//                        .toMap(teacher -> teacher.getId(), teacher -> teacher.getFirstName()+" "+teacher.getLastName()
//        ));
        return new GroupDto(group.getId(), group.getAdmittanceYear(), group.getCourseNumber(), group.getGroupNumber(), group.isGraduated(), group.getAlternativeName(),
                group.getEducationForm().getTitle(), group.getSessionStart(), group.getSessionEnd(), disciplines);
        }

}
