package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.Service.AdminService;
import com.example.demo.dto.AppointmentDTO;
import com.example.demo.dto.DoctorDTO;
import com.example.demo.dto.PatientDTO;
import com.example.demo.exception.ResourceNotFoundException;

@SpringBootTest(classes = AmazecarenewApplication.class)
class AdminServiceImplTest {

    @Autowired
    AdminService adminService;

    @Test
    void testAddDoctor() {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setName("Dr A");
        doctorDTO.setSpecialty("ENT");

        DoctorDTO saved = adminService.addDoctor(doctorDTO);

        assertNotNull(saved);
        assertEquals("Dr A", saved.getName());
    }

    @Test
    void testUpdateDoctor() {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setName("Dr B");
        DoctorDTO saved = adminService.addDoctor(doctorDTO);

        DoctorDTO updated = new DoctorDTO();
        updated.setName("Dr Updated");

        DoctorDTO result = adminService.updateDoctor(saved.getId(), updated);

        assertEquals("Dr Updated", result.getName());
    }

    @Test
    void testDeleteDoctor() {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setName("Dr Delete");
        DoctorDTO saved = adminService.addDoctor(doctorDTO);

        adminService.deleteDoctor(saved.getId());

        assertThrows(ResourceNotFoundException.class, () -> adminService.updateDoctor(saved.getId(), doctorDTO));
    }

    @Test
    void testGetAllDoctors() {
        List<DoctorDTO> list = adminService.getAllDoctors();
        assertNotNull(list);
    }

    @Test
    void testAddPatient() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFullName("John");

        PatientDTO saved = adminService.addPatient(patientDTO);
        assertEquals("John", saved.getFullName());
    }

    @Test
    void testUpdatePatient() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFullName("Old");
        PatientDTO saved = adminService.addPatient(patientDTO);

        PatientDTO updated = new PatientDTO();
        updated.setFullName("New");

        PatientDTO result = adminService.updatePatient(saved.getId(), updated);
        assertEquals("New", result.getFullName());
    }

    @Test
    void testDeletePatient() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFullName("Temp");
        PatientDTO saved = adminService.addPatient(patientDTO);

        adminService.deletePatient(saved.getId());

        assertThrows(ResourceNotFoundException.class, () -> adminService.updatePatient(saved.getId(), patientDTO));
    }

    @Test
    void testGetAllPatients() {
        List<PatientDTO> list = adminService.getAllPatients();
        assertNotNull(list);
    }

    @Test
    void testGetAllAppointments() {
        List<AppointmentDTO> list = adminService.getAllAppointments();
        assertNotNull(list);
    }

    @Test
    void testRescheduleAppointment() {
        List<AppointmentDTO> list = adminService.getAllAppointments();
        AppointmentDTO saved = list.isEmpty() ? null : list.get(0);

        if (saved != null) {
            AppointmentDTO update = new AppointmentDTO();
            update.setAppointmentDate(LocalDate.now().plusDays(1));
            update.setAppointmentTime(LocalTime.now().plusHours(2));

            AppointmentDTO result = adminService.rescheduleAppointment(saved.getId(), update);
            assertNotNull(result);
        }
    }
}
