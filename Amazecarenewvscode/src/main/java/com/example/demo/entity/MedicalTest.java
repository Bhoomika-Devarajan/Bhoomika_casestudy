package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class MedicalTest {
    @Id
    private Long id;
    private String testName;
    private String description;

    public MedicalTest() {
    }

    public MedicalTest(Long id, String testName, String description) {
        this.id = id;
        this.testName = testName;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MedicalTest{" +
                "id=" + id +
                ", testName='" + testName + '\'' +
                '}';
    }
}
