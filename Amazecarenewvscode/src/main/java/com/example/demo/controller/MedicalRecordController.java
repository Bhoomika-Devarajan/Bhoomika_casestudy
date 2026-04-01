package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Service.MedicalRecordService;
import com.example.demo.dto.MedicalRecordDTO;

@RestController
@RequestMapping("/medical-records")
@CrossOrigin(origins = "http://localhost:3000")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @PostMapping
    public MedicalRecordDTO createMedicalRecord(@RequestBody MedicalRecordDTO recordDTO) {
        return medicalRecordService.createMedicalRecord(recordDTO);
    }

    @PutMapping("/{id}")
    public MedicalRecordDTO updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecordDTO recordDTO) {
        return medicalRecordService.updateMedicalRecord(id, recordDTO);
    }

    @GetMapping("/appointment/{appointmentId}")
    public MedicalRecordDTO getMedicalRecordByAppointment(@PathVariable Long appointmentId) {
        return medicalRecordService.getMedicalRecordByAppointment(appointmentId);
    }

    @GetMapping("/patient/{patientId}")
    public List<MedicalRecordDTO> getMedicalRecordsByPatient(@PathVariable Long patientId) {
        return medicalRecordService.getMedicalRecordsByPatient(patientId);
    }
}