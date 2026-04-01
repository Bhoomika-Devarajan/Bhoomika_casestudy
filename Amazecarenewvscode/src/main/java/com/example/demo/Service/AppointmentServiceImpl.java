package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.AppointmentStatus;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.dto.AppointmentDTO;
import com.example.demo.mapper.DTOMapper;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.BadRequestException;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger logger =
            LoggerFactory.getLogger(AppointmentServiceImpl.class);

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        logger.info("Creating appointment");
        Appointment appointment = DTOMapper.toAppointmentEntity(appointmentDTO);
        
        // Fetch managed entities to avoid Detached Entity issues
        if (appointmentDTO.getDoctor() != null && appointmentDTO.getDoctor().getId() != null) {
            Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + appointmentDTO.getDoctor().getId()));
            appointment.setDoctor(doctor);
        }
        if (appointmentDTO.getPatient() != null && appointmentDTO.getPatient().getId() != null) {
            Patient patient = patientRepository.findById(appointmentDTO.getPatient().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + appointmentDTO.getPatient().getId()));
            appointment.setPatient(patient);
        }

        appointment.setStatus(AppointmentStatus.REQUESTED);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        logger.info("Appointment created successfully with id: {}", savedAppointment.getId());
        return DTOMapper.toAppointmentDTO(savedAppointment);
    }

    @Override
    public AppointmentDTO updateAppointmentStatus(Long appointmentId, String status) {
        logger.info("Updating appointment status for id: {}", appointmentId);
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        
        try {
            appointment.setStatus(AppointmentStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid appointment status: {}", status);
            throw new BadRequestException("Invalid Appointment Status: " + status);
        }
        logger.info("Appointment status updated to {}", status);
        Appointment saved = appointmentRepository.save(appointment);
        return DTOMapper.toAppointmentDTO(saved);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByDoctor(Long doctorId) {
        logger.info("Fetching appointments for doctor id: {}", doctorId);
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return DTOMapper.toDTOList(appointments, DTOMapper::toAppointmentDTO);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByPatient(Long patientId) {
        logger.info("Fetching appointments for patient id: {}", patientId);
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return DTOMapper.toDTOList(appointments, DTOMapper::toAppointmentDTO);
    }
}