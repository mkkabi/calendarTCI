package com.mkkabi.dev.controller;

import com.mkkabi.dev.dto.DisciplineDTO;
import com.mkkabi.dev.dto.DisciplineDtoTransformer;
import com.mkkabi.dev.dto.TeacherDtoNoDisciplines;
import com.mkkabi.dev.dto.TeacherDtoTransformer;
import com.mkkabi.dev.model.Discipline;
import com.mkkabi.dev.model.DisciplineName;
import com.mkkabi.dev.model.Group;
import com.mkkabi.dev.service.*;
import com.mkkabi.dev.tools.AppLogger;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
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
        model.addAttribute("teachers", teacherService.getAll().stream().map(TeacherDtoTransformer::convertToDtoNoDisciplines)
                .sorted(Comparator.comparing(TeacherDtoNoDisciplines::getName)).collect(Collectors.toList()));
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
        disciplineService.create(discipline);
        return "redirect:/disciplines/all/";
    }

    @GetMapping("/all")
    public String listDisciplines(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortBy,
                                  @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Discipline> disciplines = disciplineService.getPaginatedDisciplines(pageable);
        // Convert each Discipline to DisciplineDTO
        Page<DisciplineDTO> disciplinesDto =
//                disciplines.map(DisciplineDtoTransformer::convertToDto);
                new PageImpl<>(disciplines.map(DisciplineDtoTransformer::convertToDto)
                .stream()
//                .sorted((o1, o2) -> (int)(o1.getId()-o2.getId()))
                .collect(Collectors.toList()),disciplines.getPageable(), disciplines.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("disciplines", disciplinesDto);
        model.addAttribute("pages", true);
        model.addAttribute("groups", groupService.getAllAsDto());
        if(disciplines==null || disciplines.isEmpty())
            model.addAttribute("message", "No disciplines found, add new");
        return "discipline-list";
    }

    @GetMapping("/group/{id}")
    public String getForGroup(@PathVariable("id") long id, Model model) {
        List<DisciplineDTO> disciplines = disciplineService.getAllForGroupAsDto(id)
                .stream().sorted((o2, o1) -> (int)(o1.getId()-o2.getId())).collect(Collectors.toList());
        model.addAttribute("disciplines", disciplines);
        model.addAttribute("groups", groupService.getAllAsDto().stream().sorted((o2,o1)-> (int)(o2.getId()-o1.getId())).collect(Collectors.toList()));
        model.addAttribute("pages", false);
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
        Discipline discipline = disciplineService.readById(id);
        model.addAttribute("discipline", discipline);
        model.addAttribute("disciplineNames", disciplineNameService.getAll().stream()
                .sorted(Comparator.comparing(DisciplineName::getName)).collect(Collectors.toList()));
        model.addAttribute("educationForms", educationFormService.getAll());
        model.addAttribute("controlForms", classTypeService.getAll());
        return "edit-discipline";
    }

    @PostMapping(value = {"/{id}/edit", "/{id}/update"})
    public String edit(@PathVariable("id") Long id,
                       @RequestParam("educationForm") long educationFormId,
                       @RequestParam("controlForm") long controlFormId,
                       @RequestParam("name") long disciplineNameId,
                       @Validated @ModelAttribute("discipline") Discipline discipline, BindingResult result, Model model) {
        if (result.hasErrors()) {
            setModelData(model);
            return "edit-discipline";
        }
        discipline.setControlForm(classTypeService.readById(controlFormId));
        discipline.setName(disciplineNameService.readById(disciplineNameId));
        discipline.setEducationForm(educationFormService.readById(educationFormId));

        disciplineService.update(discipline);

        return "redirect:/disciplines/all";
    }

}
