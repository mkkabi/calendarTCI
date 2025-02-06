package com.mkkabi.dev.tools;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Tmp_unused {

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
}
