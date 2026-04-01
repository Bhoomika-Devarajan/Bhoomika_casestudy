package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.Service.PatientService;
import com.example.demo.entity.*;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.dto.*;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    public void testUpdatePatientProfile() {
        Patient patient = new Patient();
        patient.setFullName("Ravi");
        patient.setMobileNumber("9876543210");
        patient.setAddress("Chennai");
        Patient savedPatient = patientRepository.save(patient);

        PatientDTO updateDTO = new PatientDTO();
        updateDTO.setFullName("Ravi Kumar");
        updateDTO.setMobileNumber("9999999999");
        updateDTO.setAddress("Madurai");

        PatientDTO result = patientService.updatePatientProfile(savedPatient.getId(), updateDTO);

        assertEquals("Ravi Kumar", result.getFullName());
    }

    @Test
    public void testBookAppointment() {
        Patient patient = new Patient();
        patient.setFullName("Ajay");
        patient.setMobileNumber("8888888888");
        patient.setAddress("Salem");
        Patient savedPatient = patientRepository.save(patient);

        Doctor doctor = new Doctor();
        doctor.setName("Dr. Smith");
        doctor.setSpecialty("Cardiology");
        Doctor savedDoctor = doctorRepository.save(doctor);

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setAppointmentDate(LocalDate.now().plusDays(1));

        AppointmentDTO result = patientService.bookAppointment(
                        savedPatient.getId(),
                        savedDoctor.getId(),
                        appointmentDTO);

        assertEquals(AppointmentStatus.REQUESTED, result.getStatus());
    }

    @Test
    public void testViewUpcomingAppointments() {
        Patient patient = new Patient();
        patient.setFullName("John");
        Patient savedPatient = patientRepository.save(patient);

        List<AppointmentDTO> list = patientService.viewUpcomingAppointments(savedPatient.getId());

        assertNotNull(list);
    }

    @Test
    public void testRescheduleAppointment() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDate.now());
        Appointment saved = appointmentRepository.save(appointment);

        AppointmentDTO updateDTO = new AppointmentDTO();
        updateDTO.setAppointmentDate(LocalDate.now().plusDays(3));

        AppointmentDTO result = patientService.rescheduleAppointment(saved.getId(), updateDTO);

        assertEquals(updateDTO.getAppointmentDate(), result.getAppointmentDate());
    }

    @Test
    public void testCancelAppointment() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDate.now());
        Appointment saved = appointmentRepository.save(appointment);

        patientService.cancelAppointment(saved.getId());

        Appointment result = appointmentRepository.findById(saved.getId()).orElse(null);

        assertEquals(AppointmentStatus.CANCELLED, result.getStatus());
    }

    @Test
    public void testViewCompletedAppointments() {
        Patient patient = new Patient();
        patient.setFullName("Arun");
        Patient savedPatient = patientRepository.save(patient);

        List<AppointmentDTO> list = patientService.viewCompletedAppointments(savedPatient.getId());

        assertNotNull(list);
    }
}