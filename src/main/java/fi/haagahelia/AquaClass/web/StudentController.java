package fi.haagahelia.AquaClass.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import fi.haagahelia.AquaClass.dto.AppUserDTO;
import fi.haagahelia.AquaClass.dto.StudentDTO;
import fi.haagahelia.AquaClass.dto.StudentService;
import fi.haagahelia.AquaClass.dto.AppUserService;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private AppUserService appUserService;

    public StudentController(StudentService studentService, AppUserService appUserService) {
        this.studentService = studentService;
        this.appUserService = appUserService;
    }

    @GetMapping
    public String studentList(Model model) {
        List<StudentDTO> students = studentService.getAllStudents();
        List<AppUserDTO> users = appUserService.getAllUsers();
        Map<Long, String> usersMap = users.stream()
                .collect(Collectors.toMap(AppUserDTO::getId, AppUserDTO::getUsername));
        model.addAttribute("students", students);
        model.addAttribute("usersMap", usersMap);
        model.addAttribute("studentDTO", new StudentDTO()); // Form model attribute
        return "studentlist";
    }

    @GetMapping("/add")
    public String addStudent(Model model) {
        model.addAttribute("studentDTO", new StudentDTO());
        return "addstudent";
    }

    @PostMapping("/add")
    public String saveStudent(@ModelAttribute StudentDTO studentDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents());
            return "studentlist";
        }
        studentService.saveStudent(studentDTO);
        return "redirect:/courselist";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("studentDTO", studentService.getStudentById(id));
        return "editstudent";
    }

    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute StudentDTO studentDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editstudent";
        }
        studentService.updateStudent(id, studentDTO);
        return "redirect:/student";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return "redirect:/student";
    }
}
