package com.cosmas.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;


@Getter @Setter @AllArgsConstructor
public class DashboardStatsDTO{

    private long totalStudents;
    private long completedExams;
    private long pendingExams;
    private double completionPercentage;
    private double pendingPercentage;
}


