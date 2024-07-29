//package com.softserve.itacademy;
//
//
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class Tmp2 {
//    public static void main(String[] args) {
//        LocalDate localDate = LocalDate.of(2023, 7, 28);
//        int daysInMonth = localDate.lengthOfMonth();
//        Month month = localDate.getMonth();
//        int year = localDate.getYear();
//
//        int dayOfMonthEntered = localDate.getDayOfMonth();
//        DayOfWeek dayOfWeekEntered = localDate.getDayOfWeek();
//
//        LocalDate startOfMonth = LocalDate.of(year, month.getValue(), 01);
//        DayOfWeek dayOfWeekOfFirstOfMonth = startOfMonth.getDayOfWeek();
//
//        System.out.println(month.name());
//        System.out.println(" M  T  W  T  F  S  S");
//
//        Map<Integer, String> weekMap = new HashMap<>();
//        System.out.println(dayOfWeekOfFirstOfMonth.getValue());
//        for (int i = 0; i<dayOfWeekOfFirstOfMonth.getValue()-1; i++) {
//            if(i == (dayOfWeekOfFirstOfMonth.getValue()-2))
//                weekMap.put(i*-1, "START");
//            else
//                weekMap.put(i*-1, "SPACE");
//        }
//        for(int i=1; i<=daysInMonth; i++){
//            String day = localDate.withDayOfMonth(i).getDayOfWeek().toString();
//            weekMap.put(i, day);
//        }
//
//        String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
//        List<String> daysOfWeek = Arrays.asList(days);
//
//
//        for(Integer i : weekMap.keySet().stream().sorted().collect(Collectors.toList())){
//            if(i!=null) {
//                System.out.println(i + " from weekMap " + weekMap.get(i));
//                if(weekMap.get(i).equals("SUNDAY")){
//                    System.out.println();
//                }
//            }else{
//                System.out.println(" ");
//            }
////                String out = weekMap.get(day).equals(daysOfWeek.get(x))?String.format("%2d ", day):"";
////                System.out.print(out);
//
//
//        }
//
////        model.addAttribute("daysOfWeekList", daysOfWeek);
////        model.addAttribute("weekMap", weekMap);
//
//    }
//    }
//
