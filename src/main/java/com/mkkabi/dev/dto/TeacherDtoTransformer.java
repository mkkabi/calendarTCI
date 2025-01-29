package com.mkkabi.dev.dto;

import com.mkkabi.dev.model.Teacher;

import java.util.List;
import java.util.stream.Collectors;

public class TeacherDtoTransformer {
    public static TeacherDto convertToDto(Teacher teacher) {
        List<DisciplineDTO> disciplineDTOList = null;
        if (teacher.getDisciplines().size()>0)
            disciplineDTOList = teacher.getDisciplines().stream()
                    .map(DisciplineDtoTransformer::convertToDto).collect(Collectors.toList());

        return new TeacherDto(teacher.getId(), teacher.getFio(),
                teacher.getContact(), teacher.getCommentWishes(), disciplineDTOList);
    }

    public static TeacherDtoNoDisciplines convertToDtoNoDisciplines(Teacher teacher){
        return new TeacherDtoNoDisciplines(teacher.getId(), teacher.getFio(), teacher.getContact(), teacher.getCommentWishes());
    }
}
