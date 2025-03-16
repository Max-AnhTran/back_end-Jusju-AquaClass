package fi.haagahelia.AquaClass.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
    Student findByPhone(String phone);

    Optional<Student> findByUserId(Long id);
}

