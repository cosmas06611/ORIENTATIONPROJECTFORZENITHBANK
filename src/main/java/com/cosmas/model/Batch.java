package com.cosmas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Entity
@Table(name = "batch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Batch {
    @Id
    private String staffNumber;
    private String name;
    private String gender;
    private String grade;
    private String unit;  
    private String department;
    private String branch;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private String resumptionDate;
    private String phoneNumber;
    private String officialEmail;
    private String personalEmail;
    private String qualification;
    private String remark;
    private int batchNumber;
}
