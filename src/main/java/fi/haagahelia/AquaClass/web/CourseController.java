package fi.haagahelia.AquaClass.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import fi.haagahelia.AquaClass.dto.CourseDTO;
import fi.haagahelia.AquaClass.dto.AppUserDTO;
import fi.haagahelia.AquaClass.dto.StudentDTO;

import fi.haagahelia.AquaClass.dto.CourseService;
import fi.haagahelia.AquaClass.dto.TeacherService;
import fi.haagahelia.AquaClass.dto.StudentService;
import fi.haagahelia.AquaClass.dto.AppUserService;
import fi.haagahelia.AquaClass.dto.RegistrationService;

@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private RegistrationService registrationService;

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    public CourseController(CourseService courseService, StudentService studentService, TeacherService teacherService,
            AppUserService appUserService, RegistrationService registrationService) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.appUserService = appUserService;
        this.registrationService = registrationService;
    }

    @GetMapping({ "/", "/courselist" })
    public String courseList(Model model) {
        // Lấy thông tin người dùng hiện tại
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUserDTO appUser = appUserService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kiểm tra xem người dùng đã có thông tin Student chưa
        StudentDTO student = studentService.getStudentByAppUserId(appUser.getId());
        if (student != null) {
            model.addAttribute("student", student); // Truyền thông tin Student vào view

            // Lấy danh sách courses và kiểm tra trạng thái đăng ký
            List<CourseDTO> courses = courseService.getAllCourses();
            Map<Long, String> registrationStatusMap = new HashMap<>();
            for (CourseDTO course : courses) {
                String status = String.valueOf(registrationService.getRegistrationStatus(student.getId(), course.getId()));
                registrationStatusMap.put(course.getId(), status);
            }

            model.addAttribute("courses", courses);
            model.addAttribute("registrationStatusMap", registrationStatusMap); // Truyền trạng thái đăng ký vào view
        } else {
            model.addAttribute("courses", courseService.getAllCourses());
        }

        return "courselist";
    }

    @GetMapping("/addcourse")
    public String addCourseForm(Model model) {
        model.addAttribute("course", new CourseDTO());
        model.addAttribute("teachers", teacherService.getAllTeachers());
        return "addcourse";
    }

    @PostMapping("/addcourse")
    public String saveCourse(@ModelAttribute CourseDTO courseDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teachers", teacherService.getAllTeachers());
            return "addcourse";
        }

        courseService.saveCourse(courseDTO);
        return "redirect:/courselist";
    }

    @GetMapping("/editcourse/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        model.addAttribute("teachers", teacherService.getAllTeachers());
        return "editcourse";
    }

    @PostMapping("/editcourse/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute CourseDTO courseDTO, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teachers", teacherService.getAllTeachers());
            return "editcourse";
        }

        courseService.updateCourse(id, courseDTO);
        return "redirect:/courselist";
    }

    @GetMapping("/deletecourse/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteCourse(id);
        return "redirect:/courselist";
    }


}
