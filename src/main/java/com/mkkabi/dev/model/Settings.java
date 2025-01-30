package com.mkkabi.dev.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
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
