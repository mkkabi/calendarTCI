package com.mkkabi.dev.service;

import com.mkkabi.dev.model.ClassType;

import java.util.List;

public interface ClassTypeService {
    ClassType create(ClassType classType);
    ClassType readById(long id);
    ClassType update(ClassType classType);
    void delete(long id);
    List<ClassType> getAll();
}
