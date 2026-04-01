package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.MedicalRecord;
import com.example.demo.entity.Patient;


@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

	
	

	List<MedicalRecord> findByAppointmentPatientId(Long patientId);

	MedicalRecord findByAppointmentId(Long appointmentId);

	
}