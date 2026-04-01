package com.example.demo.Service;

import java.util.List;
import com.example.demo.dto.AppointmentDTO;

public interface AppointmentService {

    AppointmentDTO createAppointment(AppointmentDTO appointment);

    AppointmentDTO updateAppointmentStatus(Long appointmentId, String status);

    List<AppointmentDTO> getAppointmentsByDoctor(Long doctorId);

    List<AppointmentDTO> getAppointmentsByPatient(Long patientId);

}