package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.dto.*;
import com.example.demo.mapper.DTOMapper;
import com.example.demo.exception.ResourceNotFoundException;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger =
            LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public DoctorDTO addDoctor(DoctorDTO doctorDTO) {
        logger.info("Adding new doctor: {}", doctorDTO.getName());
        Doctor doctor = DTOMapper.toDoctorEntity(doctorDTO);
        Doctor saved = doctorRepository.save(doctor);
        return DTOMapper.toDoctorDTO(saved);
    }

    @Override
    public DoctorDTO updateDoctor(Long doctorId, DoctorDTO doctorDTO) {
        logger.info("Updating doctor with id: {}", doctorId);
        Doctor existing = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));
        
        existing.setName(doctorDTO.getName());
        existing.setSpecialty(doctorDTO.getSpecialty());
        existing.setExperience(doctorDTO.getExperience());
        existing.setQualification(doctorDTO.getQualification());
        existing.setDesignation(doctorDTO.getDesignation());
        existing.setContactNumber(doctorDTO.getContactNumber());
        existing.setEmail(doctorDTO.getEmail());
        Doctor updated = doctorRepository.save(existing);
        logger.info("Doctor updated successfully with id: {}", doctorId);
        return DTOMapper.toDoctorDTO(updated);
    }

    @Override
    public void deleteDoctor(Long doctorId) {
        logger.warn("Deleting doctor with id: {}", doctorId);
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        doctorRepository.deleteById(doctorId);
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        logger.info("Fetching all doctors");
        List<Doctor> doctors = doctorRepository.findAll();
        return DTOMapper.toDTOList(doctors, DTOMapper::toDoctorDTO);
    }

    @Override
    public PatientDTO addPatient(PatientDTO patientDTO) {
        logger.info("Adding new patient: {}", patientDTO.getFullName());
        Patient patient = DTOMapper.toPatientEntity(patientDTO);
        Patient saved = patientRepository.save(patient);
        return DTOMapper.toPatientDTO(saved);
    }

    @Override
    public PatientDTO updatePatient(Long patientId, PatientDTO patientDTO) {
        logger.info("Updating patient with id: {}", patientId);
        Patient existing = patientRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        
        existing.setFullName(patientDTO.getFullName());
        existing.setMobileNumber(patientDTO.getMobileNumber());
        existing.setAddress(patientDTO.getAddress());
        Patient updated = patientRepository.save(existing);
        logger.info("Patient updated successfully with id: {}", patientId);
        return DTOMapper.toPatientDTO(updated);
    }

    @Override
    public void deletePatient(Long patientId) {
        logger.warn("Deleting patient with id: {}", patientId);
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        patientRepository.deleteById(patientId);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        logger.info("Fetching all patients");
        List<Patient> patients = patientRepository.findAll();
        return DTOMapper.toDTOList(patients, DTOMapper::toPatientDTO);
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        logger.info("Fetching all appointments");
        List<Appointment> appointments = appointmentRepository.findAll();
        return DTOMapper.toDTOList(appointments, DTOMapper::toAppointmentDTO);
    }

    @Override
    public AppointmentDTO rescheduleAppointment(Long appointmentId, AppointmentDTO appointmentDTO) {
        logger.info("Rescheduling appointment with id: {}", appointmentId);
        Appointment existing = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        
        existing.setAppointmentDate(appointmentDTO.getAppointmentDate());
        existing.setAppointmentTime(appointmentDTO.getAppointmentTime());
        Appointment updated = appointmentRepository.save(existing);
        logger.info("Appointment rescheduled successfully for id: {}", appointmentId);
        return DTOMapper.toAppointmentDTO(updated);
    }
}