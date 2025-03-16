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
@Table(name = "Teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id", nullable = false, updatable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    // Username with unique constraint
    @Column(name = "name", nullable = false, unique = true)
    private String fullName;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Course> assignedCourses;

    public Teacher(AppUser user, String fullName, String phone, List<Course> assignedCourses) {
        this.user = user;
        this.fullName = fullName;
        this.phone = phone;
        this.assignedCourses = assignedCourses;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", user=" + user +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}