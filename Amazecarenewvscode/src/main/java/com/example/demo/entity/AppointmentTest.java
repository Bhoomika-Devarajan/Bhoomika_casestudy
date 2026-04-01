package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class AppointmentTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    @ManyToOne
    @JoinColumn(name = "medical_test_id")
    private MedicalTest medicalTest;

    public AppointmentTest() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MedicalRecord getMedicalRecord() {
		return medicalRecord;
	}

	public void setMedicalRecord(MedicalRecord medicalRecord) {
		this.medicalRecord = medicalRecord;
	}

	public MedicalTest getMedicalTest() {
		return medicalTest;
	}

	public void setMedicalTest(MedicalTest medicalTest) {
		this.medicalTest = medicalTest;
	}

	@Override
	public String toString() {
		return "AppointmentTest [id=" + id + ", medicalRecord=" + medicalRecord + ", medicalTest=" + medicalTest + "]";
	}
    
    // getters and setters
}