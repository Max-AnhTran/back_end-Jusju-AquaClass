package fi.haagahelia.AquaClass.dto;

import fi.haagahelia.AquaClass.domain.Registration;

import fi.haagahelia.AquaClass.domain.Course;
import fi.haagahelia.AquaClass.domain.Student;

public class RegistrationMapper {

    public static RegistrationDTO toDTO(Registration registration) {
        return new RegistrationDTO(
            registration.getId(),
            registration.getStudent().getId(),
            registration.getCourse().getId(),
            registration.getStatus()
        );
    }

    public static Registration toEntity(RegistrationDTO registrationDTO, Student student, Course course) {
        Registration registration = new Registration();
        registration.setId(registrationDTO.getId());
        registration.setStudent(student);
        registration.setCourse(course);
        registration.setStatus(registrationDTO.getStatus());
        return registration;
    }
}