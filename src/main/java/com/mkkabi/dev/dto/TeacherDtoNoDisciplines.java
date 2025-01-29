package com.mkkabi.dev.dto;

import lombok.*;

import java.util.List;

import lombok.*;

        import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="name")
public class TeacherDtoNoDisciplines {

    private long id;
    private String name, contact, commentWishes;

}