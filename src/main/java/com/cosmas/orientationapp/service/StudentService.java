package com.cosmas.orientationapp.service;

import com.cosmas.model.Grade;
import com.cosmas.orientationapp.repository.StudentRepo;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private StudentRepo studentRepo;


    public StudentService(StudentRepo studentRepo){
        this.studentRepo = studentRepo;
    }

    public List<Grade> getStudentGrades() {
       return studentRepo.findAll();
    }

    public Grade getStudentGrade(String staffNumber) {
        return studentRepo.findById(staffNumber).orElse(new Grade());
    }

    public Grade addGrade(Grade grade) {
            return studentRepo.save(grade);
    }

    public void saveGradesFromExcel(MultipartFile file) {
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {   // Skip header row
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Grade grade = new Grade();
                grade.setStaffNumber(row.getCell(0).getStringCellValue());
                grade.setName(row.getCell(1).getStringCellValue());
                grade.setGrade(row.getCell(2).getStringCellValue());
                grade.setBranch(row.getCell(3).getStringCellValue());
                grade.setJobTitle(row.getCell(4).getStringCellValue());
                grade.setMonthAndYear(row.getCell(5).getStringCellValue());
                grade.setOrientationClassNumber(row.getCell(6).getStringCellValue());

                grade.setSelfMastery((int) row.getCell(7).getNumericCellValue());
                grade.setBasicEmergencyResponse((int) row.getCell(8).getNumericCellValue());
                grade.setBasicAccounting((int) row.getCell(9).getNumericCellValue());
                grade.setFundamentalsOfCredit((int) row.getCell(10).getNumericCellValue());
                grade.setUnderstandingBankingBusiness((int) row.getCell(11).getNumericCellValue());
                grade.setZenithInternalCourse((int) row.getCell(12).getNumericCellValue());
                grade.setTotalScore((int) row.getCell(13).getNumericCellValue());
                grade.setAverageScore((int) row.getCell(14).getNumericCellValue());
                grade.setPosition((int) row.getCell(15).getNumericCellValue());
                grade.setRemark(row.getCell(16).getStringCellValue());

                studentRepo.save(grade);
            }

            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to process Excel file: " + e.getMessage());
        }
    }

    public List<Grade> getResultByOrientationClassNumber(String orientationClassNumber) {
        return studentRepo.findByOrientationClassNumber(orientationClassNumber);

    }
}
