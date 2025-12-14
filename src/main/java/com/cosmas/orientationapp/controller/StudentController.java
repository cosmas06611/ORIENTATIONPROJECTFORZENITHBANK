package com.cosmas.orientationapp.controller;

import com.cosmas.orientationapp.model.Grade;
import com.cosmas.orientationapp.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    private StudentService studentService;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    //    admin can get all th result
    @GetMapping("/grades")
    public ResponseEntity<List<Grade>> getStudentResult() {
        List<Grade> studGrade = studentService.getStudentGrades();
        if (studGrade != null && !studGrade.isEmpty()) {
            return ResponseEntity.ok(studGrade);
        }
        return ResponseEntity.noContent().build();
    }

    //    user can only search their result by USING staff number USED IN REGISTERING
    @GetMapping("/grades/{staffNumber}")
    public ResponseEntity<Grade> getResultByStaffNumber(@PathVariable String staffNumber) {
        Grade staffGrade = studentService.getStudentGrade(staffNumber);
        if (staffGrade != null) {
            return new ResponseEntity<>(staffGrade, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //    admin can search all result by orientation class number
    @GetMapping("grade/{orientationClassNumber}")
    public ResponseEntity<List<Grade>> getResultByOrientationClassNumber(@PathVariable String orientationClassNumber) {
        List<Grade> orientationResult = studentService.getResultByOrientationClassNumber(orientationClassNumber);
        if (orientationResult.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orientationResult, HttpStatus.OK);
    }

    //    admin can also ADD the grades manually from the ui interface
    @PostMapping("/grades")
    public ResponseEntity<Grade> addResult(@RequestBody Grade grade) {
        Grade savedGrade = studentService.addGrade(grade);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGrade);
    }

    //    this is to upload the excel sheet by the admin
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
    public ResponseEntity<Grade> updateResult(@RequestBody Grade result){
        Grade report = studentService.updateResult(result);
        if(report != null){
            return new ResponseEntity<>(report, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}