package fi.haagahelia.AquaClass.dto;

import fi.haagahelia.AquaClass.domain.AppUser;
import fi.haagahelia.AquaClass.domain.AppUserRepository;
import fi.haagahelia.AquaClass.domain.Teacher;
import fi.haagahelia.AquaClass.domain.TeacherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    public AppUserService(AppUserRepository appUserRepository, TeacherRepository teacherRepository) {
        this.appUserRepository = appUserRepository;
        this.teacherRepository = teacherRepository;
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Get all AppUsers
    public List<AppUserDTO> getAllUsers() {
        return ((List<AppUser>) appUserRepository.findAll())
                .stream()
                .map(AppUserMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get AppUser by ID
    public Optional<AppUserDTO> getUserById(Long id) {
        Optional<AppUser> appUserOptional = appUserRepository.findById(id);
        return appUserOptional.map(AppUserMapper::toDTO);
    }

    // Get AppUser by username
    public Optional<AppUserDTO> getUserByUsername(String username) {
        Optional<AppUser> appUserOptional = appUserRepository.findByUsername(username);
        return appUserOptional.map(AppUserMapper::toDTO);
    }

    // Get AppUser without Teacher
    public List<AppUserDTO> getTeacherUsersWithoutTeacher() {
        // Lấy các Teacher mà không có AppUser liên kết
        List<Teacher> teachersWithAppUser = teacherRepository.findByUserIsNotNull();

        // Lấy danh sách tất cả AppUser
        List<AppUser> teacherUsers = appUserRepository.findByRole(AppUser.Role.TEACHER);

        // Lọc ra những AppUser không có Teacher liên kết
        List<AppUserDTO> teacherUsersWithoutTeacher = teacherUsers.stream()
                .filter(appUser -> teachersWithAppUser.stream()
                        .noneMatch(teacher -> teacher.getUser() != null && teacher.getUser().equals(appUser)))
                .map(AppUserMapper::toDTO)
                .collect(Collectors.toList());

        return teacherUsersWithoutTeacher;
    }

    // Create new AppUser
    public AppUserDTO createAppUser(AppUserDTO appUserDTO) {
        AppUser appUser = AppUserMapper.toEntity(appUserDTO);
        appUser.setPasswordHash(passwordEncoder.encode(appUserDTO.getPassword()));
        AppUser savedAppUser = appUserRepository.save(appUser);
        return AppUserMapper.toDTO(savedAppUser);
    }

    // Update an existing AppUser
    public AppUserDTO updateAppUser(Long id, AppUserDTO appUserDTO) {
        Optional<AppUser> appUserOptional = appUserRepository.findById(id);

        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            appUser.setUsername(appUserDTO.getUsername());
            appUser.setEmail(appUserDTO.getEmail());
            appUser.setRole(appUserDTO.getRole());

            // Update password
            if (appUserDTO.getPassword() != null && !appUserDTO.getPassword().isEmpty()) {
                appUser.setPasswordHash(passwordEncoder.encode(appUserDTO.getPassword()));
            }

            AppUser updatedAppUser = appUserRepository.save(appUser);
            return AppUserMapper.toDTO(updatedAppUser);
        }

        return null;
    }

    // Delete AppUser by ID
    public boolean deleteAppUser(Long id) {
        if (appUserRepository.existsById(id)) {
            appUserRepository.deleteById(id);
            return true;
        }
        return false; // Or handle differently
    }
}