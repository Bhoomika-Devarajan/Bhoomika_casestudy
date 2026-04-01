package com.example.demo.Service;

import java.util.List;
import com.example.demo.dto.MedicalRecordDTO;

public interface MedicalRecordService {

    MedicalRecordDTO createMedicalRecord(MedicalRecordDTO record);

    MedicalRecordDTO updateMedicalRecord(Long id, MedicalRecordDTO record);

    MedicalRecordDTO getMedicalRecordByAppointment(Long appointmentId);

    List<MedicalRecordDTO> getMedicalRecordsByPatient(Long patientId);
}