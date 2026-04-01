package com.example.demo.dto;


public class MedicalRecordDTO {

    private Long id;
    private AppointmentDTO appointment;
    private String currentSymptoms;
    private String physicalExamination;
    private String treatmentPlan;
    private String recommendedTests;
    private String diagnosis;
    private java.time.LocalDateTime createdAt;
    private java.util.List<PrescriptionDTO> prescriptions;
    private java.util.List<AppointmentTestDTO> appointmentTests;

    public MedicalRecordDTO() {}

    public MedicalRecordDTO(Long id, AppointmentDTO appointment, String currentSymptoms, String physicalExamination, String treatmentPlan, String recommendedTests, String diagnosis, java.time.LocalDateTime createdAt, java.util.List<PrescriptionDTO> prescriptions, java.util.List<AppointmentTestDTO> appointmentTests) {
        this.id = id;
        this.appointment = appointment;
        this.currentSymptoms = currentSymptoms;
        this.physicalExamination = physicalExamination;
        this.treatmentPlan = treatmentPlan;
        this.recommendedTests = recommendedTests;
        this.diagnosis = diagnosis;
        this.createdAt = createdAt;
        this.prescriptions = prescriptions;
        this.appointmentTests = appointmentTests;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AppointmentDTO getAppointment() { return appointment; }
    public void setAppointment(AppointmentDTO appointment) { this.appointment = appointment; }

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

    public java.util.List<PrescriptionDTO> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(java.util.List<PrescriptionDTO> prescriptions) { this.prescriptions = prescriptions; }

    public java.util.List<AppointmentTestDTO> getAppointmentTests() { return appointmentTests; }
    public void setAppointmentTests(java.util.List<AppointmentTestDTO> appointmentTests) { this.appointmentTests = appointmentTests; }

    @Override
    public String toString() {
        return "MedicalRecordDTO [id=" + id + ", appointment=" + appointment + ", currentSymptoms=" + currentSymptoms
                + ", physicalExamination=" + physicalExamination + ", treatmentPlan=" + treatmentPlan
                + ", recommendedTests=" + recommendedTests + ", diagnosis=" + diagnosis + ", createdAt=" + createdAt
                + "]";
    }
}
