package com.cosmas.orientationapp.utility;

import com.cosmas.model.Result;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class ExcelExporter {

    public static InputStream resultsToExcel(List<Result> results) throws Exception{
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Results");

//        Header row

        Row header = sheet.createRow(0);
        String[] columns = {
                "Staff Number", "Name", "Level", "Branch", "Job Title",
                "Month & Year", "Orientation Class", "Self Mastery", "Basic Emergency",
                "Basic Accounting", "Fundamentals of Credit", "Understanding Banking",
                "Zenith Internal Course", "Total Score", "Average Score", "Position", "Remark"
        };

        for(int i =0; i < columns.length; i++){
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
        }

//        Fill data
        int rowIndex = 1;
        for(Result r :  results){
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(r.getStaffNumber());
            row.createCell(1).setCellValue(r.getName());
            row.createCell(2).setCellValue(r.getLevel());
            row.createCell(3).setCellValue(r.getBranch());
            row.createCell(4).setCellValue(r.getJobTitle());
            row.createCell(5).setCellValue(r.getMonthAndYear());
            row.createCell(6).setCellValue(r.getOrientationClassNumber());
            row.createCell(7).setCellValue(r.getSelfMastery());
            row.createCell(8).setCellValue(r.getBasicEmergencyResponse());
            row.createCell(9).setCellValue(r.getBasicAccounting());
            row.createCell(10).setCellValue(r.getFundamentalsOfCredit());
            row.createCell(11).setCellValue(r.getUnderstandingBankingBusiness());
            row.createCell(12).setCellValue(r.getZenithInternalCourse());
            row.createCell(13).setCellValue(r.getTotalScore());
            row.createCell(14).setCellValue(r.getAverageScore());
            row.createCell(15).setCellValue(r.getPosition());
            row.createCell(16).setCellValue(r.getRemark());
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}
