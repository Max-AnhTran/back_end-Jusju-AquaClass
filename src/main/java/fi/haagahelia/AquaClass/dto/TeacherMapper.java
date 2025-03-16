package fi.haagahelia.AquaClass.dto;

import fi.haagahelia.AquaClass.domain.Teacher;

public class TeacherMapper {
    public static TeacherDTO toDTO(Teacher teacher) {
        return new TeacherDTO(teacher.getId(), teacher.getFullName(), teacher.getPhone(), teacher.getUser().getId());
    }

    public static Teacher toEntity(TeacherDTO teacherDTO) {
        Teacher teacher = new Teacher();
        teacher.setId(teacherDTO.getId());
        teacher.setFullName(teacherDTO.getFullName());
        teacher.setPhone(teacherDTO.getPhone());
        return teacher;
    }
}
