package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.entity.Patient;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.User;
import com.example.demo.entity.Role;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.AppointmentStatus;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.MedicalRecordRepository;
import com.example.demo.dto.PatientDTO;
import com.example.demo.dto.AppointmentDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.mapper.DTOMapper;
import com.example.demo.exception.*;

@Service
public class PatientServiceImpl implements PatientService {

    private static final Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Override
    @Transactional
    public PatientDTO getPatientByUsername(String username) {
        logger.info("Fetching patient profile for: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (user.getRole() != Role.PATIENT) {
            throw new BadRequestException("User " + username + " is not a patient.");
        }

        // HEALING LOGIC: Find any patient record with this user OR this name
        Optional<Patient> linked = patientRepository.findByUserUsername(username);
        Optional<Patient> byName = patientRepository.findByFullName(username);

        Patient finalPatient;

        if (byName.isPresent()) {
            // Found a record with the same name (likely historical). Hijack it!
            finalPatient = byName.get();
            if (finalPatient.getUser() == null || !finalPatient.getUser().getUsername().equals(username)) {
                logger.info("HEALING: Re-linking existing patient record {} to User {}", finalPatient.getId(),
                        username);
                finalPatient.setUser(user);
                finalPatient = patientRepository.save(finalPatient);
            }

            // If there's a duplicate skeleton profile (linked but different ID), we might
            // want to consolidate
            // but for now, just ensure the name-matched one is the one we use.
        } else if (linked.isPresent()) {
            finalPatient = linked.get();
        } else {
            logger.info("Creating new skeleton patient for {}", username);
            finalPatient = new Patient();
            finalPatient.setUser(user);
            finalPatient.setFullName(username);
            finalPatient = patientRepository.save(finalPatient);
        }

        return DTOMapper.toPatientDTO(finalPatient);
    }

    @Override
    public PatientDTO updatePatientProfile(Long id, PatientDTO patientDTO) {
        logger.info("Updating patient profile with id: {}", id);
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        existing.setFullName(patientDTO.getFullName());
        existing.setDateOfBirth(patientDTO.getDateOfBirth());
        existing.setGender(patientDTO.getGender());
        existing.setMobileNumber(patientDTO.getMobileNumber());
        existing.setAddress(patientDTO.getAddress());
        Patient updatedPatient = patientRepository.save(existing);
        return DTOMapper.toPatientDTO(updatedPatient);
    }

    @Override
    @Transactional
    public AppointmentDTO bookAppointment(Long patientId, Long doctorId, AppointmentDTO appointmentDTO) {
        logger.info("Booking appointment for patientId: {} with doctorId: {}", patientId, doctorId);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        Appointment appointment = DTOMapper.toAppointmentEntity(appointmentDTO);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        // Fix for 500 errors: Ensure required fields have defaults
        if (appointment.getVisitType() == null)
            appointment.setVisitType("Inperson");
        if (appointment.getAppointmentTime() == null)
            appointment.setAppointmentTime(java.time.LocalTime.of(10, 0));

        appointment.setStatus(AppointmentStatus.REQUESTED);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return DTOMapper.toAppointmentDTO(savedAppointment);
    }

    @Override
    public List<AppointmentDTO> viewUpcomingAppointments(Long patientId) {
        logger.info("Fetching upcoming appointments for patientId: {}", patientId);
        List<Appointment> appointments = appointmentRepository.findByPatientIdAndStatusNotIn(
                patientId, java.util.Arrays.asList(AppointmentStatus.COMPLETED, AppointmentStatus.CANCELLED));
        return DTOMapper.toDTOList(appointments, DTOMapper::toAppointmentDTO);
    }

    @Override
    public AppointmentDTO rescheduleAppointment(Long appointmentId, AppointmentDTO appointmentDTO) {
        Appointment existing = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        existing.setAppointmentDate(appointmentDTO.getAppointmentDate());
        Appointment updated = appointmentRepository.save(existing);
        return DTOMapper.toAppointmentDTO(updated);
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    public List<AppointmentDTO> viewCompletedAppointments(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientIdAndStatus(patientId,
                AppointmentStatus.COMPLETED);
        return DTOMapper.toDTOList(appointments, DTOMapper::toAppointmentDTO);
    }
}