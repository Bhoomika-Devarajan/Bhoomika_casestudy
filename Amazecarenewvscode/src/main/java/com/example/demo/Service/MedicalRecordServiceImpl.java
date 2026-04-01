package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.MedicalRecord;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.MedicalRecordRepository;
import com.example.demo.dto.MedicalRecordDTO;
import com.example.demo.mapper.DTOMapper;
import com.example.demo.exception.ResourceNotFoundException;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private static final Logger logger =
            LoggerFactory.getLogger(MedicalRecordServiceImpl.class);

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO recordDTO) {
        logger.info("Creating new medical record");
        MedicalRecord record = DTOMapper.toMedicalRecordEntity(recordDTO);

        // Fetch managed Appointment to avoid Detached Entity issues
        if (recordDTO.getAppointment() != null && recordDTO.getAppointment().getId() != null) {
            Appointment appointment = appointmentRepository.findById(recordDTO.getAppointment().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + recordDTO.getAppointment().getId()));
            record.setAppointment(appointment);
        }

        MedicalRecord savedRecord = medicalRecordRepository.save(record);
        logger.info("Medical record created successfully with id: {}", savedRecord.getId());
        return DTOMapper.toMedicalRecordDTO(savedRecord);
    }

    @Override
    public MedicalRecordDTO updateMedicalRecord(Long id, MedicalRecordDTO recordDTO) {
        logger.info("Updating medical record with id: {}", id);
        MedicalRecord existing = medicalRecordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medical record not found with id: " + id));
        
        existing.setDiagnosis(recordDTO.getDiagnosis());
        existing.setCurrentSymptoms(recordDTO.getCurrentSymptoms());
        existing.setPhysicalExamination(recordDTO.getPhysicalExamination());
        existing.setTreatmentPlan(recordDTO.getTreatmentPlan());
        existing.setRecommendedTests(recordDTO.getRecommendedTests());
        MedicalRecord updatedRecord = medicalRecordRepository.save(existing);
        logger.info("Medical record updated successfully with id: {}", id);
        return DTOMapper.toMedicalRecordDTO(updatedRecord);
    }

    @Override
    public MedicalRecordDTO getMedicalRecordByAppointment(Long appointmentId) {
        logger.info("Fetching medical record for appointment id: {}", appointmentId);
        MedicalRecord record = medicalRecordRepository.findByAppointmentId(appointmentId);
        if (record == null) {
            throw new ResourceNotFoundException("Medical record not found for appointment id: " + appointmentId);
        }
        return DTOMapper.toMedicalRecordDTO(record);
    }

    @Override
    public List<MedicalRecordDTO> getMedicalRecordsByPatient(Long patientId) {
        logger.info("Fetching medical records for patient id: {}", patientId);
        List<MedicalRecord> records = medicalRecordRepository.findByAppointmentPatientId(patientId);
        return DTOMapper.toDTOList(records, DTOMapper::toMedicalRecordDTO);
    }
}