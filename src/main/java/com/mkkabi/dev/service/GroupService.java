package com.mkkabi.dev.service;

import com.mkkabi.dev.dto.GroupDto;
import com.mkkabi.dev.model.Group;

import java.util.Set;

public interface GroupService {
    Group create(Group user);
    Group readById(long id);
    GroupDto readByIdAsDto(long id);
    Group update(Group user);
    void delete(long id);
    Set<Group> getAll();
    Set<GroupDto> getAllAsDto();
}
