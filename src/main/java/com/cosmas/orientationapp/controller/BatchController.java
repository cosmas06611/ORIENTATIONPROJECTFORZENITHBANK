package com.cosmas.orientationapp.controller;

import com.cosmas.model.Batch;
import com.cosmas.orientationapp.service.BatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService){
        this.batchService = batchService;
    }


    @PostMapping("/batch/upload")
    public ResponseEntity <String> batchUpload(@RequestParam MultipartFile file){
        try{
            batchService.importBatchFromExcel(file);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    @PostMapping("batch/create")
    public ResponseEntity <Batch> createABatch(@RequestBody Batch batch){
        Batch bs = batchService.createANewBatch(batch);
        if(bs != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(bs);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("batch/{batchNumber}")
    public ResponseEntity<?> deleteBatchByBatchNumber(@PathVariable int batchNumber){
        batchService.deleteBatchByBatchNumber(batchNumber);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


//    this delete is for admin to delete staff who resigned before orientation.
    @DeleteMapping("batch/{staffNumber}")
    public ResponseEntity deleteByStaffNumber(@PathVariable String staffNumber){
        String message = batchService.deleteStaffByStaffNumber(staffNumber);
        return ResponseEntity.ok(message);
    }
}
