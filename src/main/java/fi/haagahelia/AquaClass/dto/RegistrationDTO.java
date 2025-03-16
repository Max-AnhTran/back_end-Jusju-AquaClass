package fi.haagahelia.AquaClass.dto;

import fi.haagahelia.AquaClass.domain.Registration.RegistrationStatus;

public class RegistrationDTO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private RegistrationStatus status;

    // Constructors
    public RegistrationDTO() {}

    public RegistrationDTO(Long id, Long studentId, Long courseId, RegistrationStatus status) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }
}