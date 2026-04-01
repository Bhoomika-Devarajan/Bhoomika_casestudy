package com.example.demo.dto;

public class DoctorDTO {

    private Long id;
    private UserDTO user;
    private String name;
    private String specialty;
    private Integer experience;
    private String qualification;
    private String designation;
    private String contactNumber;
    private String email;

    public DoctorDTO() {}

    public DoctorDTO(Long id, UserDTO user, String name, String specialty, Integer experience, String qualification, String designation, String contactNumber, String email) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.specialty = specialty;
        this.experience = experience;
        this.qualification = qualification;
        this.designation = designation;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "DoctorDTO [id=" + id + ", user=" + user + ", name=" + name + ", specialty=" + specialty + ", experience="
                + experience + ", qualification=" + qualification + ", designation=" + designation + ", contactNumber="
                + contactNumber + ", email=" + email + "]";
    }
}
