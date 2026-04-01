package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.Service.AppointmentService;
import com.example.demo.entity.AppointmentStatus;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.dto.AppointmentDTO;
import com.example.demo.mapper.DTOMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class AppointmentServiceTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void testCreateAppointment() {
        Doctor doctor = new Doctor();
        doctor.setName("Dr Test");
        doctor = doctorRepository.save(doctor);

        Patient patient = new Patient();
        patient.setFullName("Test Patient");
        patient = patientRepository.save(patient);

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setDoctor(DTOMapper.toDoctorDTO(doctor));
        appointmentDTO.setPatient(DTOMapper.toPatientDTO(patient));
        appointmentDTO.setAppointmentDate(LocalDate.now());
        appointmentDTO.setAppointmentTime(LocalTime.of(10, 30));
        appointmentDTO.setVisitType("IN_PERSON");
        appointmentDTO.setSymptoms("Flu symptoms");
       
        AppointmentDTO saved = appointmentService.createAppointment(appointmentDTO);

        assertNotNull(saved);
        assertEquals("IN_PERSON", saved.getVisitType());
        assertEquals(LocalTime.of(10, 30), saved.getAppointmentTime());
    }

    @Test
    void testUpdateAppointmentStatus() {
        Doctor doctor = doctorRepository.save(new Doctor());
        Patient patient = patientRepository.save(new Patient());

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setDoctor(DTOMapper.toDoctorDTO(doctor));
        appointmentDTO.setPatient(DTOMapper.toPatientDTO(patient));
        appointmentDTO.setAppointmentDate(LocalDate.now());
        appointmentDTO.setAppointmentTime(LocalTime.now());
        appointmentDTO.setStatus(AppointmentStatus.REJECTED);

        AppointmentDTO saved = appointmentService.createAppointment(appointmentDTO);

        AppointmentDTO updated = appointmentService.updateAppointmentStatus(saved.getId(), "CONFIRMED");

        assertEquals(AppointmentStatus.CONFIRMED, updated.getStatus());
    }

    @Test
    void testGetAppointmentsByDoctor() {
        Doctor doctor = new Doctor();
        doctor.setName("Dr Filter");
        doctor = doctorRepository.save(doctor);

        Patient patient = patientRepository.save(new Patient());

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setDoctor(DTOMapper.toDoctorDTO(doctor));
        appointmentDTO.setPatient(DTOMapper.toPatientDTO(patient));
        
        appointmentService.createAppointment(appointmentDTO);

        List<AppointmentDTO> list = appointmentService.getAppointmentsByDoctor(doctor.getId());

        assertEquals(doctor.getId(), list.get(0).getDoctor().getId());
    }

    @Test
    void testGetAppointmentsByPatient() {
        Doctor doctor = doctorRepository.save(new Doctor());

        Patient patient = new Patient();
        patient.setFullName("Patient Filter");
        patient = patientRepository.save(patient);

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setDoctor(DTOMapper.toDoctorDTO(doctor));
        appointmentDTO.setPatient(DTOMapper.toPatientDTO(patient));
        appointmentDTO.setAppointmentDate(LocalDate.now());
        appointmentDTO.setAppointmentTime(LocalTime.now());
        appointmentDTO.setStatus(AppointmentStatus.REJECTED);

        appointmentService.createAppointment(appointmentDTO);

        List<AppointmentDTO> list = appointmentService.getAppointmentsByPatient(patient.getId());
      
        assertEquals(patient.getId(), list.get(0).getPatient().getId());
    }
}