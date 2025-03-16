package fi.haagahelia.AquaClass.dto;

import fi.haagahelia.AquaClass.domain.Course;

public class CourseMapper {

    public static CourseDTO toDTO(Course course) {
        return new CourseDTO(
                course.getId(),
                course.getName(),
                course.getDescription(),
                course.getLevel(),
                course.getMaxStudents(),
                course.getTeacher().getId(),
                course.getTeacher().getFullName());
    }

    public static Course toEntity(CourseDTO dto) {
        Course course = new Course();
        course.setId(dto.getId());
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setLevel(dto.getLevel());
        course.setMaxStudents(dto.getMaxStudents());
        return course;
    }
}