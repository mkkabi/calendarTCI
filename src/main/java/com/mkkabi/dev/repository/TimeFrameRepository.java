package com.mkkabi.dev.repository;

import com.mkkabi.dev.model.TimeFrame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeFrameRepository extends JpaRepository<TimeFrame, Long> {
    Optional<TimeFrame> getTimeFrameByOrderNumber(int number);
}
