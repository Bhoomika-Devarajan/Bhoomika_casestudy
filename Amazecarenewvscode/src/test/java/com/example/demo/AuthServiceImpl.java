package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Service.AuthService;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.*;

@SpringBootTest
@Transactional
class AuthServiceImpl {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentTestRepository appointmentTestRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private MedicalTestRepository medicalTestRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @BeforeEach
    void setUp() {
        prescriptionRepository.deleteAllInBatch();
        medicalRecordRepository.deleteAllInBatch();
        medicalTestRepository.deleteAllInBatch();
        appointmentTestRepository.deleteAllInBatch();
        appointmentRepository.deleteAllInBatch();
        patientRepository.deleteAllInBatch();
        doctorRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    void testRegisterPatient() {
        String username = "patient1";
        String password = "pass123";

        String result = authService.registerPatient(username, password);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testRegisterDoctor() {
        String username = "doctor1";
        String password = "docpass";

        String result = authService.registerDoctor(username, password);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

   
    @Test
    void testLoginSuccess() {
        String username = "loginUser";
        String password = "mypassword";

        authService.registerPatient(username, password);

        String result = authService.login(username, password);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

 
}