package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.dto.GroupDto;
import com.softserve.itacademy.dto.GroupDtoConverter;
import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Group;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.service.GroupService;

import java.util.*;
import java.util.logging.Logger;
import com.softserve.itacademy.tools.AppLogger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {
    private final Logger logger = new AppLogger("GroupServiceImpl.class");

    private GroupRepository repository;

    public GroupServiceImpl(GroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public Group create(Group group) {
        try {
            Group savedGroup = repository.save(group);
            logger.info("saved group "+savedGroup.getId());
            return savedGroup;
        } catch (IllegalArgumentException e) {
            logger.warning("group was null, no group created");
            throw new NullEntityReferenceException("Group was not created, try again");
        }
    }

    @Override
//    @Transactional(readOnly = true)
    public Group readById(long id) {
        Optional<Group> optional = repository.readById(id);
        if (optional.isPresent()) {
            logger.info("reading group, found group number "+optional.get().getGroupNumber());
            return optional.get();
        }
        logger.warning("could not find group with the specified ID "+id);
        throw new EntityNotFoundException("Could not find group with ID "+id);
    }

    @Override
    public GroupDto readByIdAsDto(long id){
        return GroupDtoConverter.convertToDto(readById(id));
    }

    @Override
    public Group update(Group group) {
        if (group != null) {
            logger.info("updating valid group "+group.getId());
            Group oldGroup = readById(group.getId());
            if (oldGroup != null) {

                logger.info("updating uer id = "+group.getId()+" group role = "+group.getGroupNumber());
                return repository.save(group);
            }
        }
        logger.warning("group was not found in DB while updating, group does not exist");
        throw new NullEntityReferenceException("Something went wrong, try again");
    }

    @Override
    public void delete(long id) {
        Group group = readById(id);
        if (group != null) {
            logger.info("size of disciplines in Group = "+group.getDisciplines().size());
            repository.delete(group);
        } else {
            throw new EntityNotFoundException("Could not find group with ID "+id);
        }
    }

//    @Transactional(readOnly = true)
    @Override
    public Set<Group> getAll() {
        Set<Group> groups = repository.getAll();
        logger.info("found "+groups.size()+" groups in DB");
        return groups.isEmpty() ? new HashSet<>() : groups;
    }

    @Override
    public Set<GroupDto> getAllAsDto() {
        return getAll().stream().map(GroupDtoConverter::convertToDto).collect(Collectors.toSet());
    }

}
