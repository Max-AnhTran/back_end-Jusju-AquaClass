package fi.haagahelia.AquaClass.domain;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false, updatable = false)
    private Long id;

    // Username with unique constraint
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level;

    public enum Level {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

    @Column(name = "maxStudents", nullable = false)
    private int maxStudents;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Registration> registrations;

    public Course(String name, String description, Level level, int maxStudents, Teacher teacher) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.maxStudents = maxStudents;
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", level=" + level +
                ", maxStudents=" + maxStudents +
                ", teacher=" + teacher +
                '}';
    }
}