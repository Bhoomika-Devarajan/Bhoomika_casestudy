package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.Service.DoctorService;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.AppointmentStatus;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.MedicalRecord;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.MedicalRecordRepository;
import com.example.demo.dto.*;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class DoctorServiceTest {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Test
    void testViewDoctorAppointments() {
        List<AppointmentDTO> appointments = doctorService.viewDoctorAppointments(1L);
        assertNotNull(appointments);
    }

    @Test
    void testGetUpcomingAppointments() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDate.now().plusDays(2));
        appointment.setStatus(AppointmentStatus.REQUESTED);
        appointmentRepository.save(appointment);

        List<AppointmentDTO> upcoming = doctorService.getUpcomingAppointments(1L);
        assertNotNull(upcoming);
    }

    @Test
    void testGetCompletedAppointments() {
        Doctor doctor = new Doctor();
        doctor.setName("Dr Completed Test");
        doctor = doctorRepository.save(doctor);

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        List<AppointmentDTO> completed = doctorService.getCompletedAppointments(doctor.getId());
        assertFalse(completed.isEmpty());
    }

    @Test
    void testConfirmAppointment() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setStatus(AppointmentStatus.REQUESTED);
        Appointment saved = appointmentRepository.save(appointment);

        AppointmentDTO confirmed = doctorService.confirmAppointment(saved.getId());
        assertEquals(AppointmentStatus.CONFIRMED, confirmed.getStatus());
    }

    @Test
    void testRejectAppointment() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setStatus(AppointmentStatus.REQUESTED);
        Appointment saved = appointmentRepository.save(appointment);

        AppointmentDTO rejected = doctorService.rejectAppointment(saved.getId());
        assertEquals(AppointmentStatus.REJECTED, rejected.getStatus());
    }

    @Test
    void testCompleteAppointment() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment saved = appointmentRepository.save(appointment);

        AppointmentDTO completed = doctorService.completeAppointment(saved.getId());
        assertEquals(AppointmentStatus.COMPLETED, completed.getStatus());
    }

    @Test
    public void testAddConsultationDetails() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDate.now());
        Appointment savedAppointment = appointmentRepository.save(appointment);

        MedicalRecordDTO recordDTO = new MedicalRecordDTO();
        recordDTO.setDiagnosis("Common Cold");
        recordDTO.setCurrentSymptoms("Cough, Sore throat");
        recordDTO.setTreatmentPlan("Rest and hydration");
        
        // Add a nested prescription
        PrescriptionDTO p1 = new PrescriptionDTO();
        p1.setMedicineName("Amoxicillin");
        p1.setMorning(1);
        p1.setAfternoon(0);
        p1.setEvening(1);
        p1.setFoodInstruction(com.example.demo.entity.FoodInstruction.AF);
        p1.setDurationDays(7);
        
        recordDTO.setPrescriptions(java.util.Arrays.asList(p1));

        MedicalRecordDTO result = doctorService.addConsultationDetails(savedAppointment.getId(), recordDTO);
        
        assertNotNull(result);
        assertEquals("Common Cold", result.getDiagnosis());
        assertNotNull(result.getPrescriptions());
        assertEquals(1, result.getPrescriptions().size());
        assertEquals("Amoxicillin", result.getPrescriptions().get(0).getMedicineName());
    }

    @Test
    public void testAddPrescription() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        MedicalRecord record = new MedicalRecord();
        record.setAppointment(savedAppointment);
        MedicalRecord savedRecord = medicalRecordRepository.save(record);

        PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
        prescriptionDTO.setMedicineName("Paracetamol");

        PrescriptionDTO result = doctorService.addPrescription(savedRecord.getId(), prescriptionDTO);
        assertNotNull(result);
    }
  
    @Test
    void testViewPatientHistory() {
        List<MedicalRecordDTO> records = doctorService.viewPatientHistory(1L);
        assertNotNull(records);
    }
}