package com.cosmas.orientationapp.service;

import com.cosmas.model.Grade;
import com.cosmas.orientationapp.repository.StudentRepo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private StudentRepo studentRepo;


    public StudentService(StudentRepo studentRepo){
        this.studentRepo = studentRepo;
    }

    public List<Grade> getStudentGrades() {
       return studentRepo.findAll();
    }
}
