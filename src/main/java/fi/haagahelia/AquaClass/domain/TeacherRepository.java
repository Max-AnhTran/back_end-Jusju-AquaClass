package fi.haagahelia.AquaClass.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface TeacherRepository extends CrudRepository<Teacher, Long> {
    Teacher findByFullName(String fullName);

    Optional<Teacher> findById(Long id);

    Optional<Teacher> findByUserId(Long userId);

    List<Teacher> findByUserIsNotNull();
} 
