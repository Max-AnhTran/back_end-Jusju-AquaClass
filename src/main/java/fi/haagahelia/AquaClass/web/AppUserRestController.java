package fi.haagahelia.AquaClass.web;

import fi.haagahelia.AquaClass.domain.SignupForm;
import fi.haagahelia.AquaClass.dto.AppUserDTO;
import fi.haagahelia.AquaClass.dto.AppUserService;
import fi.haagahelia.AquaClass.dto.TeacherService;
import fi.haagahelia.AquaClass.domain.AppUser.Role;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/users")
public class AppUserRestController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private TeacherService teacherService;

    public AppUserRestController(AppUserService appUserService, TeacherService teacherService) {
        this.appUserService = appUserService;
        this.teacherService = teacherService;
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<AppUserDTO>> getAllUsers() {
        List<AppUserDTO> users = appUserService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> getUserById(@PathVariable Long id) {
        AppUserDTO appUserDTO = appUserService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        return new ResponseEntity<>(appUserDTO, HttpStatus.OK);
    }

    // Update user details
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @Valid @RequestBody AppUserDTO appUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        appUserService.updateAppUser(id, appUserDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        teacherService.deleteTeacher(teacherService.getTeacherByAppUserId(id).getId());
        appUserService.deleteAppUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Student signup
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupForm signupForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!signupForm.getPassword().equals(signupForm.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords do not match");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (appUserService.getUserByUsername(signupForm.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "err.username", "Username already exists");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        AppUserDTO newUserDTO = new AppUserDTO(
                null,
                signupForm.getUsername(),
                signupForm.getPassword(), // Password will be hashed inside the service
                signupForm.getEmail(),
                Role.STUDENT // Default role
        );

        appUserService.createAppUser(newUserDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Add new user
    @PostMapping
    public ResponseEntity<Void> addUser(@Valid @RequestBody AppUserDTO appUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        appUserService.createAppUser(appUserDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}