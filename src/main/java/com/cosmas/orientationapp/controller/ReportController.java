package com.cosmas.orientationapp.controller;

import com.cosmas.orientationapp.service.ReportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }


//    this API is for downloading the list of staff yet to complete the orientation.
    @GetMapping("/download-pending")
    public ResponseEntity<Resource> downloadPending() throws IOException{
        String filename ="pending_staff.xlsx";
        InputStreamResource file = new InputStreamResource(reportService.generatePendingStudentsExcel());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= " + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);

    }


}
