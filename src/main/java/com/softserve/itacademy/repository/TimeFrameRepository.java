package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.TimeFrame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeFrameRepository extends JpaRepository<TimeFrame, Long> {
    Optional<TimeFrame> getTimeFrameByOrderNumber(int number);
}
