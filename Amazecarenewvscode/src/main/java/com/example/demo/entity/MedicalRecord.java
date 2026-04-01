package com.example.demo.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private String currentSymptoms;
    private String physicalExamination;
    private String treatmentPlan;
    private String recommendedTests;
    private String diagnosis;
    private java.time.LocalDateTime createdAt;

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<Prescription> prescriptions = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<AppointmentTest> appointmentTests = new java.util.ArrayList<>();

    public MedicalRecord() {
    }

    public MedicalRecord(Long id, Appointment appointment, String currentSymptoms,
                         String physicalExamination, String treatmentPlan,
                         String recommendedTests, String diagnosis,
                         LocalDateTime createdAt) {
        this.id = id;
        this.appointment = appointment;
        this.currentSymptoms = currentSymptoms;
        this.physicalExamination = physicalExamination;
        this.treatmentPlan = treatmentPlan;
        this.recommendedTests = recommendedTests;
        this.diagnosis = diagnosis;
        this.createdAt = createdAt;
    }
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }

    public String getCurrentSymptoms() { return currentSymptoms; }
    public void setCurrentSymptoms(String currentSymptoms) { this.currentSymptoms = currentSymptoms; }

    public String getPhysicalExamination() { return physicalExamination; }
    public void setPhysicalExamination(String physicalExamination) { this.physicalExamination = physicalExamination; }

    public String getTreatmentPlan() { return treatmentPlan; }
    public void setTreatmentPlan(String treatmentPlan) { this.treatmentPlan = treatmentPlan; }

    public String getRecommendedTests() { return recommendedTests; }
    public void setRecommendedTests(String recommendedTests) { this.recommendedTests = recommendedTests; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }

    public java.util.List<Prescription> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(java.util.List<Prescription> prescriptions) { this.prescriptions = prescriptions; }

    public java.util.List<AppointmentTest> getAppointmentTests() { return appointmentTests; }
    public void setAppointmentTests(java.util.List<AppointmentTest> appointmentTests) { this.appointmentTests = appointmentTests; }

	@Override
	public String toString() {
		return "MedicalRecord [id=" + id + ", appointment=" + appointment + ", currentSymptoms=" + currentSymptoms
				+ ", physicalExamination=" + physicalExamination + ", treatmentPlan=" + treatmentPlan
				+ ", recommendedTests=" + recommendedTests + ", diagnosis=" + diagnosis + ", createdAt=" + createdAt
				+ ", prescriptionsCount=" + (prescriptions != null ? prescriptions.size() : 0) 
                + ", testsCount=" + (appointmentTests != null ? appointmentTests.size() : 0) + "]";
	}
    
}