package fi.haagahelia.AquaClass.dto;

public class TeacherDTO {
    private Long id;
    private String fullName;
    private String phone;
    private Long appUserId;

    // Constructors
    public TeacherDTO() {
    }

    public TeacherDTO(Long id, String fullName, String phone, Long appUserId) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.appUserId = appUserId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }
}