package com.cosmas.orientationapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "grades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    @Id
    private String staffNumber;
    private String name;
    private String grade;
    private String branch;
    private String jobTitle;
    private String monthAndYear;
    private String orientationClassNumber;
    private int selfMastery;
    private int basicEmergencyResponse;
    private int basicAccounting;
    private int fundamentalsOfCredit;
    private int understandingBankingBusiness;
    private int zenithInternalCourse;
    private int totalScore;
    private int averageScore;
    private int position;
    private String remark;

}
