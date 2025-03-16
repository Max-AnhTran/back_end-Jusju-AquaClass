package fi.haagahelia.AquaClass.web;

import fi.haagahelia.AquaClass.domain.Registration.RegistrationStatus;

import fi.haagahelia.AquaClass.dto.AppUserDTO;
import fi.haagahelia.AquaClass.dto.AppUserService;
import fi.haagahelia.AquaClass.dto.CourseDTO;
import fi.haagahelia.AquaClass.dto.CourseService;
import fi.haagahelia.AquaClass.dto.RegistrationDTO;
import fi.haagahelia.AquaClass.dto.RegistrationService;
import fi.haagahelia.AquaClass.dto.StudentDTO;
import fi.haagahelia.AquaClass.dto.StudentService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private AppUserService appUserService;

    public RegistrationController(RegistrationService registrationService, StudentService studentService,
            CourseService courseService, AppUserService appUserService) {
        this.registrationService = registrationService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.appUserService = appUserService;
    }

    // Lấy tất cả đăng ký
    @GetMapping
    public String getAllRegistrations(Model model) {
        List<StudentDTO> students = studentService.getAllStudents();
        Map<Long, String> studentsMap = students.stream()
                .collect(Collectors.toMap(StudentDTO::getId, StudentDTO::getFullName));
        model.addAttribute("studentsMap", studentsMap);

        List<CourseDTO> courses = courseService.getAllCourses();
        Map<Long, String> coursesMap = courses.stream()
                .collect(Collectors.toMap(CourseDTO::getId, CourseDTO::getName));
        model.addAttribute("coursesMap", coursesMap);

        List<RegistrationDTO> registrations = registrationService.getAllRegistrations().stream()
                .sorted(Comparator.comparing(RegistrationDTO::getCourseId))
                .collect(Collectors.toList());
        model.addAttribute("registrations", registrations);
        return "registrationlist";
    }

    // Xem danh sách đăng ký của một khóa học
    @GetMapping("/course/{courseid}")
    public String getRegistrationsByCourseId(@PathVariable Long courseid, Model model) {
        List<StudentDTO> students = studentService.getAllStudents();
        Map<Long, String> studentsMap = students.stream()
                .collect(Collectors.toMap(StudentDTO::getId, StudentDTO::getFullName));
        model.addAttribute("studentsMap", studentsMap);

        List<CourseDTO> courses = courseService.getAllCourses();
        Map<Long, String> coursesMap = courses.stream()
                .collect(Collectors.toMap(CourseDTO::getId, CourseDTO::getName));
        model.addAttribute("coursesMap", coursesMap);

        List<RegistrationDTO> registrations = registrationService.getRegistrationsByCourseId(courseid);
        model.addAttribute("registrations", registrations);
        return "registrationlist";
    }

    // Hiển thị form tạo đăng ký mới
    @GetMapping("/add")
    public String showAddRegistrationForm(Model model) {
        model.addAttribute("registration", new RegistrationDTO());
        return "registration-add";
    }

    // Tạo đăng ký mới
    @PostMapping("/add")
    public String createRegistration(@ModelAttribute RegistrationDTO registrationDTO,
            RedirectAttributes redirectAttributes) {
        registrationService.createRegistration(registrationDTO);
        redirectAttributes.addFlashAttribute("message", "Registration created successfully!");
        return "redirect:/registration";
    }

    // Tạo đăng ký theo ID khóa học
    @PostMapping("/add/course/{courseId}")
    public String createRegistrationByCourseId(@PathVariable Long courseId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUserDTO appUser = appUserService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        StudentDTO student = studentService.getStudentByAppUserId(appUser.getId());

        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setStudentId(student.getId());
        registrationDTO.setCourseId(courseId);
        registrationDTO.setStatus(RegistrationStatus.PENDING);

        registrationService.createRegistration(registrationDTO);
        return "redirect:/courselist";
    }

    // Hiển thị form chỉnh sửa đăng ký
    @GetMapping("/edit/{id}")
    public String showEditRegistrationForm(@PathVariable Long id, Model model) {
        RegistrationDTO registrationDTO = registrationService.getRegistrationById(id);
        if (registrationDTO != null) {
            model.addAttribute("registration", registrationDTO);
            return "registration-edit";
        } else {
            return "redirect:/registration";
        }
    }

    // Cập nhật đăng ký
    @PostMapping("/edit/{id}")
    public String updateRegistration(@PathVariable Long id, @ModelAttribute RegistrationDTO registrationDTO,
            RedirectAttributes redirectAttributes) {
        RegistrationDTO updatedRegistration = registrationService.updateRegistration(id, registrationDTO);
        if (updatedRegistration != null) {
            redirectAttributes.addFlashAttribute("message", "Registration updated successfully!");
            redirectAttributes.addFlashAttribute("error", "Registration not found!");
        }
        return "redirect:/registrations";
    }

    // Xóa đăng ký
    @GetMapping("/delete/{id}")
    public String deleteRegistration(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        registrationService.deleteRegistration(id);
        redirectAttributes.addFlashAttribute("message", "Registration deleted successfully!");
        return "redirect:/registrations";
    }

    // Handle accept
    @PostMapping("/accept/{id}")
    public String acceptRegistration(@PathVariable Long id, HttpServletRequest request) {
        registrationService.updateRegistrationStatus(id, RegistrationStatus.ACCEPTED);

        // Get the original URL from the "referer" header
        String referer = request.getHeader("Referer");

        // Check if the original URL contains "/registration/course/"
        if (referer != null && referer.contains("/registration/course/")) {
            // Extract courseId from the original URL
            String[] parts = referer.split("/");
            String courseId = parts[parts.length - 1]; // Get the last part of the URL

            return "redirect:/registration/course/" + courseId;
        } else {
            return "redirect:/registration";
        }
    }

    // Handle decline
    @PostMapping("/decline/{id}")
    public String declineRegistration(@PathVariable Long id, HttpServletRequest request) {
        registrationService.updateRegistrationStatus(id, RegistrationStatus.DECLINED);

        // Get the original URL from the "referer" header
        String referer = request.getHeader("Referer");

        // Check if the original URL contains "/registration/course/"
        if (referer != null && referer.contains("/registration/course/")) {
            // Extract courseId from the original URL
            String[] parts = referer.split("/");
            String courseId = parts[parts.length - 1]; // Get the last part of the URL

            return "redirect:/registration/course/" + courseId;
        } else {
            return "redirect:/registration";
        }
    }
}