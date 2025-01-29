package com.mkkabi.dev.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class GroupsListDTO {
    private Set<GroupDto> groups;

    public GroupsListDTO(Set<GroupDto> groups) {
        this.groups = groups;
    }

}
