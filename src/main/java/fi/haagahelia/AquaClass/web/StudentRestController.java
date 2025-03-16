package fi.haagahelia.AquaClass.web;

import fi.haagahelia.AquaClass.dto.AppUserDTO;
import fi.haagahelia.AquaClass.dto.StudentDTO;
import fi.haagahelia.AquaClass.dto.StudentService;
import fi.haagahelia.AquaClass.dto.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AppUserService appUserService;

    public StudentRestController(StudentService studentService, AppUserService appUserService) {
        this.studentService = studentService;
        this.appUserService = appUserService;
    }

    // Get all students
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Get a student by ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO studentDTO = studentService.getStudentById(id);
        if (studentDTO != null) {
            return new ResponseEntity<>(studentDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create a new student
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        StudentDTO savedStudent = studentService.saveStudent(studentDTO);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    // Update a student
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        StudentDTO updatedStudent = studentService.updateStudent(id, studentDTO);
        if (updatedStudent != null) {
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get all users mapped to their IDs (for reference)
    @GetMapping("/users")
    public ResponseEntity<Map<Long, String>> getAllUsers() {
        List<AppUserDTO> users = appUserService.getAllUsers();
        Map<Long, String> usersMap = users.stream()
                .collect(Collectors.toMap(AppUserDTO::getId, AppUserDTO::getUsername));
        return new ResponseEntity<>(usersMap, HttpStatus.OK);
    }
}