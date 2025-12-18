package com.cosmas.orientationapp.controller;

import com.cosmas.model.Result;
import com.cosmas.orientationapp.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    private final StudentService studentService;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }



    //    admin can get all th result
    @GetMapping("/grades")
    public ResponseEntity<List<Result>> getStudentResult() {
        List<Result> studGrade = studentService.getStudentGrades();
        if (studGrade != null && !studGrade.isEmpty()) {
            return ResponseEntity.ok(studGrade);
        }
        return ResponseEntity.noContent().build();
    }

    //    user can only search their result by USING staff number USED IN REGISTERING
    @GetMapping("/grades/{staffNumber}")
    public ResponseEntity<Result> getResultByStaffNumber(@PathVariable String staffNumber) {
        Result staffGrade = studentService.getStudentGrade(staffNumber);
        if (staffGrade != null) {
            return new ResponseEntity<>(staffGrade, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //    admin can search all result by orientation class number
    @GetMapping("grade/{orientationClassNumber}")
    public ResponseEntity<List<Result>> getResultByOrientationClassNumber(@PathVariable String orientationClassNumber) {
        List<Result> orientationResult = studentService.getResultByOrientationClassNumber(orientationClassNumber);
        if (orientationResult.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orientationResult, HttpStatus.OK);
    }

    //    admin can also ADD the grades manually from the ui interface
    @PostMapping("/grades")
    public ResponseEntity<Result> addResult(@RequestBody Result grade) {
        Result savedGrade = studentService.addGrade(grade);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGrade);
    }

    //    this is to upload the Excel sheet by the admin
    @PostMapping("/grades/upload")
    public ResponseEntity<String> uploadGrades(@RequestParam("file") MultipartFile file) {
        try {
            studentService.importGradesFromExcel(file);
            return ResponseEntity.ok("Upload successful!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

//    ADMIN CAN DELETE A RESULT USING THE STAFF NUMBER REGARDLESS OF THE CASE
    @DeleteMapping("/grade/{staffNumber}")
    public ResponseEntity <Void> deleteResultByStaffNumber(@PathVariable String staffNumber) {
                studentService.deleteGrade(staffNumber);
               return ResponseEntity.status(HttpStatus.OK).build();
    }


//    ADMIN CAN UPDATE RESULT RECORD FOR A STAFF
    @PutMapping("/update")
    public ResponseEntity<Result> updateResult(@RequestBody Result result){
        Result report = studentService.updateResult(result);
        if(report != null){
            return new ResponseEntity<>(report, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}