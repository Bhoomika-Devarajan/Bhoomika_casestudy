package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.Service.MedicalRecordService;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.AppointmentStatus;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.dto.MedicalRecordDTO;
import com.example.demo.mapper.DTOMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class MedicalRecordServiceTest {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    public void testCreateMedicalRecord() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        MedicalRecordDTO recordDTO = new MedicalRecordDTO();
        recordDTO.setDiagnosis("Fever");
        recordDTO.setAppointment(DTOMapper.toAppointmentDTO(savedAppointment));

        MedicalRecordDTO result = medicalRecordService.createMedicalRecord(recordDTO);

        assertNotNull(result);
    }

    @Test
    public void testUpdateMedicalRecord() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        MedicalRecordDTO recordDTO = new MedicalRecordDTO();
        recordDTO.setDiagnosis("Cold");
        recordDTO.setAppointment(DTOMapper.toAppointmentDTO(savedAppointment));

        MedicalRecordDTO savedRecord = medicalRecordService.createMedicalRecord(recordDTO);

        MedicalRecordDTO updateDTO = new MedicalRecordDTO();
        updateDTO.setDiagnosis("Severe Cold");
        updateDTO.setCurrentSymptoms("Cough");

        MedicalRecordDTO result = medicalRecordService.updateMedicalRecord(savedRecord.getId(), updateDTO);

        assertEquals("Severe Cold", result.getDiagnosis());
    }

    @Test
    public void testGetMedicalRecordByAppointment() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        MedicalRecordDTO recordDTO = new MedicalRecordDTO();
        recordDTO.setDiagnosis("Headache");
        recordDTO.setAppointment(DTOMapper.toAppointmentDTO(savedAppointment));

        medicalRecordService.createMedicalRecord(recordDTO);

        MedicalRecordDTO result = medicalRecordService.getMedicalRecordByAppointment(savedAppointment.getId());

        assertNotNull(result);
    }

    @Test
    public void testGetMedicalRecordsByPatient() {
        List<MedicalRecordDTO> records = medicalRecordService.getMedicalRecordsByPatient(1L);
        assertNotNull(records);
    }
}