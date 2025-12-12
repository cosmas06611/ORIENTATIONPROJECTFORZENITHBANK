package com.cosmas.orientationapp.controller;

import com.cosmas.model.Grade;
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


    public StudentController(StudentService studentService){
       this.studentService = studentService;
   }
//    admin can get all th result
    @GetMapping("/grades")
    public ResponseEntity<List<Grade>> getStudentResult(){
            List <Grade> studGrade = studentService.getStudentGrades();
            if(studGrade != null && !studGrade.isEmpty()){
                return ResponseEntity.ok(studGrade);
            }
           return ResponseEntity.noContent().build();
    }

//    user can only search their result by their own staff number
    @GetMapping("/grades/{staffNumber}")
    public ResponseEntity <Grade> getResultByStaffNumber(@PathVariable String staffNumber){
        Grade staffGrade = studentService.getStudentGrade(staffNumber);
        if(staffGrade != null ){
            return new ResponseEntity<>(staffGrade, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    admin can search all result by orientation class number
    @GetMapping("grade/{orientationClassNumber}")
    public ResponseEntity< List <Grade>> getResultByOrientationClassNumber(@PathVariable String orientationClassNumber){
        String normalizedInput = orientationClassNumber.replaceAll("\\s+", "").toLowerCase();
        List <Grade> orientationResult = studentService.getResultByOrientationClassNumber(normalizedInput);
        if(!normalizedInput.isEmpty()){
            return new ResponseEntity<>(orientationResult, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    admin can also input the grades manually from the ui interface
    @PostMapping("/grades")
    public ResponseEntity <Grade> addGrade(@RequestBody Grade grade){
       Grade savedGrade = studentService.addGrade(grade);
       return ResponseEntity.status(HttpStatus.CREATED).body(savedGrade);
    }

//    this is to upload the excel sheet by the admin
    @PostMapping("/grades/upload")
    public ResponseEntity<String> uploadGrades(@RequestParam("file") MultipartFile file) {
        try {
            studentService.saveGradesFromExcel(file);
            return ResponseEntity.ok("Upload successful!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

}
