package com.mkkabi.dev.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="name")
public class TeacherDtoNoDisciplines {

    private long id;
    private String name, contact, commentWishes;

}