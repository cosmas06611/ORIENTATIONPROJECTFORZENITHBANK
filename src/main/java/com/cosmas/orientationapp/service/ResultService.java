package com.cosmas.orientationapp.service;

import com.cosmas.model.Result;
import com.cosmas.orientationapp.repository.ResultRepo;

import com.cosmas.orientationapp.utility.ExcelExporter;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ResultService {

    private final ResultRepo resultRepo;


    public ResultService(ResultRepo resultRepo) {
        this.resultRepo = resultRepo;
    }

    public List<Result> getStudentGrades() {
        return resultRepo.findAll();
    }



    public Result addGrade(Result grade) {
        return resultRepo.save(grade);
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
            resultRepo.saveAll(grades);

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
        return resultRepo.findByOrientationClassNumber(orientationClassNumber);

    }

    public Result deleteGrade(String staffNumber) {
        Result report = resultRepo.findByStaffNumberIgnoreCase(staffNumber).orElseThrow(() ->
                new RuntimeException("The result for " + staffNumber + " does not exist"));

        resultRepo.delete(report);
        return report;

    }

    public Result updateResult(Result result) {
        return resultRepo.save(result);
    }

    public Result searchResult(String keyword) {
        return resultRepo.searchResult(keyword);
    }


    public Result getMyResult(){
        String staffNumber = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return resultRepo.findByStaffNumberIgnoreCase(staffNumber)
                .orElseThrow(() -> new RuntimeException("Result not found"));
    }

//    Get percentage distribution of FAIL, PASS, FAIR, EXCELLENT
    public Map<String, Double> getRemarkPercentages(){
        List<Object[]> counts = resultRepo.countByRemark();
        long total = resultRepo.count();
        Map<String, Double> percentages = new HashMap<>();
        for(Object[] row : counts){
            String remark = (String) row[0];
            long count = (long) row[1];
            double percent = (total > 0) ? ((double) count / total) * 100 : 0;

//            round to 2 decimal places
            BigDecimal rounded = BigDecimal.valueOf(percent).setScale(2, RoundingMode.HALF_UP);

            percentages.put(remark, rounded.doubleValue());
        }
        return percentages;
    }


//    Get results by remark
    public List<Result> getResultsByRemark(String remark){
        return resultRepo.findByRemark(remark);
    }

//    Export results of a specific remark to Excel

    public InputStream exportResultsToExcel(String remark) throws Exception{
        List<Result> results = getResultsByRemark(remark);
        return ExcelExporter.resultsToExcel(results);
    }
}