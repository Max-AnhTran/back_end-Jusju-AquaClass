package fi.haagahelia.AquaClass.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fi.haagahelia.AquaClass.domain.AppUser;
import fi.haagahelia.AquaClass.domain.AppUserRepository;
import fi.haagahelia.AquaClass.domain.Student;
import fi.haagahelia.AquaClass.domain.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public StudentService(StudentRepository studentRepository, AppUserRepository appUserRepository) {
        this.studentRepository = studentRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<StudentDTO> getAllStudents() {
        return ((List<Student>) studentRepository.findAll())
                .stream()
                .map(StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return StudentMapper.toDTO(student);
    }

    public StudentDTO getStudentByAppUserId(Long appUserId) {
        // Sử dụng Optional để kiểm tra xem Student có tồn tại không
        Optional<Student> studentOptional = studentRepository.findByUserId(appUserId);
    
        // Nếu Student tồn tại, chuyển đổi sang DTO và trả về
        if (studentOptional.isPresent()) {
            return StudentMapper.toDTO(studentOptional.get());
        } else {
            // Nếu không tìm thấy, trả về null
            return null;
        }
    }

    public StudentDTO saveStudent(StudentDTO studentDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Student student = StudentMapper.toEntity(studentDTO, appUser);
        Student savedStudent = studentRepository.save(student);
        return StudentMapper.toDTO(savedStudent);
    }

    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        existingStudent.setFullName(studentDTO.getFullName());
        existingStudent.setPhone(studentDTO.getPhone());
        existingStudent.setDateOfBirth(studentDTO.getDateOfBirth());

        Student updatedStudent = studentRepository.save(existingStudent);
        return StudentMapper.toDTO(updatedStudent);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
