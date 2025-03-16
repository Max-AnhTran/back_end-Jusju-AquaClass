package fi.haagahelia.AquaClass.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "Registration")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RegistrationStatus status;

    public enum RegistrationStatus {
        PENDING, ACCEPTED, DECLINED, CANCELED
    }

    public Registration() {
        this.status = RegistrationStatus.PENDING;
    }

    public Registration(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.status = RegistrationStatus.PENDING;
    }
}
