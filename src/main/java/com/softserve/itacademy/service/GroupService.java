package com.softserve.itacademy.service;

import com.softserve.itacademy.dto.GroupDto;
import com.softserve.itacademy.model.Group;

import java.util.List;
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
