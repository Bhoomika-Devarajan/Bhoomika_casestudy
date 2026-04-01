package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import java.util.List;
import java.util.stream.Collectors;

public class DTOMapper {

    // User
    public static UserDTO toUserDTO(User user) {
        if (user == null) return null;
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.getCreatedAt());
    }

    public static User toUserEntity(UserDTO dto) {
        if (dto == null) return null;
        return new User(dto.getId(), dto.getUsername(), dto.getPassword(), dto.getRole(), dto.getCreatedAt());
    }

    // Patient
    public static PatientDTO toPatientDTO(Patient patient) {
        if (patient == null) return null;
        return new PatientDTO(
            patient.getId(),
            toUserDTO(patient.getUser()),
            patient.getFullName(),
            patient.getDateOfBirth(),
            patient.getGender(),
            patient.getMobileNumber(),
            patient.getAddress(),
            patient.getCreatedAt()
        );
    }

    public static Patient toPatientEntity(PatientDTO dto) {
        if (dto == null) return null;
        return new Patient(
            dto.getId(),
            toUserEntity(dto.getUser()),
            dto.getFullName(),
            dto.getDateOfBirth(),
            dto.getGender(),
            dto.getMobileNumber(),
            dto.getAddress(),
            dto.getCreatedAt()
        );
    }

    // Doctor
    public static DoctorDTO toDoctorDTO(Doctor doctor) {
        if (doctor == null) return null;
        return new DoctorDTO(
            doctor.getId(),
            toUserDTO(doctor.getUser()),
            doctor.getName(),
            doctor.getSpecialty(),
            doctor.getExperience(),
            doctor.getQualification(),
            doctor.getDesignation(),
            doctor.getContactNumber(),
            doctor.getEmail()
        );
    }

    public static Doctor toDoctorEntity(DoctorDTO dto) {
        if (dto == null) return null;
        return new Doctor(
            dto.getId(),
            toUserEntity(dto.getUser()),
            dto.getName(),
            dto.getSpecialty(),
            dto.getExperience(),
            dto.getQualification(),
            dto.getDesignation(),
            dto.getContactNumber(),
            dto.getEmail()
        );
    }

    // Appointment
    public static AppointmentDTO toAppointmentDTO(Appointment appointment) {
        if (appointment == null) return null;
        return new AppointmentDTO(
            appointment.getId(),
            toPatientDTO(appointment.getPatient()),
            toDoctorDTO(appointment.getDoctor()),
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime(),
            appointment.getSymptoms(),
            appointment.getVisitType(),
            appointment.getStatus(),
            appointment.getCreatedAt()
        );
    }

    public static Appointment toAppointmentEntity(AppointmentDTO dto) {
        if (dto == null) return null;
        return new Appointment(
            dto.getId(),
            toPatientEntity(dto.getPatient()),
            toDoctorEntity(dto.getDoctor()),
            dto.getAppointmentDate(),
            dto.getAppointmentTime(),
            dto.getSymptoms(),
            dto.getVisitType(),
            dto.getStatus(),
            dto.getCreatedAt()
        );
    }

    // MedicalRecord
    public static MedicalRecordDTO toMedicalRecordDTO(MedicalRecord record) {
        if (record == null) return null;
        return new MedicalRecordDTO(
            record.getId(),
            toAppointmentDTO(record.getAppointment()),
            record.getCurrentSymptoms(),
            record.getPhysicalExamination(),
            record.getTreatmentPlan(),
            record.getRecommendedTests(),
            record.getDiagnosis(),
            record.getCreatedAt(),
            toPrescriptionDTOList(record.getPrescriptions()),
            toAppointmentTestDTOList(record.getAppointmentTests())
        );
    }
    
    public static List<AppointmentTestDTO> toAppointmentTestDTOList(List<AppointmentTest> tests) {
        if (tests == null) return null;
        return tests.stream().map(DTOMapper::toAppointmentTestDTO).collect(Collectors.toList());
    }

    public static List<PrescriptionDTO> toPrescriptionDTOList(List<Prescription> prescriptions) {
        if (prescriptions == null) return null;
        return prescriptions.stream().map(DTOMapper::toPrescriptionDTO).collect(Collectors.toList());
    }

    public static MedicalRecord toMedicalRecordEntity(MedicalRecordDTO dto) {
        if (dto == null) return null;
        MedicalRecord record = new MedicalRecord();
        record.setId(dto.getId());
        record.setAppointment(toAppointmentEntity(dto.getAppointment()));
        record.setCurrentSymptoms(dto.getCurrentSymptoms());
        record.setPhysicalExamination(dto.getPhysicalExamination());
        record.setTreatmentPlan(dto.getTreatmentPlan());
        record.setRecommendedTests(dto.getRecommendedTests());
        record.setDiagnosis(dto.getDiagnosis());
        record.setCreatedAt(dto.getCreatedAt());
        
        // Handle Prescriptions
        if (dto.getPrescriptions() != null) {
            List<Prescription> prescriptions = dto.getPrescriptions().stream().map(pDto -> {
                Prescription p = toPrescriptionEntity(pDto);
                p.setMedicalRecord(record); // Link back for JPA cascade
                return p;
            }).collect(Collectors.toList());
            record.setPrescriptions(prescriptions);
        }

        // Handle AppointmentTests
        if (dto.getAppointmentTests() != null) {
            List<AppointmentTest> tests = dto.getAppointmentTests().stream().map(tDto -> {
                AppointmentTest t = toAppointmentTestEntity(tDto);
                t.setMedicalRecord(record); // Link back for JPA cascade
                return t;
            }).collect(Collectors.toList());
            record.setAppointmentTests(tests);
        }

        return record;
    }

    public static List<AppointmentTest> toAppointmentTestList(List<AppointmentTestDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(DTOMapper::toAppointmentTestEntity).collect(Collectors.toList());
    }

    public static List<Prescription> toPrescriptionList(List<PrescriptionDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(DTOMapper::toPrescriptionEntity).collect(Collectors.toList());
    }

    // Prescription
    public static PrescriptionDTO toPrescriptionDTO(Prescription prescription) {
        if (prescription == null) return null;
        return new PrescriptionDTO(
            prescription.getId(),
            null, // Avoid recursion toMedicalRecordDTO
            prescription.getMedicineName(),
            prescription.getMorning(),
            prescription.getAfternoon(),
            prescription.getEvening(),
            prescription.getFoodInstruction(),
            prescription.getDurationDays()
        );
    }

    public static Prescription toPrescriptionEntity(PrescriptionDTO dto) {
        if (dto == null) return null;
        return new Prescription(
            dto.getId(),
            toMedicalRecordEntity(dto.getMedicalRecord()),
            dto.getMedicineName(),
            dto.getMorning(),
            dto.getAfternoon(),
            dto.getEvening(),
            dto.getFoodInstruction(),
            dto.getDurationDays()
        );
    }

    // MedicalTest
    public static MedicalTestDTO toMedicalTestDTO(MedicalTest test) {
        if (test == null) return null;
        return new MedicalTestDTO(test.getId(), test.getTestName(), test.getDescription());
    }

    public static MedicalTest toMedicalTestEntity(MedicalTestDTO dto) {
        if (dto == null) return null;
        return new MedicalTest(dto.getId(), dto.getTestName(), dto.getDescription());
    }

    // AppointmentTest
    public static AppointmentTestDTO toAppointmentTestDTO(AppointmentTest test) {
        if (test == null) return null;
        return new AppointmentTestDTO(
            test.getId(),
            null, // Avoid recursion toMedicalRecordDTO
            toMedicalTestDTO(test.getMedicalTest())
        );
    }

    public static AppointmentTest toAppointmentTestEntity(AppointmentTestDTO dto) {
        if (dto == null) return null;
        AppointmentTest test = new AppointmentTest();
        test.setId(dto.getId());
        test.setMedicalRecord(toMedicalRecordEntity(dto.getMedicalRecord()));
        test.setMedicalTest(toMedicalTestEntity(dto.getMedicalTest()));
        return test;
    }

    // List Converters
    public static <E, D> List<D> toDTOList(List<E> entities, java.util.function.Function<E, D> mapper) {
        if (entities == null) return null;
        return entities.stream().map(mapper).collect(Collectors.toList());
    }
}
