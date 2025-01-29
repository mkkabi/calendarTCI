package com.mkkabi.dev.service;

import com.mkkabi.dev.model.EducationForm;

import java.util.List;

public interface EducationFormService extends GenericService{
    EducationForm create(EducationForm educationForm);
    EducationForm readByName(String name);
    EducationForm readById(Long id);
    EducationForm update(EducationForm educationForm);

    List<EducationForm> getAll();
}
