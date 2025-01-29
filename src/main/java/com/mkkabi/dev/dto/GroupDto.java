package com.mkkabi.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupDto {
    private long id;
    private int admittanceYear, courseNumber;
    String groupNumber;
    private boolean graduated;
    private String alternativeName, educationForm;
    private LocalDate sessionStart, sessionEnd;
    List<String> disciplines;

}
