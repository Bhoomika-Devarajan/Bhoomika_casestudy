package com.example.demo.dto;

import com.example.demo.entity.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientDTO {

    private Long id;
    private UserDTO user;
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String mobileNumber;
    private String address;
    private LocalDateTime createdAt;

    public PatientDTO() {}

    public PatientDTO(Long id, UserDTO user, String fullName, LocalDate dateOfBirth, Gender gender, String mobileNumber, String address, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "PatientDTO [id=" + id + ", user=" + user + ", fullName=" + fullName + ", dateOfBirth=" + dateOfBirth
                + ", gender=" + gender + ", mobileNumber=" + mobileNumber + ", address=" + address + ", createdAt="
                + createdAt + "]";
    }
}
