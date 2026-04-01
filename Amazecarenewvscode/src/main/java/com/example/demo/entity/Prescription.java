package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    private String medicineName;
    private Integer morning;
    private Integer afternoon;
    private Integer evening;

    
    @Enumerated(EnumType.STRING)
    private FoodInstruction foodInstruction;

    private Integer durationDays;

    public Prescription() {
    }

    public Prescription(Long id, MedicalRecord medicalRecord, String medicineName,
                        Integer morning, Integer afternoon, Integer evening,
                        FoodInstruction foodInstruction, Integer durationDays) {
        this.id = id;
        this.medicalRecord = medicalRecord;
        this.medicineName = medicineName;
        this.morning = morning;
        this.afternoon = afternoon;
        this.evening = evening;
        this.foodInstruction = foodInstruction;
        this.durationDays = durationDays;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MedicalRecord getMedicalRecord() { return medicalRecord; }
    public void setMedicalRecord(MedicalRecord medicalRecord) { this.medicalRecord = medicalRecord; }

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
		return "Prescription [id=" + id + ", medicalRecord=" + medicalRecord + ", medicineName=" + medicineName
				+ ", morning=" + morning + ", afternoon=" + afternoon + ", evening=" + evening + ", foodInstruction="
				+ foodInstruction + ", durationDays=" + durationDays + "]";
	}
    
}