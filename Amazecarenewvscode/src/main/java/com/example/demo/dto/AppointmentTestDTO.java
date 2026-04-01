package com.example.demo.dto;

public class AppointmentTestDTO {

    private Long id;
    private MedicalRecordDTO medicalRecord;
    private MedicalTestDTO medicalTest;

    public AppointmentTestDTO() {}

    public AppointmentTestDTO(Long id, MedicalRecordDTO medicalRecord, MedicalTestDTO medicalTest) {
        this.id = id;
        this.medicalRecord = medicalRecord;
        this.medicalTest = medicalTest;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MedicalRecordDTO getMedicalRecord() { return medicalRecord; }
    public void setMedicalRecord(MedicalRecordDTO medicalRecord) { this.medicalRecord = medicalRecord; }

    public MedicalTestDTO getMedicalTest() { return medicalTest; }
    public void setMedicalTest(MedicalTestDTO medicalTest) { this.medicalTest = medicalTest; }

    @Override
    public String toString() {
        return "AppointmentTestDTO [id=" + id + ", medicalRecord=" + medicalRecord + ", medicalTest=" + medicalTest + "]";
    }
}
