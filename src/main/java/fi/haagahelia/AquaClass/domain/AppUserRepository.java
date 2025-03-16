package fi.haagahelia.AquaClass.domain;

import java.util.Optional;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    List<AppUser> findByRole(AppUser.Role role);
}