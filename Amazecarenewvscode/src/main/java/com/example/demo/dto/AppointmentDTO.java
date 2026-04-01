package com.example.demo.dto;

import com.example.demo.entity.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentDTO {

    private Long id;
    private PatientDTO patient;
    private DoctorDTO doctor;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String symptoms;
    private String visitType;
    private AppointmentStatus status;
    private LocalDateTime createdAt;

    public AppointmentDTO() {}

    public AppointmentDTO(Long id, PatientDTO patient, DoctorDTO doctor, LocalDate appointmentDate, LocalTime appointmentTime, String symptoms, String visitType, AppointmentStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.symptoms = symptoms;
        this.visitType = visitType;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PatientDTO getPatient() { return patient; }
    public void setPatient(PatientDTO patient) { this.patient = patient; }

    public DoctorDTO getDoctor() { return doctor; }
    public void setDoctor(DoctorDTO doctor) { this.doctor = doctor; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public String getVisitType() { return visitType; }
    public void setVisitType(String visitType) { this.visitType = visitType; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "AppointmentDTO [id=" + id + ", patient=" + patient + ", doctor=" + doctor + ", appointmentDate="
                + appointmentDate + ", appointmentTime=" + appointmentTime + ", symptoms=" + symptoms + ", visitType="
                + visitType + ", status=" + status + ", createdAt=" + createdAt + "]";
    }
}
