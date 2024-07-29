package com.softserve.itacademy;

import com.softserve.itacademy.tools.Settings;

import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Tmp {
    public static void main(String[] args) {


        for (int i = 1; i < 4; i++) {
            System.out.println(i);
            for (int y = 1; y < 5; y++) {
                System.out.print(y  +"  ");
            }
            System.out.println();
        }


    }
}
