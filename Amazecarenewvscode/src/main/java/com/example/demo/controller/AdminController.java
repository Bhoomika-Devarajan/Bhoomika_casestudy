package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Service.AdminService;
import com.example.demo.dto.DoctorDTO;
import com.example.demo.dto.PatientDTO;
import com.example.demo.dto.AppointmentDTO;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/doctor")
    public DoctorDTO addDoctor(@RequestBody DoctorDTO doctorDTO) {
        return adminService.addDoctor(doctorDTO);
    }

    @PutMapping("/doctor/{id}")
    public DoctorDTO updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO) {
        return adminService.updateDoctor(id, doctorDTO);
    }

    @DeleteMapping("/doctor/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        adminService.deleteDoctor(id);
    }

    @GetMapping("/doctors")
    public List<DoctorDTO> getAllDoctors() {
        return adminService.getAllDoctors();
    }

    @PostMapping("/patient")
    public PatientDTO addPatient(@RequestBody PatientDTO patientDTO) {
        return adminService.addPatient(patientDTO);
    }

    @PutMapping("/patient/{id}")
    public PatientDTO updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        return adminService.updatePatient(id, patientDTO);
    }

    @DeleteMapping("/patient/{id}")
    public void deletePatient(@PathVariable Long id) {
        adminService.deletePatient(id);
    }

    @GetMapping("/patients")
    public List<PatientDTO> getAllPatients() {
        return adminService.getAllPatients();
    }

    @GetMapping("/appointments")
    public List<AppointmentDTO> getAllAppointments() {
        return adminService.getAllAppointments();
    }

    @PutMapping("/appointment/{id}")
    public AppointmentDTO rescheduleAppointment(@PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO) {
        return adminService.rescheduleAppointment(id, appointmentDTO);
    }
}