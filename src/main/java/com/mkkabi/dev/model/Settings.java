package com.mkkabi.dev.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "settings")
public class Settings {
// sets the date of the first day of the study year to calculate first week of study
    @Id
    private long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date", nullable = false)
    private LocalDate studyYearStartDate;

//    @Pattern(regexp = "[#][\\w]{3,6}",
//            message = "Must start with # followed 3 or 6 characters")
    @Column(nullable = false, name = "onlineclass_color")
    private String colorForOnlineClasses;
}
