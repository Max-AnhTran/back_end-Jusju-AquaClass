package fi.haagahelia.AquaClass;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// import fi.haagahelia.AquaClass.domain.*;
import fi.haagahelia.AquaClass.domain.Course;
import fi.haagahelia.AquaClass.domain.CourseRepository;
import fi.haagahelia.AquaClass.domain.Student;
import fi.haagahelia.AquaClass.domain.Teacher;
import fi.haagahelia.AquaClass.domain.TeacherRepository;
import fi.haagahelia.AquaClass.domain.AppUserRepository;
import fi.haagahelia.AquaClass.domain.AppUser;
import fi.haagahelia.AquaClass.domain.StudentRepository;
import fi.haagahelia.AquaClass.domain.Registration;
import fi.haagahelia.AquaClass.domain.RegistrationRepository;

@Configuration
public class DatabaseLoader {
    @Bean
    public CommandLineRunner demo(TeacherRepository teacherRepository, CourseRepository courseRepository, AppUserRepository appUserRepository, StudentRepository studentRepository, RegistrationRepository registrationRepository) {
        return (args) -> {
            // Clear existing data
            teacherRepository.deleteAll();
            courseRepository.deleteAll(); 
            appUserRepository.deleteAll();
            studentRepository.deleteAll();
            registrationRepository.deleteAll();

            // Create some AppUsers
            AppUser user1 = new AppUser("student", "$2y$10$iV9F98uHxUUTvAhqDBOTqu5YOOweujT1mTGN/ukLoBvIAaPErUiZ2", "student@example.com", AppUser.Role.STUDENT);
            AppUser user3 = new AppUser("student1", "$2y$10$iV9F98uHxUUTvAhqDBOTqu5YOOweujT1mTGN/ukLoBvIAaPErUiZ2", "student1@example.com", AppUser.Role.STUDENT);
            AppUser user4 = new AppUser("student2", "$2y$10$iV9F98uHxUUTvAhqDBOTqu5YOOweujT1mTGN/ukLoBvIAaPErUiZ2", "student2@example.com", AppUser.Role.STUDENT);
            AppUser user2 = new AppUser("admin", "$2y$10$Mw52MWULJMZJozyNAYfJcuf4P0.OcNZw2TijEXAUhoYaWKysSEWba", "admin@example.com", AppUser.Role.ADMIN);
            appUserRepository.save(user1);
            appUserRepository.save(user2);
            appUserRepository.save(user3);
            appUserRepository.save(user4);

            // Create Students
            Student student1 = new Student(user1, "John Doe", "0912345678", "2000-01-01", List.of());
            Student student2 = new Student(user3, "Jane Dortle", "0912343570", "2020-01-09", List.of());
            Student student3 = new Student(user4, "Ma Drene", "0912334278", "2012-06-02", List.of());
            studentRepository.save(student1);
            studentRepository.save(student2);
            studentRepository.save(student3);

            // Create and save AppUsers
            AppUser appUser1 = new AppUser("teacher1", "$2y$10$.tmVrm7FO2RsF/IKNPMBcOKEGleIvBxNiIyqgopTZLC/tYd9qeSou", "johndoe@example.com", AppUser.Role.TEACHER);
            appUserRepository.save(appUser1);
            Teacher teacher1 = new Teacher(appUser1, "John Doe", "0912345678", List.of());
            teacherRepository.save(teacher1);
    
            AppUser appUser2 = new AppUser("teacher2", "passwordHash2", "janesmith@example.com", AppUser.Role.TEACHER);
            appUserRepository.save(appUser2);
            Teacher teacher2 = new Teacher(appUser2, "Jane Smith", "0874563231", List.of());
            teacherRepository.save(teacher2);
    
            AppUser appUser3 = new AppUser("teacher3", "passwordHash3", "emilydavis@example.com", AppUser.Role.TEACHER);
            appUserRepository.save(appUser3);
            Teacher teacher3 = new Teacher(appUser3, "Emily Davis", "0128349233", List.of());
            teacherRepository.save(teacher3);

            AppUser appUser4 = new AppUser("teacher4", "passwordHash4", "tc4@example.com", AppUser.Role.TEACHER);
            appUserRepository.save(appUser4);
    
            // Create and save courses with teachers
            Course course1 = new Course("Swimming 101", "Beginner swimming course", Course.Level.BEGINNER, 20, teacher1);
            courseRepository.save(course1);
    
            Course course2 = new Course("Advanced Swimming Techniques", "Advanced techniques for swimmers", Course.Level.ADVANCED, 15, teacher2);
            courseRepository.save(course2);
    
            Course course3 = new Course("Water Safety", "Learning water safety for all levels", Course.Level.INTERMEDIATE, 25, teacher1);
            courseRepository.save(course3);
    
            Course course4 = new Course("Competitive Swimming", "Course for competitive swimmers", Course.Level.ADVANCED, 10, teacher3);
            courseRepository.save(course4);
    
            Course course5 = new Course("Childrens' Swimming", "Swimming for children 5-12 years", Course.Level.BEGINNER, 30, teacher2);
            courseRepository.save(course5);

            // Create and save registrations
            Registration registration1 = new Registration(student1, course4);
            Registration registration2 = new Registration(student1, course2);
            Registration registration3 = new Registration(student1, course3);
            Registration registration4 = new Registration(student2, course3);
            Registration registration5 = new Registration(student3, course3);
            Registration registration6 = new Registration(student3, course1);
            registrationRepository.save(registration1);
            registrationRepository.save(registration2);
            registrationRepository.save(registration3);
            registrationRepository.save(registration4);
            registrationRepository.save(registration5);
            registrationRepository.save(registration6);

        };
    }  
}
