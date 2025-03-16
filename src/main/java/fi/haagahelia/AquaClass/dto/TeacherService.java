package fi.haagahelia.AquaClass.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.haagahelia.AquaClass.domain.AppUser;
import fi.haagahelia.AquaClass.domain.AppUserRepository;
import fi.haagahelia.AquaClass.domain.Teacher;
import fi.haagahelia.AquaClass.domain.TeacherRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public TeacherService(TeacherRepository teacherRepository, AppUserRepository appUserRepository) {
        this.teacherRepository = teacherRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<TeacherDTO> getAllTeachers() {
        return ((List<Teacher>) teacherRepository.findAll())
                .stream()
                .map(TeacherMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TeacherDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        return TeacherMapper.toDTO(teacher);
    }

    public TeacherDTO getTeacherByAppUserId (Long appUserId) {
        Teacher teacher = teacherRepository.findByUserId(appUserId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        return TeacherMapper.toDTO(teacher);
    }

    public TeacherDTO saveTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = TeacherMapper.toEntity(teacherDTO);
        Optional<AppUser> appUserOptional = appUserRepository.findById(teacherDTO.getAppUserId());
        appUserOptional.ifPresent(teacher::setUser);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return TeacherMapper.toDTO(savedTeacher);
    }

    public TeacherDTO updateTeacher(Long id, TeacherDTO teacherDTO) {
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        existingTeacher.setFullName(teacherDTO.getFullName());
        existingTeacher.setPhone(teacherDTO.getPhone());

        Teacher updatedTeacher = teacherRepository.save(existingTeacher);
        return TeacherMapper.toDTO(updatedTeacher);
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
}

