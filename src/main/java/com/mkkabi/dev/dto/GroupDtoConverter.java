package com.mkkabi.dev.dto;

import com.mkkabi.dev.model.Group;

import java.util.*;
import java.util.stream.Collectors;

public class GroupDtoConverter {

    public static GroupDto convertToDto(Group group){
        List<String> disciplines = group.getDisciplines().stream().map(d->d.getName().getName()).collect(Collectors.toList());

        return new GroupDto(group.getId(), group.getAdmittanceYear(), group.getCourseNumber(), group.getGroupNumber(), group.isGraduated(), group.getAlternativeName(),
                group.getEducationForm().getTitle(), group.getSessionStart(), group.getSessionEnd(), disciplines);
        }

}
