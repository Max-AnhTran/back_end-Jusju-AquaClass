package fi.haagahelia.AquaClass.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import fi.haagahelia.AquaClass.dto.AppUserService;
import fi.haagahelia.AquaClass.dto.TeacherDTO;
import fi.haagahelia.AquaClass.dto.TeacherService;

import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AppUserService appUserService;

    public TeacherController(TeacherService teacherService, AppUserService appUserService) {
        this.teacherService = teacherService;
        this.appUserService = appUserService;
    }

    @GetMapping
    public String teacherList(Model model) {
        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        
        model.addAttribute("teachers", teachers);
        model.addAttribute("teacherDTO", new TeacherDTO());
        model.addAttribute("users", appUserService.getTeacherUsersWithoutTeacher());
        return "teacherlist";
    }

    @PostMapping("/add")
    public String saveTeacher(@ModelAttribute TeacherDTO teacherDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teachers", teacherService.getAllTeachers());
            return "teacherlist";
        }
        teacherService.saveTeacher(teacherDTO);
        return "redirect:/teacher";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("teacherDTO", teacherService.getTeacherById(id));
        return "editteacher";
    }

    @PostMapping("/edit/{id}")
    public String updateTeacher(@PathVariable Long id, @ModelAttribute TeacherDTO teacherDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editteacher";
        }
        teacherService.updateTeacher(id, teacherDTO);
        return "redirect:/teacher";
    }

    @GetMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable("id") Long id) {
        teacherService.deleteTeacher(id);
        return "redirect:/teacher";
    }
}
