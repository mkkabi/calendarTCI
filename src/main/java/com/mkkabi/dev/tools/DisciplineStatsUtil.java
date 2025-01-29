package com.mkkabi.dev.tools;

import com.mkkabi.dev.dto.LessonDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DisciplineStatsUtil {
    private long Id;
    private int count;
    private String title;
    private int hoursTaught;
    // this number was provided during creation of Discipline Obj
    private int totalDisciplineHours;
    private final int hoursLef = totalDisciplineHours;

    public DisciplineStatsUtil(LessonDto lesson) {
        Id = lesson.getDisciplineId();
        count++;
        title = lesson.getDiscipline();
//        hoursTaught = ((lesson.getTimeEnd().toSecondOfDay() - lesson.getTimeStart().toSecondOfDay()) / 60.0)/60.0;
        hoursTaught = 2;
        totalDisciplineHours = lesson.getDisciplineHours();
    }

    public DisciplineStatsUtil merge(DisciplineStatsUtil lesson){
        if(count>0 && Id != lesson.getId())
            throw new IllegalArgumentException("trying to add merge DisciplineStatsUtil" +
                    "Current stats created for DisciplineStatsUtil ID="+Id+". Trying to add DisciplineStatsUtil ID="+lesson.getId());
        count++;
        hoursTaught += lesson.hoursTaught;
        return this;
    }

    public double countHoursLeft(){
        return totalDisciplineHours-hoursTaught;
    }
}
