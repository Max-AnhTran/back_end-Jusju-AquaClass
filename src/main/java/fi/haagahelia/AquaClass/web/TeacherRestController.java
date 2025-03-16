package fi.haagahelia.AquaClass.web;

import fi.haagahelia.AquaClass.dto.AppUserDTO;
import fi.haagahelia.AquaClass.dto.AppUserService;
import fi.haagahelia.AquaClass.dto.TeacherDTO;
import fi.haagahelia.AquaClass.dto.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherRestController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AppUserService appUserService;

    public TeacherRestController(TeacherService teacherService, AppUserService appUserService) {
        this.teacherService = teacherService;
        this.appUserService = appUserService;
    }

    // Get all teachers
    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    // Get a teacher by ID
    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Long id) {
        TeacherDTO teacherDTO = teacherService.getTeacherById(id);
        if (teacherDTO != null) {
            return new ResponseEntity<>(teacherDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create a new teacher
    @PostMapping
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO teacherDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TeacherDTO savedTeacher = teacherService.saveTeacher(teacherDTO);
        return new ResponseEntity<>(savedTeacher, HttpStatus.CREATED);
    }

    // Update a teacher
    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(@PathVariable Long id, @RequestBody TeacherDTO teacherDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TeacherDTO updatedTeacher = teacherService.updateTeacher(id, teacherDTO);
        if (updatedTeacher != null) {
            return new ResponseEntity<>(updatedTeacher, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a teacher
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get users without a teacher profile (for reference)
    @GetMapping("/users/without-teacher")
    public ResponseEntity<List<AppUserDTO>> getUsersWithoutTeacher() {
        List<AppUserDTO> users = appUserService.getTeacherUsersWithoutTeacher();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}