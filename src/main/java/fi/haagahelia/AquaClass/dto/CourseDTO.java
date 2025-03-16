package fi.haagahelia.AquaClass.dto;

import fi.haagahelia.AquaClass.domain.Course.Level;

public class CourseDTO {
    private Long id;
    private String name;
    private String description;
    private Level level;
    private int maxStudents;
    private Long teacherId;
    private String teacherName;

    // Constructors
    public CourseDTO() {
    }

    public CourseDTO(Long id, String name, String description, Level level, int maxStudents, Long teacherId,
            String teacherName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.level = level;
        this.maxStudents = maxStudents;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
