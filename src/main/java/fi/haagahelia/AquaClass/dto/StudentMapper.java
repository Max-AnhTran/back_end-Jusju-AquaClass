package fi.haagahelia.AquaClass.dto;

import fi.haagahelia.AquaClass.domain.AppUser;
import fi.haagahelia.AquaClass.domain.Student;

public class StudentMapper {
    public static StudentDTO toDTO(Student student) {
        return new StudentDTO(student.getId(), student.getFullName(), student.getPhone(), student.getDateOfBirth(),
                student.getUser().getId());
    }

    public static Student toEntity(StudentDTO studentDTO) {
        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setFullName(studentDTO.getFullName());
        student.setPhone(studentDTO.getPhone());
        student.setDateOfBirth(studentDTO.getDateOfBirth());
        student.getUser().setId(studentDTO.getAppUserId());
        return student;
    }

    public static Student toEntity(StudentDTO studentDTO, AppUser appUser) {
        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setFullName(studentDTO.getFullName());
        student.setPhone(studentDTO.getPhone());
        student.setDateOfBirth(studentDTO.getDateOfBirth());
        student.setUser(appUser);
        return student;
    }
}
