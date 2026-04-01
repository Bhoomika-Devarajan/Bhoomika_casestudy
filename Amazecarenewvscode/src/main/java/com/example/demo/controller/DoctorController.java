package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Service.DoctorService;
import com.example.demo.dto.AppointmentDTO;
import com.example.demo.dto.DoctorDTO;
import com.example.demo.dto.MedicalRecordDTO;
import com.example.demo.dto.PrescriptionDTO;

@RestController
@RequestMapping("/doctor")
@CrossOrigin(origins = "http://localhost:3000")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/profile/{username}")
    public DoctorDTO getDoctorProfile(@PathVariable String username) {
        return doctorService.getDoctorByUsername(username);
    }

    @GetMapping("/{doctorId}/appointments")
    public List<AppointmentDTO> viewAppointments(@PathVariable Long doctorId) {
        return doctorService.viewDoctorAppointments(doctorId);
    }

    @GetMapping("/{doctorId}/upcoming")
    public List<AppointmentDTO> viewUpcomingAppointments(@PathVariable Long doctorId) {
        return doctorService.getUpcomingAppointments(doctorId);
    }

    @GetMapping("/{doctorId}/completed")
    public List<AppointmentDTO> viewCompletedAppointments(@PathVariable Long doctorId) {
        return doctorService.getCompletedAppointments(doctorId);
    }

    @PutMapping("/appointment/{appointmentId}/confirm")
    public AppointmentDTO confirmAppointment(@PathVariable Long appointmentId) {
        return doctorService.confirmAppointment(appointmentId);
    }

    @PutMapping("/appointment/{appointmentId}/reject")
    public AppointmentDTO rejectAppointment(@PathVariable Long appointmentId) {
        return doctorService.rejectAppointment(appointmentId);
    }

    @PutMapping("/appointment/{appointmentId}/complete")
    public AppointmentDTO completeAppointment(@PathVariable Long appointmentId) {
        return doctorService.completeAppointment(appointmentId);
    }

    @PostMapping("/appointment/{appointmentId}/consultation")
    public MedicalRecordDTO addConsultationDetails(@PathVariable Long appointmentId, @RequestBody MedicalRecordDTO recordDTO) {
        return doctorService.addConsultationDetails(appointmentId, recordDTO);
    }

    @PostMapping("/medicalrecord/{recordId}/prescription")
    public PrescriptionDTO addPrescription(@PathVariable Long recordId, @RequestBody PrescriptionDTO prescriptionDTO) {
        return doctorService.addPrescription(recordId, prescriptionDTO);
    }

    @GetMapping("/patient/{patientId}/history")
    public List<MedicalRecordDTO> viewPatientHistory(@PathVariable Long patientId) {
        return doctorService.viewPatientHistory(patientId);
    }
}