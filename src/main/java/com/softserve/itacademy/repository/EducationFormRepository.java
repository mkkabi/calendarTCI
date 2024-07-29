package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.EducationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EducationFormRepository extends JpaRepository<EducationForm, Long> {
    Optional<EducationForm> findByTitle(String name);
    Optional<EducationForm> findById(Long id);
}
