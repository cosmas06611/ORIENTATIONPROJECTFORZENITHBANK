package com.cosmas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "batch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Batch {

    @Id
    @Column(length = 20)
    private String staffNumber;

    @Column(length = 100)
    private String name;

    @Column(length = 10)
    private String gender;

    @Column(length = 20)
    private String grade;

    @Column(length = 100)
    private String unit;

    @Column(length = 150)
    private String department;

    @Column(length = 100)
    private String branch;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate resumptionDate;

    @Column(length = 25)
    private String phoneNumber;

    @Column(length = 150)
    private String officialEmail;

    @Column(length = 150)
    private String personalEmail;

    @Column(columnDefinition = "TEXT")
    private String qualification;

    @Column(columnDefinition = "TEXT")
    private String remark;

    private int batchNumber;
}
