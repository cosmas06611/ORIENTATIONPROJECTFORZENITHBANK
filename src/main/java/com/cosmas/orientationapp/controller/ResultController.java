package com.cosmas.orientationapp.controller;

import com.cosmas.model.Result;
import com.cosmas.orientationapp.service.PDFService;
import com.cosmas.orientationapp.service.ResultService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ResultController {

    private final ResultService resultService;
    private final PDFService pdfService;


    public ResultController(ResultService resultService, PDFService pdfService) {
        this.resultService = resultService;
        this.pdfService = pdfService;
    }



    //    admin can get all th result
    @GetMapping("/results")
    public ResponseEntity<List<Result>> getStudentResult() {
        List<Result> studGrade = resultService.getStudentGrades();
        if (studGrade != null && !studGrade.isEmpty()) {
            return ResponseEntity.ok(studGrade);
        }
        return ResponseEntity.noContent().build();
    }

        @GetMapping("/me/download")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<byte[]> downloadMyResult(){
        Result result = resultService.getMyResult();

        byte[] pdf = pdfService.generateResultPdf(result);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.pdf")
                .body(pdf);
        }

//        user can get their result
        @GetMapping("/me")
        @PreAuthorize("hasRole(USER)")
        public ResponseEntity<Result> getMyResult(){
        return ResponseEntity.ok(resultService.getMyResult());
        }

    //    admin can search all result by orientation class number
    @GetMapping("grade/{orientationClassNumber}")
    public ResponseEntity<List<Result>> getResultByOrientationClassNumber(@PathVariable String orientationClassNumber) {
        List<Result> orientationResult = resultService.getResultByOrientationClassNumber(orientationClassNumber);
        if (orientationResult.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orientationResult, HttpStatus.OK);
    }

    //    admin can also ADD the grades manually from the ui interface
    @PostMapping("/result")
    public ResponseEntity<Result> addResult(@RequestBody Result result) {
        Result savedGrade = resultService.addGrade(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGrade);
    }

    //    this is to upload the Excel sheet by the admin
    @PostMapping("/grades/upload")
    public ResponseEntity<String> uploadGrades(@RequestParam("file") MultipartFile file) {
        try {
            resultService.importGradesFromExcel(file);
            return ResponseEntity.ok("Upload successful!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

//    ADMIN CAN DELETE A RESULT USING THE STAFF NUMBER REGARDLESS OF THE CASE
    @DeleteMapping("/grade/{staffNumber}")
    public ResponseEntity <Void> deleteResultByStaffNumber(@PathVariable String staffNumber) {
                resultService.deleteGrade(staffNumber);
               return ResponseEntity.status(HttpStatus.OK).build();
    }


//    ADMIN CAN UPDATE RESULT RECORD FOR A STAFF
    @PutMapping("/update")
    public ResponseEntity<Result> updateResult(@RequestBody Result result){
        Result report = resultService.updateResult(result);
        if(report != null){
            return new ResponseEntity<>(report, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/results/search/{keyword}")
    public ResponseEntity<Result> searchResultByUser(@PathVariable("keyword") String keyword) {

        Result result = resultService.searchResult(keyword);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }
}