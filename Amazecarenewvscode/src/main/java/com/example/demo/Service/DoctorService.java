package com.example.demo.Service;

import java.util.List;
import com.example.demo.dto.AppointmentDTO;
import com.example.demo.dto.DoctorDTO;
import com.example.demo.dto.MedicalRecordDTO;
import com.example.demo.dto.PrescriptionDTO;

public interface DoctorService {

    List<AppointmentDTO> viewDoctorAppointments(Long doctorId);

    DoctorDTO getDoctorByUsername(String username);

    List<AppointmentDTO> getUpcomingAppointments(Long doctorId);

    List<AppointmentDTO> getCompletedAppointments(Long doctorId);

    AppointmentDTO confirmAppointment(Long appointmentId);

    AppointmentDTO rejectAppointment(Long appointmentId);

    AppointmentDTO completeAppointment(Long appointmentId);

    MedicalRecordDTO addConsultationDetails(Long appointmentId, MedicalRecordDTO medicalRecord);

    PrescriptionDTO addPrescription(Long medicalRecordId, PrescriptionDTO prescription);

    List<MedicalRecordDTO> viewPatientHistory(Long patientId);
}