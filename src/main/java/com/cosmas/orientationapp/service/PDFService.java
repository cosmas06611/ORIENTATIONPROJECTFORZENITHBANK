package com.cosmas.orientationapp.service;

import com.cosmas.model.Result;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PDFService {
    public byte[] generateResultPdf(Result result){

        try{
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font bodyFont = new Font(Font.HELVETICA, 11);

            document.add(new Paragraph("Orientation Result Slip", titleFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Staff Number: " + result.getStaffNumber(), bodyFont));
            document.add(new Paragraph("Name: " + result.getName(), bodyFont));
            document.add(new Paragraph("Level: " + result.getLevel(),bodyFont));
            document.add(new Paragraph("Branch: "+ result.getBranch(), bodyFont));
            document.add(new Paragraph("Job Title "+ result.getJobTitle(),bodyFont));
            document.add(new Paragraph("Month & Year: " + result.getMonthAndYear()));

            document.add(new Paragraph(" "));

            document.add(new Paragraph("Course Scores", titleFont));
            document.add(new Paragraph("Self Mastery: " + result.getSelfMastery(),bodyFont));
            document.add(new Paragraph("Basic Emergency Response: " + result.getBasicEmergencyResponse(), bodyFont));
            document.add(new Paragraph("Basic Accounting: " + result.getBasicAccounting(), bodyFont));
            document.add(new Paragraph("Fundamentals of Credit: " + result.getFundamentalsOfCredit(), bodyFont));
            document.add(new Paragraph("Understanding Banking Business: "+ result.getUnderstandingBankingBusiness(), bodyFont));
            document.add(new Paragraph("Zenith Internal Course: "+ result.getZenithInternalCourse(), bodyFont));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total Score: " + result.getTotalScore(), bodyFont));
            document.add(new Paragraph("Average Score: " + result.getAverageScore(), bodyFont));
            document.add(new Paragraph("Position: " + result.getPosition()));
            document.add(new Paragraph("Remark: " + result.getName(), bodyFont));
            document.close();
            return out.toByteArray();
        }catch(Exception e){
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
