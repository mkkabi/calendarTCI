package com.mkkabi.dev.tools;

import com.mkkabi.dev.dto.LessonDto;
import com.mkkabi.dev.model.Lesson;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.*;

@Getter
@AllArgsConstructor
public class DateTimeService {

    private int calendarCurrentWeek, actualCurrentWeek, actualWeekOfCalendarStartDate, numberOfWeeksInYear, weeksPassedFromCalendarStart;
    private LocalDate calendarStart;
    public final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    public final DateTimeFormatter fullDayFormatter = DateTimeFormatter.ofPattern("EEEE", Settings.locale);
    public final DateTimeFormatter fullMonthFormatter = DateTimeFormatter.ofPattern("MMMM", Settings.locale);
    public final DateTimeFormatter dayMonthYearFormat = DateTimeFormatter.ofPattern("d.MM.yyyy", Settings.locale);
    public final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy HH:mm", Settings.locale);


    public DateTimeService(LocalDate calendarStart) {
        this.calendarStart = calendarStart;
        actualWeekOfCalendarStartDate = calendarStart.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        actualCurrentWeek = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfYear());
        numberOfWeeksInYear = (int)calendarStart.range(WeekFields.of(Locale.getDefault()).weekOfYear()).getMaximum();
        weeksPassedFromCalendarStart = actualCurrentWeek-actualWeekOfCalendarStartDate;
        calendarCurrentWeek = weeksPassedFromCalendarStart+1;
    }

    public DateTimeService() {
    }

    // TODO add one week, otherwise it starts from week 0 used in UserService, will be deprecated soon
    @Deprecated
    public int getStudyWeekFromCalendarWeek(int weekNumber){
        return weekNumber-actualWeekOfCalendarStartDate +1;
    }


    public Map<String, Map<String, LessonDto>> createAndInitializeMapOfLessonsByOrderAndDayOfWeek(List<String> timeFrames, String[] daysOfWeekArr,
                                                                                                  List<LessonDto> weeklyLessons, Locale locale){

        Map<String, Map<String, LessonDto>> lessonsByOrderAndDayOfWeek = new LinkedHashMap<>();
        //initializing lessonsByOrderAndDayOfWeek with numbers and days of week
        int rows = timeFrames.size()<=0?3:timeFrames.size();
        for (int i = 0; i < rows; i++) {
            Map<String, LessonDto> newMap = new LinkedHashMap<>();
            for (String s : daysOfWeekArr) {
                newMap.put(s, null);
            }
            String timeframeStr = !timeFrames.isEmpty() ?timeFrames.get(i):i+"";
            lessonsByOrderAndDayOfWeek.put(timeframeStr, newMap);
        }

        for (LessonDto lessonDto : weeklyLessons) {
            String timeFrameString = lessonDto.getStartDateTime().format(timeFormatter)+" - "+lessonDto.getEndDateTime().format(timeFormatter);
            String dayOfWeek = getDayNameInLocale(lessonDto.getDateStart(), locale);
            if (lessonsByOrderAndDayOfWeek.get(timeFrameString) != null) {
                lessonsByOrderAndDayOfWeek.get(timeFrameString).put(dayOfWeek, lessonDto);
            }
            else{
                lessonsByOrderAndDayOfWeek.put(timeFrameString, new LinkedHashMap<>());
                lessonsByOrderAndDayOfWeek.get(timeFrameString).put(dayOfWeek, lessonDto);
            }
        }
        return lessonsByOrderAndDayOfWeek;
    }

    public String getDayNameInLocale(LocalDate localDate, Locale locale){
        return localDate.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, locale).toUpperCase();
    }


    public Map<Integer, String> getDaysOfWeekInLocaleMappedToNumberOfTheirOrder(LocalDate start, int from, int days, Locale locale){
        Map<Integer, String>  daysOfQueryWeekWithDates = new LinkedHashMap<>(days);
        start = start.with(DayOfWeek.of(from));
        for(int i = 1, y=0; i<=days-from+1; y++, i++){
            LocalDate date = start.plusDays(y);
            String dayOfWeekInLocale = getDayNameInLocale(date, locale);
            daysOfQueryWeekWithDates.put(i, dayOfWeekInLocale);
        }
        return daysOfQueryWeekWithDates;
    }

    public List<LocalDate> getLocalDatesOfWeek(LocalDate date, int start, int end){
        List<LocalDate> result = new ArrayList<>();
        for(int i=start; i<=end; i++){
            result.add(date.with(DayOfWeek.of(i)));
        }
        return result;
    }
    public Map<String, String> getDaysOfWeekWithDatesInLocale(LocalDate start, int from, int days, Locale locale){
        Map<String, String> daysOfQueryWeekWithDates = new LinkedHashMap<>(days);
        start = start.with(DayOfWeek.of(from));
        for(int i=0; i<days;i++){
            LocalDate date = start.plusDays(i).with(DayOfWeek.of(i+1));
            String dayOfWeekInLocale = getDayNameInLocale(date, locale);
            daysOfQueryWeekWithDates.put(dayOfWeekInLocale, date.format(dayMonthYearFormat));
        }
        return daysOfQueryWeekWithDates;
    }

    // gets week days from and to week day number, inclusive
    public String[] getWeekDays(int from, int to){
        DateFormatSymbols symbols = new DateFormatSymbols(Settings.locale);
        // for the current Locale :
        //   DateFormatSymbols symbols = new DateFormatSymbols();
        String[] result = new String[to-from+1];
        for(int i = from, y=0; i<=to;y++, i++){
            result[y] = symbols.getWeekdays()[i].toUpperCase();
        }
        return result;
    }

    public static Optional<LessonDto> checkIfLessonClashesWithAnother(Lesson newLesson, List<LessonDto> lessonDtos){
        return lessonDtos.stream()
                .filter(l->newLesson.getStartDateTime().isBefore(l.getEndDateTime()) &&
                        l.getStartDateTime().isBefore(newLesson.getEndDateTime())).findFirst();
    }


}
