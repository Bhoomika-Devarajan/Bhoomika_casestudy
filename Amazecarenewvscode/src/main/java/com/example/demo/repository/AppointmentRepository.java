package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.AppointmentStatus;
import com.example.demo.entity.Patient;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findByDoctorId(Long doctorId);

	List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus completed);

	List<Appointment> findByPatientId(Long patientId);

	List<Appointment> findByPatientIdAndStatusNotIn(Long patientId, java.util.List<AppointmentStatus> statuses);

    List<Appointment> findByPatientIdAndStatus(Long patientId, AppointmentStatus status);

    List<Appointment> findByDoctorIdAndStatusNotIn(Long doctorId, java.util.List<AppointmentStatus> statuses);
}