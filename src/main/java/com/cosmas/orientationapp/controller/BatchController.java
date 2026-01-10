package com.cosmas.orientationapp.controller;

import com.cosmas.model.Batch;
import com.cosmas.orientationapp.service.BatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
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
    @DeleteMapping("batch/staff/{staffNumber}")
    public ResponseEntity <String> deleteByStaffNumber(@PathVariable String staffNumber){
       String staff = batchService.deleteStaffByStaffNumber(staffNumber);
       if(staff == null){
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
       return new ResponseEntity<>(staff, HttpStatus.OK);
    }

//    this method is to get all batch after downloading
    @GetMapping("/get-batch")
    public ResponseEntity<List<Batch>> getAllBatch(){
        List<Batch> gb = batchService.getAllBatch();
      if(gb != null){
          return new ResponseEntity<>(gb, HttpStatus.OK);
      }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update-batch")
    public ResponseEntity<Batch> updateBatch(@RequestBody Batch batch){
        Batch b = batchService.updateBatch(batch);
        return (b != null) ?
        new ResponseEntity<>(b, HttpStatus.ACCEPTED) :
         new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
