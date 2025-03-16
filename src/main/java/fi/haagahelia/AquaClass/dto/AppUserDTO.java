package fi.haagahelia.AquaClass.dto;

import fi.haagahelia.AquaClass.domain.AppUser.Role;

public class AppUserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Role role;

    public AppUserDTO() {}

    public AppUserDTO(Long id, String name, String password, String email, Role role) {
        this.id = id;
        this.username = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
