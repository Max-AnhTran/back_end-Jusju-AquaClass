package fi.haagahelia.AquaClass.dto;

import fi.haagahelia.AquaClass.domain.Course;
import fi.haagahelia.AquaClass.domain.Registration;
import fi.haagahelia.AquaClass.domain.Registration.RegistrationStatus;
import fi.haagahelia.AquaClass.domain.Student;
import fi.haagahelia.AquaClass.domain.CourseRepository;
import fi.haagahelia.AquaClass.domain.RegistrationRepository;
import fi.haagahelia.AquaClass.domain.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Lấy tất cả đăng ký
    public List<RegistrationDTO> getAllRegistrations() {
        return ((List<Registration>) registrationRepository.findAll()).stream()
                .map(RegistrationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Lấy đăng ký theo ID
    public RegistrationDTO getRegistrationById(Long id) {
        return registrationRepository.findById(id)
                .map(RegistrationMapper::toDTO)
                .orElse(null);
    }

    // Get registrations by course ID
    public List<RegistrationDTO> getRegistrationsByCourseId(Long courseId) {
        return registrationRepository.findByCourseId(courseId).stream()
                .map(RegistrationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get registrations by student ID
    public List<RegistrationDTO> getRegistrationsByStudentId(Long studentId) {
        return registrationRepository.findByStudentId(studentId).stream()
                .map(RegistrationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get registration status
    public RegistrationStatus getRegistrationStatus(Long studentId, Long courseId) {
        return registrationRepository.findByStudentIdAndCourseId(studentId, courseId)
                .map(Registration::getStatus)
                .orElse(null);
    }

    // Tạo đăng ký
    public RegistrationDTO createRegistration(RegistrationDTO registrationDTO) {
        Student student = studentRepository.findById(registrationDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(registrationDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Registration registration = RegistrationMapper.toEntity(registrationDTO, student, course);
        registration = registrationRepository.save(registration);
        return RegistrationMapper.toDTO(registration);
    }

    // Cập nhật đăng ký
    public RegistrationDTO updateRegistration(Long id, RegistrationDTO registrationDTO) {
        Registration existingRegistration = registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        Student student = studentRepository.findById(registrationDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(registrationDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        existingRegistration.setStudent(student);
        existingRegistration.setCourse(course);
        existingRegistration.setStatus(registrationDTO.getStatus());

        Registration updatedRegistration = registrationRepository.save(existingRegistration);
        return RegistrationMapper.toDTO(updatedRegistration);
    }

    // Xóa đăng ký
    public void deleteRegistration(Long id) {
        registrationRepository.deleteById(id);
    }

    // Update registration status
    public void updateRegistrationStatus(Long id, RegistrationStatus status) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        registration.setStatus(status);
        registrationRepository.save(registration);
    }
}