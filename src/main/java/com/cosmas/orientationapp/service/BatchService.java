package com.cosmas.orientationapp.service;

import com.cosmas.model.Batch;
import com.cosmas.orientationapp.repository.BatchRepo;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BatchService {

    private final BatchRepo batchRepo;

    public BatchService(BatchRepo batchRepo){
        this.batchRepo = batchRepo;
    }

    @Transactional
    public String importBatchFromExcel(MultipartFile file){

       Set<String> existingStaffNumbers = new HashSet<>(batchRepo.findAllStaffNumbers());
       List<Batch> batchList = new ArrayList<>();
       int duplicateCount = 0;
       int successCount = 0;

       try(
               InputStream openStreamForExcel = file.getInputStream();
               Workbook workbook = WorkbookFactory.create(openStreamForExcel)
       ){
           Sheet sheet = workbook.getSheetAt(0);
           Iterator<Row> rows = sheet.iterator();
           while(rows.hasNext()){
               Row row = rows.next();
               String staffNumber = getString(row, 0);

               if(isHeader(staffNumber) || !isValidStaffNumber(staffNumber)){
                   continue;
               }

               if(existingStaffNumbers.contains(staffNumber)){
                   duplicateCount++;
                   continue;
               }

               Batch newEntry = new Batch();
               newEntry.setStaffNumber(staffNumber);
               newEntry.setName(getString(row, 1));
               newEntry.setGender(getString(row, 2));
               newEntry.setGrade(getString(row,3));
               newEntry.setUnit(getString(row, 4));
               newEntry.setDepartment(getString(row,5));
               newEntry.setBranch(getString(row, 6));
               Cell cell = row.getCell(7);

               if (cell != null) {
                   switch (cell.getCellType()) {

                       case NUMERIC:
                           if (DateUtil.isCellDateFormatted(cell)) {
                               newEntry.setResumptionDate(
                                       cell.getDateCellValue()
                                               .toInstant()
                                               .atZone(ZoneId.systemDefault())
                                               .toLocalDate()
                               );
                           }
                           break;

                       case STRING:
                           String dateStr = cell.getStringCellValue().trim();
                           if (!dateStr.isEmpty()) {
                               DateTimeFormatter formatter =
                                       DateTimeFormatter.ofPattern("dd-MM-yyyy");
                               newEntry.setResumptionDate(
                                       LocalDate.parse(dateStr, formatter)
                               );
                           }
                           break;

                       default:
                           // do nothing or log invalid date
                           break;
                   }
               }
               newEntry.setPhoneNumber(getString(row,8));
               newEntry.setOfficialEmail(getString(row, 9));
               newEntry.setPersonalEmail(getString(row,10));
               newEntry.setQualification(getString(row,11));
               newEntry.setRemark(getString(row,12));
               newEntry.setBatchNumber(getInt(row, 13));

               existingStaffNumbers.add(staffNumber);
               batchList.add(newEntry);
               successCount++;


           }
           if(!batchList.isEmpty()){
               batchRepo.saveAll(batchList);
           }

       } catch (Exception e) {
           throw new RuntimeException("The batch failed to upload " + e.getMessage());
       }
        return String.format("upload successful! %d records added, %d duplicate found and skipped", successCount, duplicateCount);
    }

//    Helpers
//    public boolean isAValidStaffNum(String staffNum){
//        if(staffNum == null) return false;
//
//        String removeWhiteSpace = staffNum.trim();
//
//        if(removeWhiteSpace.equalsIgnoreCase("staff number")) return false;
//        if(removeWhiteSpace.equalsIgnoreCase(("staffnumber"))) return false;
//
//        return removeWhiteSpace.matches("[A-Za-z0-9]+");
//    }


private boolean isHeader(String staffNum){
        if(staffNum == null) return false;

        String removedSpace = staffNum.trim();

        return removedSpace.equalsIgnoreCase("staff number") || removedSpace.equalsIgnoreCase("Staffnumber");
}

private boolean isValidStaffNumber(String value){
        if(value == null) return false;

    return value.trim().matches("[A-Za-z0-9]+");
}


    private String getString(Row row, int index){
        Cell cell = row.getCell(index, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        if(cell.getCellType() == CellType.STRING){
            return cell.getStringCellValue().trim();
        }
        if(cell.getCellType() == CellType.NUMERIC){
            return String.valueOf((long) cell.getNumericCellValue());
        }
        return null;
    }

private int getInt(Row row, int index){
        Cell cell = row.getCell(index, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if(cell == null) return 0;
        if(cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        }
        if(cell.getCellType() == CellType.STRING)
            try{
                return Integer.parseInt(cell.getStringCellValue().trim());
            }catch(NumberFormatException e){
                return 0;
            }
        return 0;
}

    public Batch createANewBatch(Batch batch) {
        Batch bt = batchRepo.save(batch);
        return bt;
    }

    @Transactional
    public void deleteBatchByBatchNumber(int batchNumber) {
        try {
            // Check if the batch exists first
            long count = batchRepo.countByBatchNumber(batchNumber);

            if (count == 0) {
                throw new RuntimeException("No records found for batch number: " + batchNumber);
            }

            // 2. Perform the deletion
            batchRepo.deleteByBatchNumber(batchNumber);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete batch: " + e.getMessage(), e);
        }
    }

    @Transactional
    public String deleteStaffByStaffNumber(String staffNumber){
      if(!batchRepo.existsById(staffNumber)){
          return String.format(" %s does not exist", staffNumber);
      }
      batchRepo.deleteById(staffNumber);
      return "Staff member " + staffNumber + " has been successfully removed from the batch.";
    }

    public List<Batch> getAllBatch() {
       return  batchRepo.findAll();
    }

    public Batch updateBatch(Batch batch) {
        return batchRepo.save(batch);
    }
}



