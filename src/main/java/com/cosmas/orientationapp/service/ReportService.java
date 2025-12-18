package com.cosmas.orientationapp.service;

import com.cosmas.dto.DashboardStatsDTO;
import com.cosmas.model.Batch;
import com.cosmas.orientationapp.repository.BatchRepo;
import com.cosmas.orientationapp.repository.StudentRepo;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportService {
    private final BatchRepo batchRepo;
    private final StudentRepo studentRepo;

    public ReportService(BatchRepo batchRepo, StudentRepo studentRepo){
        this.batchRepo = batchRepo;
        this.studentRepo = studentRepo;
    }

    public DashboardStatsDTO getDashboardStats(){
        long total = batchRepo.countTotalStudents();
        long pending = batchRepo.countPendingStudents();
        long completed = total - pending;

        double completionPct = (total > 0) ? ((double) completed / total) * 100 : 0;
        double pendingPct = (total > 0) ? ((double) pending / total) * 100 : 0;

        return new DashboardStatsDTO(total, completed, pending, completionPct, pendingPct);
    }

        @Transactional
    public ByteArrayInputStream generatePendingStudentsExcel() throws IOException{
        List<Batch> pendingStudents = batchRepo.findStudentsWithoutResults();

        try(Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream()){
            Sheet sheet = workbook.createSheet("pending Students");

            Row headerRow = sheet.createRow(0);
            String[] headerSubjects = {"Staff Number","Name","Gender","Grade",
                    "Unit","Department","Branch","Resumption Date",
                    "Phone Number","Official Email",
                    "Personal Email", "Qualification",
                    "Remark", " Batch Number" };

            for(int i = 0; i < headerSubjects.length; i++){
                Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headerSubjects[i]);
            }

//            data Rows

            int rowIndex = 1;
            for(Batch stud : pendingStudents){
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(stud.getStaffNumber());
                row.createCell(1).setCellValue(stud.getName());
                row.createCell(2).setCellValue(stud.getGender());
                row.createCell(3).setCellValue(stud.getGrade());
                row.createCell(4).setCellValue(stud.getUnit());
                row.createCell(5).setCellValue(stud.getDepartment());
                row.createCell(6).setCellValue(stud.getBranch());
                row.createCell(7).setCellValue(stud.getResumptionDate());
                row.createCell(8).setCellValue(stud.getPhoneNumber());
                row.createCell(9).setCellValue(stud.getOfficialEmail());
                row.createCell(10).setCellValue(stud.getPersonalEmail());
                row.createCell(11).setCellValue(stud.getQualification());
                row.createCell(12).setCellValue(stud.getRemark());
                row.createCell(13).setCellValue(stud.getBatchNumber());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        }
    }

}
