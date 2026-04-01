package com.example.demo.dto;

import com.example.demo.entity.FoodInstruction;

public class PrescriptionDTO {

    private Long id;
    private MedicalRecordDTO medicalRecord;
    private String medicineName;
    private Integer morning;
    private Integer afternoon;
    private Integer evening;
    private FoodInstruction foodInstruction;
    private Integer durationDays;

    public PrescriptionDTO() {}

    public PrescriptionDTO(Long id, MedicalRecordDTO medicalRecord, String medicineName, Integer morning, Integer afternoon, Integer evening, FoodInstruction foodInstruction, Integer durationDays) {
        this.id = id;
        this.medicalRecord = medicalRecord;
        this.medicineName = medicineName;
        this.morning = morning;
        this.afternoon = afternoon;
        this.evening = evening;
        this.foodInstruction = foodInstruction;
        this.durationDays = durationDays;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MedicalRecordDTO getMedicalRecord() { return medicalRecord; }
    public void setMedicalRecord(MedicalRecordDTO medicalRecord) { this.medicalRecord = medicalRecord; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public Integer getMorning() { return morning; }
    public void setMorning(Integer morning) { this.morning = morning; }

    public Integer getAfternoon() { return afternoon; }
    public void setAfternoon(Integer afternoon) { this.afternoon = afternoon; }

    public Integer getEvening() { return evening; }
    public void setEvening(Integer evening) { this.evening = evening; }

    public FoodInstruction getFoodInstruction() { return foodInstruction; }
    public void setFoodInstruction(FoodInstruction foodInstruction) { this.foodInstruction = foodInstruction; }

    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }

    @Override
    public String toString() {
        return "PrescriptionDTO [id=" + id + ", medicalRecord=" + medicalRecord + ", medicineName=" + medicineName
                + ", morning=" + morning + ", afternoon=" + afternoon + ", evening=" + evening + ", foodInstruction="
                + foodInstruction + ", durationDays=" + durationDays + "]";
    }
}
