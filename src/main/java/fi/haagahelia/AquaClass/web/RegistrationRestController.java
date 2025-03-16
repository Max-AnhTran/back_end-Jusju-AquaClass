package fi.haagahelia.AquaClass.web;

import fi.haagahelia.AquaClass.domain.Registration.RegistrationStatus;
import fi.haagahelia.AquaClass.dto.AppUserDTO;
import fi.haagahelia.AquaClass.dto.AppUserService;
import fi.haagahelia.AquaClass.dto.RegistrationDTO;
import fi.haagahelia.AquaClass.dto.RegistrationService;
import fi.haagahelia.AquaClass.dto.StudentDTO;
import fi.haagahelia.AquaClass.dto.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationRestController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AppUserService appUserService;

    public RegistrationRestController(RegistrationService registrationService, StudentService studentService, AppUserService appUserService) {
        this.registrationService = registrationService;
        this.studentService = studentService;
        this.appUserService = appUserService;
    }

    // Get all registrations
    @GetMapping
    public ResponseEntity<List<RegistrationDTO>> getAllRegistrations() {
        List<RegistrationDTO> registrations = registrationService.getAllRegistrations().stream()
                .sorted(Comparator.comparing(RegistrationDTO::getCourseId))
                .collect(Collectors.toList());
        return new ResponseEntity<>(registrations, HttpStatus.OK);
    }

    // Get registrations by course ID
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<RegistrationDTO>> getRegistrationsByCourseId(@PathVariable Long courseId) {
        List<RegistrationDTO> registrations = registrationService.getRegistrationsByCourseId(courseId);
        return new ResponseEntity<>(registrations, HttpStatus.OK);
    }

    // Create a new registration
    @PostMapping
    public ResponseEntity<RegistrationDTO> createRegistration(@RequestBody RegistrationDTO registrationDTO) {
        RegistrationDTO createdRegistration = registrationService.createRegistration(registrationDTO);
        return new ResponseEntity<>(createdRegistration, HttpStatus.CREATED);
    }

    // Create a registration by course ID (for the logged-in student)
    @PostMapping("/course/{courseId}")
    public ResponseEntity<RegistrationDTO> createRegistrationByCourseId(@PathVariable Long courseId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUserDTO appUser = appUserService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        StudentDTO student = studentService.getStudentByAppUserId(appUser.getId());

        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setStudentId(student.getId());
        registrationDTO.setCourseId(courseId);
        registrationDTO.setStatus(RegistrationStatus.PENDING);

        RegistrationDTO createdRegistration = registrationService.createRegistration(registrationDTO);
        return new ResponseEntity<>(createdRegistration, HttpStatus.CREATED);
    }

    // Get a registration by ID
    @GetMapping("/{id}")
    public ResponseEntity<RegistrationDTO> getRegistrationById(@PathVariable Long id) {
        RegistrationDTO registrationDTO = registrationService.getRegistrationById(id);
        if (registrationDTO != null) {
            return new ResponseEntity<>(registrationDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update a registration
    @PutMapping("/{id}")
    public ResponseEntity<RegistrationDTO> updateRegistration(@PathVariable Long id, @RequestBody RegistrationDTO registrationDTO) {
        RegistrationDTO updatedRegistration = registrationService.updateRegistration(id, registrationDTO);
        if (updatedRegistration != null) {
            return new ResponseEntity<>(updatedRegistration, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a registration
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        registrationService.deleteRegistration(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Accept a registration
    @PostMapping("/accept/{id}")
    public ResponseEntity<Void> acceptRegistration(@PathVariable Long id) {
        registrationService.updateRegistrationStatus(id, RegistrationStatus.ACCEPTED);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Decline a registration
    @PostMapping("/decline/{id}")
    public ResponseEntity<Void> declineRegistration(@PathVariable Long id) {
        registrationService.updateRegistrationStatus(id, RegistrationStatus.DECLINED);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}