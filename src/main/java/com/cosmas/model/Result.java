package com.cosmas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
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
public class Result {

    @Id
    private String staffNumber;
    private String name;
    private String level;
    private String branch;
    private String jobTitle;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
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
    @Column(length = 150)
    private String supervisorEmail;

}
