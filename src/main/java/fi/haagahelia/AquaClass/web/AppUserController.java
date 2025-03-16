package fi.haagahelia.AquaClass.web;

import fi.haagahelia.AquaClass.domain.SignupForm;
import fi.haagahelia.AquaClass.dto.AppUserDTO;
import fi.haagahelia.AquaClass.dto.AppUserService;
import fi.haagahelia.AquaClass.dto.TeacherService;
import jakarta.validation.Valid;
import fi.haagahelia.AquaClass.domain.AppUser.Role;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private TeacherService teacherService;

    public AppUserController(AppUserService appUserService, TeacherService teacherService) {
        this.appUserService = appUserService;
        this.teacherService = teacherService;
    }

    // Display all users
    @GetMapping("/user")
    public String userList(Model model) {
        model.addAttribute("users", appUserService.getAllUsers());
        return "userlist";
    }

    // Edit existing user form
    @GetMapping("/user/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        AppUserDTO appUserDTO = appUserService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        model.addAttribute("user", appUserDTO);
        return "edituser";
    }

    // Update user details
    @PostMapping("/user/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute AppUserDTO appUserDTO, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "edituser";
        }

        appUserService.updateAppUser(id, appUserDTO);
        return "redirect:/user";
    }

    // Delete user by ID
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        teacherService.deleteTeacher(teacherService.getTeacherByAppUserId(id).getId());
        appUserService.deleteAppUser(id);
        return "redirect:/user";
    }

    // Student signup form
    @GetMapping("/signup")
    public String addStudent(Model model) {
        model.addAttribute("signupform", new SignupForm());
        return "signup";
    }

    // Save the new student user
    @PostMapping("/saveuser")
    public String save(@Valid @ModelAttribute("signupform") SignupForm signupForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup"; // Return signup page if validation errors exist
        }

        if (!signupForm.getPassword().equals(signupForm.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords do not match");
            return "signup";
        }

        if (appUserService.getUserByUsername(signupForm.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "err.username", "Username already exists");
            return "signup";
        }

        // Create DTO and pass it to the service layer
        AppUserDTO newUserDTO = new AppUserDTO(
                null,
                signupForm.getUsername(),
                signupForm.getPassword(), // Password will be hashed inside the service
                signupForm.getEmail(),
                Role.STUDENT // Default role
        );

        appUserService.createAppUser(newUserDTO);

        return "redirect:/login";
    }

    // Add new user form
    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new AppUserDTO());
        return "adduser";
    }

    // Save the new user
    @PostMapping("/user/save")
    public String saveUser(@ModelAttribute AppUserDTO appUserDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "adduser";
        }

        appUserService.createAppUser(appUserDTO);
        return "redirect:/user";
    }
}