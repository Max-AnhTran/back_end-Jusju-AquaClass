package fi.haagahelia.AquaClass.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.haagahelia.AquaClass.domain.Course;
import fi.haagahelia.AquaClass.domain.Teacher;
import fi.haagahelia.AquaClass.domain.CourseRepository;
import fi.haagahelia.AquaClass.domain.TeacherRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;

    // Get all courses as DTOs
    public List<CourseDTO> getAllCourses() {
        return ((List<Course>) courseRepository.findAll())
                .stream()
                .map(CourseMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get a single course by ID
    public CourseDTO getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(CourseMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    // Save a new course
    public CourseDTO saveCourse(CourseDTO courseDTO) {
        if (courseDTO.getTeacherId() == null) {
            throw new IllegalArgumentException("Teacher ID must not be null");
        }

        Course course = CourseMapper.toEntity(courseDTO);
        Teacher teacher = teacherRepository.findById(courseDTO.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        course.setTeacher(teacher);
        
        Course savedCourse = courseRepository.save(course);
        return CourseMapper.toDTO(savedCourse);
    }

    // Update an existing course
    public CourseDTO updateCourse(Long id, CourseDTO updatedCourseDTO) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        existingCourse.setName(updatedCourseDTO.getName());
        existingCourse.setDescription(updatedCourseDTO.getDescription());
        existingCourse.setLevel(updatedCourseDTO.getLevel());
        existingCourse.setMaxStudents(updatedCourseDTO.getMaxStudents());

        if (updatedCourseDTO.getTeacherId() == null) {
            throw new IllegalArgumentException("Teacher ID must not be null");
        }

        Teacher teacher = teacherRepository.findById(updatedCourseDTO.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        existingCourse.setTeacher(teacher);

        courseRepository.save(existingCourse);
        return CourseMapper.toDTO(existingCourse);
    }

    // Delete a course
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}