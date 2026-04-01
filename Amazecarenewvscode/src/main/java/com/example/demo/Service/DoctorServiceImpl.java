package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.MedicalRecordRepository;
import com.example.demo.repository.PrescriptionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.AppointmentStatus;
import com.example.demo.entity.MedicalRecord;
import com.example.demo.entity.Prescription;
import com.example.demo.entity.User;
import com.example.demo.entity.Role;
import com.example.demo.dto.DoctorDTO;
import com.example.demo.dto.AppointmentDTO;
import com.example.demo.dto.MedicalRecordDTO;
import com.example.demo.dto.PrescriptionDTO;
import com.example.demo.mapper.DTOMapper;
import com.example.demo.exception.*;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private static final Logger logger =
            LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    MedicalRecordRepository medicalRecordRepository;

    @Autowired
    PrescriptionRepository prescriptionRepository;

    @Override
    public DoctorDTO getDoctorByUsername(String username) {
        logger.info("Fetching doctor by username: {}", username);
        return doctorRepository.findByUserUsername(username)
                .map(DTOMapper::toDoctorDTO)
                .orElseGet(() -> {
                    logger.info("Doctor profile not found for user: {}. Attempting to link by name or create skeleton.", username);
                    User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
                    
                    if (user.getRole() != Role.DOCTOR) {
                        throw new BadRequestException("User " + username + " is not a doctor.");
                    }

                    // Try to find an existing orphan doctor record by name
                    Doctor doctor = doctorRepository.findByName(username)
                            .orElseGet(() -> {
                                logger.info("Creating brand new skeleton doctor for {}", username);
                                Doctor d = new Doctor();
                                d.setName(username);
                                return d;
                            });

                    if (doctor.getUser() == null) {
                        logger.info("Linking existing or new doctor record to user {}", username);
                        doctor.setUser(user);
                        doctor = doctorRepository.save(doctor);
                    }
                    
                    return DTOMapper.toDoctorDTO(doctor);
                });
    }

    @Override
    public List<AppointmentDTO> viewDoctorAppointments(Long doctorId) {
        logger.info("Fetching all appointments for doctor id: {}", doctorId);
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return DTOMapper.toDTOList(appointments, DTOMapper::toAppointmentDTO);
    }

    @Override
    public List<AppointmentDTO> getUpcomingAppointments(Long doctorId) {
        logger.info("Fetching upcoming appointments for doctor id: {}", doctorId);
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndStatusNotIn(
                doctorId, java.util.Arrays.asList(AppointmentStatus.COMPLETED, AppointmentStatus.CANCELLED, AppointmentStatus.REJECTED));
        return DTOMapper.toDTOList(appointments, DTOMapper::toAppointmentDTO);
    }

    @Override
    public List<AppointmentDTO> getCompletedAppointments(Long doctorId) {
        logger.info("Fetching completed appointments for doctor id: {}", doctorId);
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndStatus(doctorId, AppointmentStatus.COMPLETED);
        return DTOMapper.toDTOList(appointments, DTOMapper::toAppointmentDTO);
    }

    @Override
    public AppointmentDTO confirmAppointment(Long appointmentId) {
        logger.info("Confirming appointment with id: {}", appointmentId);
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment saved = appointmentRepository.save(appointment);
        logger.info("Appointment confirmed successfully with id: {}", appointmentId);
        return DTOMapper.toAppointmentDTO(saved);
    }

    @Override
    public AppointmentDTO rejectAppointment(Long appointmentId) {
        logger.warn("Rejecting appointment with id: {}", appointmentId);
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        
        appointment.setStatus(AppointmentStatus.REJECTED);
        Appointment saved = appointmentRepository.save(appointment);
        return DTOMapper.toAppointmentDTO(saved);
    }

    @Override
    public AppointmentDTO completeAppointment(Long appointmentId) {
        logger.info("Completing appointment with id: {}", appointmentId);
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        
        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment saved = appointmentRepository.save(appointment);
        logger.info("Appointment completed with id: {}", appointmentId);
        return DTOMapper.toAppointmentDTO(saved);
    }

    @Override
    public MedicalRecordDTO addConsultationDetails(Long appointmentId, MedicalRecordDTO medicalRecordDTO) {
        logger.info("Adding consultation details for appointment id: {}", appointmentId);
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found for consultation. id: " + appointmentId));
        
        MedicalRecord medicalRecord = DTOMapper.toMedicalRecordEntity(medicalRecordDTO);
        medicalRecord.setAppointment(appointment);
        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        logger.info("Medical record saved successfully with id: {}", savedRecord.getId());
        return DTOMapper.toMedicalRecordDTO(savedRecord);
    }

    @Override
    public PrescriptionDTO addPrescription(Long medicalRecordId, PrescriptionDTO prescriptionDTO) {
        logger.info("Adding prescription for medical record id: {}", medicalRecordId);
        MedicalRecord record = medicalRecordRepository.findById(medicalRecordId)
            .orElseThrow(() -> new ResourceNotFoundException("Medical record not found with id: " + medicalRecordId));
        
        Prescription prescription = DTOMapper.toPrescriptionEntity(prescriptionDTO);
        prescription.setMedicalRecord(record);
        Prescription savedPrescription = prescriptionRepository.save(prescription);
        logger.info("Prescription added successfully with id: {}", savedPrescription.getId());
        return DTOMapper.toPrescriptionDTO(savedPrescription);
    }

    @Override
    public List<MedicalRecordDTO> viewPatientHistory(Long patientId) {
        logger.info("Fetching medical history for patient id: {}", patientId);
        List<MedicalRecord> records = medicalRecordRepository.findByAppointmentPatientId(patientId);
        return DTOMapper.toDTOList(records, DTOMapper::toMedicalRecordDTO);
    }
}