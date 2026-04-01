package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Service.PatientService;
import com.example.demo.dto.AppointmentDTO;
import com.example.demo.dto.PatientDTO;

@RestController
@RequestMapping("/patients")
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/profile/{username}")
    public PatientDTO getPatientProfile(@PathVariable String username) {
        return patientService.getPatientByUsername(username);
    }

    @PutMapping("/{id}")
    public PatientDTO updatePatientProfile(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        return patientService.updatePatientProfile(id, patientDTO);
    }

    @PostMapping("/{patientId}/appointments/{doctorId}")
    public AppointmentDTO bookAppointment(@PathVariable Long patientId, @PathVariable Long doctorId, @RequestBody AppointmentDTO appointmentDTO) {
        return patientService.bookAppointment(patientId, doctorId, appointmentDTO);
    }

    @GetMapping("/{patientId}/appointments/upcoming")
    public List<AppointmentDTO> viewUpcomingAppointments(@PathVariable Long patientId) {
        return patientService.viewUpcomingAppointments(patientId);
    }

    @PutMapping("/appointments/{appointmentId}/reschedule")
    public AppointmentDTO rescheduleAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentDTO appointmentDTO) {
        return patientService.rescheduleAppointment(appointmentId, appointmentDTO);
    }

    @DeleteMapping("/appointments/{appointmentId}")
    public String cancelAppointment(@PathVariable Long appointmentId) {
        patientService.cancelAppointment(appointmentId);
        return "Appointment Cancelled Successfully";
    }

    @GetMapping("/{patientId}/appointments/completed")
    public List<AppointmentDTO> viewCompletedAppointments(@PathVariable Long patientId) {
        return patientService.viewCompletedAppointments(patientId);
    }
}