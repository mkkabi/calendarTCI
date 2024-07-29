package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.DisciplineDTO;
import com.softserve.itacademy.dto.TeacherDto;
import com.softserve.itacademy.dto.TeacherDtoNoDisciplines;
import com.softserve.itacademy.dto.TeacherDtoTransformer;
import com.softserve.itacademy.model.*;
import com.softserve.itacademy.model.Discipline;
import com.softserve.itacademy.model.Discipline;
import com.softserve.itacademy.service.*;
import com.softserve.itacademy.tools.AppLogger;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/disciplines")
public class DisciplineController {
    //TODO create discipline page with all information, including
    //        isGraduated
    //        sessionEnd
    //        disciplines<>
    //        lessons<>
    //        teachers<>
    private final Logger logger = new AppLogger(this.getClass().getSimpleName());
    private final DisciplineNameService disciplineNameService;
    private final DisciplineService disciplineService;
    private final EducationFormService educationFormService;
    private final GroupService groupService;
    private final TeacherService teacherService;
    private final ClassTypeService classTypeService;


    @GetMapping("/create")
    public String create(Model model){
        setModelData(model);
        model.addAttribute("discipline", new Discipline());
        return "create-discipline";
    }

    private void setModelData(Model model) {
        model.addAttribute("disciplineNames", disciplineNameService.getAll().stream()
                .sorted(Comparator.comparing(DisciplineName::getName)).collect(Collectors.toList()));
        model.addAttribute("educationForms", educationFormService.getAll());
        model.addAttribute("groups", groupService.getAllAsDto());
        model.addAttribute("teachers", teacherService.getAllAsDto().stream()
                .sorted(Comparator.comparing(TeacherDto::getName)).collect(Collectors.toList()));
//        model.addAttribute("teachers", teacherService.getAll().stream().map(TeacherDtoTransformer::convertToDtoNoDisciplines)
//                        .sorted(Comparator.comparing(TeacherDtoNoDisciplines::getName)).collect(Collectors.toList()));
        model.addAttribute("controlForms", classTypeService.getAll());
    }

    @PostMapping("/create")
    public String create(
                         @RequestParam("group_id") long groupId,
                         @RequestParam("discipline_name_id") long disciplineNameId,
                         @RequestParam("education_form_id") long educationFormId,
                         @Validated @ModelAttribute("discipline") Discipline discipline, BindingResult result, Model model) {
        if (result.hasErrors()) {
            setModelData(model);
            model.addAttribute("discipline", discipline);
            return "create-discipline";
        }

        discipline.setName(disciplineNameService.readById(disciplineNameId));
        discipline.setDisciplineGroup(groupService.readById(groupId));
        discipline.setEducationForm(educationFormService.readById(educationFormId));
        Discipline newDiscipline = disciplineService.create(discipline);
        return "redirect:/disciplines/all/";
    }


    @GetMapping("/all")
    public String getAll(Model model) {
        List<DisciplineDTO> disciplines = disciplineService.getAllAsDto();
        model.addAttribute("disciplines", disciplines);
        if(disciplines==null || disciplines.isEmpty())
            model.addAttribute("message", "No disciplines found, add new");

        return "discipline-list";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") long id, RedirectAttributes rat) {
        try {
            disciplineService.delete(id);

        }catch (RuntimeException e){
            rat.addFlashAttribute("warning", e.getMessage());
        }
        return "redirect:/disciplines/all";
    }

    @GetMapping(value = {"/{id}/edit", "/{id}/update"})
    public String edit(@PathVariable long id, Model model){
        Discipline discipline = disciplineService.readByIdWithTeachers(id);
        model.addAttribute("discipline", discipline);


        model.addAttribute("disciplineNames", disciplineNameService.getAll().stream()
                .sorted(Comparator.comparing(DisciplineName::getName)).collect(Collectors.toList()));
        model.addAttribute("educationForms", educationFormService.getAll());
//        model.addAttribute("groups", groupService.getAllAsDto());
//        model.addAttribute("teachers", teacherService.getAll());
//        model.addAttribute("teachers", teacherService.getAll().stream().map(TeacherDtoTransformer::convertToDtoNoDisciplines)
//                        .sorted(Comparator.comparing(TeacherDtoNoDisciplines::getName)).collect(Collectors.toList()));
        model.addAttribute("controlForms", classTypeService.getAll());


//        List<TeacherDtoNoDisciplines> disciplineTeachers = discipline.getTeachers().stream()
//                .map(TeacherDtoTransformer::convertToDtoNoDisciplines).collect(Collectors.toList());
//        model.addAttribute("disciplineTeachers", disciplineTeachers);
        return "edit-discipline";
    }

    @PostMapping(value = {"/{id}/edit", "/{id}/update"})
    public String edit(@PathVariable long id,
                       @RequestParam("educationForm") long educationFormId,
                       @RequestParam("controlForm") long controlFormId,
                       @RequestParam("name") long disciplineNameId,
//                       @RequestParam("group_id") long disciplineGroupId,
                       @Validated @ModelAttribute("discipline") Discipline discipline, BindingResult result, Model model) {
        if (result.hasErrors()) {
            setModelData(model);
            return "edit-discipline";
        }
        discipline.setControlForm(classTypeService.readById(controlFormId));
        discipline.setName(disciplineNameService.readById(disciplineNameId));
//        discipline.setDisciplineGroup(groupService.readById(disciplineGroupId));
        discipline.setEducationForm(educationFormService.readById(educationFormId));
        discipline.setDisciplineGroup(discipline.getDisciplineGroup());
        discipline.setTeachers(discipline.getTeachers());
        disciplineService.update(discipline);
        return "redirect:/disciplines/all";
    }

}
