package com.softserve.itacademy.controller.rest;

import com.softserve.itacademy.controller.LessonController;
import com.softserve.itacademy.exception.DuplicateEventException;
import com.softserve.itacademy.service.LessonService;
import com.softserve.itacademy.tools.AppLogger;
import lombok.AllArgsConstructor;
import org.hibernate.jpa.internal.util.LogHelper;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.logging.Logger;

@RestController
@AllArgsConstructor
@RequestMapping("/api/groups")
public class GroupRestController {
    private final Logger logger = new AppLogger(this.getClass().getSimpleName());
private LessonService lessonService;

    @PostMapping("/testpost")
    public String testPost(@Param(value = "lesson1Html")
                           String lesson1Html, @Param(value = "lesson2Html") String lesson2Html,
                           @Param(value = "ctrlPressed") boolean ctrlPressed, Model model){

        System.out.println("lesson1Html = "+lesson1Html+" lesson2Html = "+lesson2Html + " ctrlPressed = "+ctrlPressed);
        if(lesson1Html.contains("id") && lesson2Html.contains("id")){
            long id1 = Long.parseLong(lesson1Html.split(":")[1].replace(" ", ""));
            long id2 = Long.parseLong(lesson2Html.split(":")[1]);
            logger.info("swapping lessons "+id1+" and "+id2);
            try {
                lessonService.swapDates(id1, id2);
            }catch (DuplicateEventException e){
                return "DUPLICATED;"+e.getMessage();
            }
        }
        if(lesson1Html.contains("id") && lesson2Html.contains("time")){
            long id1 = Long.parseLong(lesson1Html.split(":")[1].replace(" ", ""));
            String[] lesson2HtmlArr = lesson2Html.split(";");
            long timeframeId = Long.parseLong(lesson2HtmlArr[0].split(":")[1]);
            LocalDate date = LocalDate.parse(lesson2HtmlArr[1].split(":")[1]);
            System.out.println("timeframeID = "+timeframeId);
            System.out.println("date = "+date);
            try {
                if(!ctrlPressed) {
                    lessonService.moveToOtherDate(id1, timeframeId, date);
                }
                if(ctrlPressed){
                    lessonService.duplicateLessonToAnotherDate(id1, timeframeId, date);
                }

            } catch (DuplicateEventException e) {
                return "DUPLICATED;"+e.getMessage();
            }
        }

        return "OK";
    }
}