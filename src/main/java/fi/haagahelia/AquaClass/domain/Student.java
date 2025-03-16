package fi.haagahelia.AquaClass.domain;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id", nullable = false, updatable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    // Username with unique constraint
    @Column(name = "name", nullable = false, unique = true)
    private String fullName;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "dateOfBirth", nullable = false)
    private String dateOfBirth;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Registration> registrations;

    public Student(AppUser user, String fullName, String phone, String dateOfBirth, List<Registration> registrations) {
        this.user = user;
        this.fullName = fullName;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.registrations = registrations;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", user=" + user +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                '}';
    }
}
