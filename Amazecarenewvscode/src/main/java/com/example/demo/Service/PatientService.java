package com.example.demo.Service;

import java.util.List;
import com.example.demo.dto.AppointmentDTO;
import com.example.demo.dto.PatientDTO;

public interface PatientService {

    PatientDTO updatePatientProfile(Long id, PatientDTO patient);

    PatientDTO getPatientByUsername(String username);

    AppointmentDTO bookAppointment(Long patientId, Long doctorId, AppointmentDTO appointment);

    List<AppointmentDTO> viewUpcomingAppointments(Long patientId);

    AppointmentDTO rescheduleAppointment(Long appointmentId, AppointmentDTO appointment);

    void cancelAppointment(Long appointmentId);

    List<AppointmentDTO> viewCompletedAppointments(Long patientId);
}