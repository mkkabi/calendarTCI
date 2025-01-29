package com.mkkabi.dev.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="name")
public class TeacherDto {

    private long id;
    private String name, contact, commentWishes;
    private List<DisciplineDTO> disciplines;
}
