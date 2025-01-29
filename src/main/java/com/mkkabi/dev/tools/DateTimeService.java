package com.mkkabi.dev.tools;

import com.mkkabi.dev.dto.LessonDto;
import com.mkkabi.dev.model.Lesson;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
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
            for (int y = 0; y < daysOfWeekArr.length; y++) {
                newMap.put(daysOfWeekArr[y], null);
            }
            String timeframeStr = timeFrames.size()>0?timeFrames.get(i):i+"";
            lessonsByOrderAndDayOfWeek.put(timeframeStr, newMap);
        }

        for (LessonDto lessonDto : weeklyLessons) {
            String timeFrameString = lessonDto.getStartDateTime().format(timeFormatter)+" - "+lessonDto.getEndDateTime().format(timeFormatter);
            String dayOfWeek = getDayNameInLocale(lessonDto.getDateStart(), locale);
            if (lessonsByOrderAndDayOfWeek.get(timeFrameString) != null) {
                lessonsByOrderAndDayOfWeek.get(timeFrameString).put(dayOfWeek, lessonDto);
            }
            else{
                lessonsByOrderAndDayOfWeek.put(timeFrameString, new LinkedHashMap<String, LessonDto>());
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

    // TODO needs to be tested first
    public LocalDate getFirstDayOfWeek(int year, int weekNumber, Locale locale) {
        return LocalDate
                .of(year, 2, 1)
                .with(WeekFields.of(locale).getFirstDayOfWeek())
                .with(WeekFields.of(locale).weekOfWeekBasedYear(), weekNumber);
    }

    private void printGregorianCalendarWithJavaCalendar(){
        Calendar calendar = new GregorianCalendar(2023, 0, 28);
        calendar.set(Calendar.DAY_OF_MONTH, 1); //Set the day of month to 1
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); //get day of week for 1st of month
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

//print month name and year
        System.out.println(new SimpleDateFormat("MMMM YYYY").format(calendar.getTime()));
        System.out.println(" S  M  T  W  T  F  S");

//print initial spaces
        String initialSpace = "";
        for (int i = 0; i < dayOfWeek - 1; i++) {
            initialSpace += "   ";
        }
        System.out.print(initialSpace);

//print the days of the month starting from 1
        for (int i = 0, dayOfMonth = 1; dayOfMonth <= daysInMonth; i++) {
            for (int j = ((i == 0) ? dayOfWeek - 1 : 0); j < 7 && (dayOfMonth <= daysInMonth); j++) {
                System.out.printf("%2d ", dayOfMonth);
                dayOfMonth++;
            }
            System.out.println();
        }
    }
    public static void printCalendarUsingLocalDate() {
        LocalDate localDate = LocalDate.of(2023, 8, 28);
        int daysInMonth = localDate.lengthOfMonth();
        Month month = localDate.getMonth();
        int year = localDate.getYear();

        int dayOfMonthEntered = localDate.getDayOfMonth();
        DayOfWeek dayOfWeekEntered = localDate.getDayOfWeek();

        LocalDate startOfMonth = LocalDate.of(year, month.getValue(), 01);
        DayOfWeek dayOfWeekOfFirstOfMonth = startOfMonth.getDayOfWeek();

        System.out.println(month.name());
        System.out.println(" S  M  T  W  T  F  S");


        //print initial spaces
        String initialSpace = "";
        for (int i = 0; i < dayOfWeekOfFirstOfMonth.getValue(); i++) {
            initialSpace += "   ";
        }
        System.out.print(initialSpace);


        //print the days of the month starting from 1
        for (int i = 0, dayOfMonth = 1; dayOfMonth <= daysInMonth; i++) {
            for (int j = ((i == 0) ? dayOfWeekOfFirstOfMonth.getValue() : 0); j < 7 && (dayOfMonth <= daysInMonth); j++) {
                System.out.printf("%2d ", dayOfMonth);
                dayOfMonth++;
            }
            System.out.println();
        }
    }

//    (StartA <= EndB)  and  (EndA >= StartB)
//This includes conditions where the edges overlap exactly. If you wish to exclude that,
//    change the >= operators to >, and <= to <
    //s1.before(e2) && e1.after(e2)

//    public static boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
//        return start1.before(end2) && start2.before(end1);
//    }
    public static Optional<LessonDto> checkIfLessonClashesWithAnother(Lesson newLesson, List<LessonDto> lessonDtos){
        for(LessonDto lessonDto : lessonDtos){
            boolean clashes = newLesson.getStartDateTime().isBefore(lessonDto.getEndDateTime()) && lessonDto.getStartDateTime().isBefore((newLesson.getEndDateTime()));
            System.out.println("newLesson.getStartDateTime() "+newLesson.getStartDateTime());
            System.out.println("newLesson.getEndDateTime() "+newLesson.getEndDateTime());
            System.out.println("lessonDto.getStartDateTime() "+lessonDto.getStartDateTime());
            System.out.println("lessonDto.getEndDateTime() "+lessonDto.getEndDateTime());
            System.out.println("lessons clash = "+clashes);
        }
        return lessonDtos.stream()
                .filter(l->newLesson.getStartDateTime().isBefore(l.getEndDateTime()) &&
                        l.getStartDateTime().isBefore(newLesson.getEndDateTime())).findFirst();
    }


}
