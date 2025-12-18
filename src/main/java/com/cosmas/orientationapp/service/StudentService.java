package com.cosmas.orientationapp.service;

import com.cosmas.model.Result;
import com.cosmas.orientationapp.repository.StudentRepo;

import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepo studentRepo;


    public StudentService(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    public List<Result> getStudentGrades() {
        return studentRepo.findAll();
    }

    public Result getStudentGrade(String staffNumber) {
        return studentRepo.findById(staffNumber).orElse(new Result());
    }

    public Result addGrade(Result grade) {
        return studentRepo.save(grade);
    }

    @Transactional
    public void importGradesFromExcel(MultipartFile file) {

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<Result> grades = new ArrayList<>();

            while (rows.hasNext()) {
                Row row = rows.next();

//                 KEY RULE: staffNumber decides if row is data or header
                String staffNumber = getString(row, 0);

//                // Ignore all headers, titles, empty rows
//                if (!isValidStaffNumber(staffNumber)) {
//                    continue;
//                }

                // KEY CHANGE: Check for headers first, then validate the ID format
                if (isHeader(staffNumber) || !isValidStaffNumber(staffNumber)) {
                    continue; // This safely skips headers AND empty/garbage rows
                }

                Result grade = new Result();
                grade.setStaffNumber(staffNumber);
                grade.setName(getString(row, 1));
                grade.setLevel(getString(row, 2));
                grade.setBranch(getString(row, 3));
                grade.setJobTitle(getString(row, 4));
                grade.setMonthAndYear(getString(row, 5));
                grade.setOrientationClassNumber(getString(row, 6));

                grade.setSelfMastery(getInt(row, 7));
                grade.setBasicEmergencyResponse(getInt(row, 8));
                grade.setBasicAccounting(getInt(row, 9));
                grade.setFundamentalsOfCredit(getInt(row, 10));
                grade.setUnderstandingBankingBusiness(getInt(row, 11));
                grade.setZenithInternalCourse(getInt(row, 12));

                grade.setTotalScore(getInt(row, 13));
                grade.setAverageScore(getInt(row, 14));
                grade.setPosition(getInt(row, 15));
                grade.setRemark(getString(row, 16));

                grades.add(grade);
            }

            //  Save in batch (faster & safer)
            studentRepo.saveAll(grades);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload Excel: " + e.getMessage(), e);
        }
    }

    // HELPERS

//    private boolean isValidStaffNumber(String value) {
//        if (value == null) return false;
//
//        String v = value.trim();
//
//        // Ignore column headers
//        if (v.equalsIgnoreCase("staffnumber")) return false;
//        if (v.equalsIgnoreCase("staff number")) return false;
//
//        // Optional: enforce your staff number format
//        // Example: ZB12001
//        return v.matches("[A-Za-z0-9]+");
//    }


    private boolean isHeader(String value) {
        if (value == null) return false;
        String v = value.trim();
        // Returns true for any variation of these words
        return v.equalsIgnoreCase("staffnumber") || v.equalsIgnoreCase("staff number");
    }

    private boolean isValidStaffNumber(String value) {
        if (value == null) return false;
        // Only returns true if it matches your alphanumeric pattern (e.g., ZB12001)
        return value.trim().matches("[A-Za-z0-9]+");
    }

    private String getString(Row row, int index) {
        Cell cell = row.getCell(index, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        }

        return null;
    }

    private int getInt(Row row, int index) {
        Cell cell = row.getCell(index, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return 0;

        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        }

        if (cell.getCellType() == CellType.STRING) {
            try {
                return Integer.parseInt(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }

    public List<Result> getResultByOrientationClassNumber(String orientationClassNumber) {
        return studentRepo.findByOrientationClassNumber(orientationClassNumber);

    }

    public Result deleteGrade(String staffNumber) {
        Result report = studentRepo.findByStaffNumberIgnoreCase(staffNumber).orElseThrow(() ->
                new RuntimeException("The result for " + staffNumber + " does not exist"));

        studentRepo.delete(report);
        return report;

    }

    public Result updateResult(Result result) {
        return studentRepo.save(result);
    }
}