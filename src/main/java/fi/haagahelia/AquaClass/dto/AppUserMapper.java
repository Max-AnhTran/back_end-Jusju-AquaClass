package fi.haagahelia.AquaClass.dto;

import fi.haagahelia.AquaClass.domain.AppUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AppUserMapper {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static AppUserDTO toDTO(AppUser appUser) {
        return new AppUserDTO(appUser.getId(), appUser.getUsername(), null, appUser.getEmail(),
                appUser.getRole());
    }

    public static AppUser toEntity(AppUserDTO appUserDTO) {
        AppUser appUser = new AppUser();
        appUser.setUsername(appUserDTO.getUsername());

        if (appUserDTO.getPassword() != null && !appUserDTO.getPassword().isEmpty()) {
            appUser.setPasswordHash(passwordEncoder.encode(appUserDTO.getPassword())); // Encrypt password
        }

        appUser.setEmail(appUserDTO.getEmail());
        appUser.setRole(appUserDTO.getRole());
        return appUser;
    }
}