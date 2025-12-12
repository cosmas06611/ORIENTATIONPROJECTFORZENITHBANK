package com.cosmas.orientationapp.controller;

import com.cosmas.model.Grade;
import com.cosmas.orientationapp.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    private StudentService studentService;


    public StudentController(StudentService studentService){
       this.studentService = studentService;
   }

    @GetMapping("/grades")
    public ResponseEntity<List<Grade>> getStudentGrades(){
            List <Grade> studGrade = studentService.getStudentGrades();
            if(studGrade != null && !studGrade.isEmpty()){
                return ResponseEntity.ok(studGrade);
            }
           return ResponseEntity.noContent().build();
    }
}
