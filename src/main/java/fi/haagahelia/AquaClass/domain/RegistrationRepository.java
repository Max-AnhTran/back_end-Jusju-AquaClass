package fi.haagahelia.AquaClass.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends CrudRepository<Registration, Long> {
    Optional<Registration> findById(Long id);
    
    List<Registration> findByCourseId(Long courseId);

    List<Registration> findByStudentId(Long studentId);

    Optional<Registration> findByStudentIdAndCourseId(Long studentId, Long courseId);
}