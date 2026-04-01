package com.example.demo.Service;

import java.util.List;
import com.example.demo.dto.AppointmentDTO;
import com.example.demo.dto.DoctorDTO;
import com.example.demo.dto.PatientDTO;

public interface AdminService {
    // Doctor
    DoctorDTO addDoctor(DoctorDTO doctor);
    DoctorDTO updateDoctor(Long doctorId, DoctorDTO doctor);
    void deleteDoctor(Long doctorId);
    List<DoctorDTO> getAllDoctors();

    // Patient 
    PatientDTO addPatient(PatientDTO patient);
    PatientDTO updatePatient(Long patientId, PatientDTO patient);
    void deletePatient(Long patientId);
    List<PatientDTO> getAllPatients();

    // Appointment 
    List<AppointmentDTO> getAllAppointments();
    AppointmentDTO rescheduleAppointment(Long appointmentId, AppointmentDTO appointment);
}
