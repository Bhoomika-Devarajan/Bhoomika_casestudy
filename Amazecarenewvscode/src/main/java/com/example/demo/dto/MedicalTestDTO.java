package com.example.demo.dto;

public class MedicalTestDTO {

    private Long id;
    private String testName;
    private String description;

    public MedicalTestDTO() {}

    public MedicalTestDTO(Long id, String testName, String description) {
        this.id = id;
        this.testName = testName;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "MedicalTestDTO [id=" + id + ", testName=" + testName + ", description=" + description + "]";
    }
}
