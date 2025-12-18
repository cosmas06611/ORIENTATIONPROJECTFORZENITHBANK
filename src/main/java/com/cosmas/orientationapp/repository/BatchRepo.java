package com.cosmas.orientationapp.repository;

import com.cosmas.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepo extends JpaRepository<Batch, String>{

    @Query("SELECT b.staffNumber FROM Batch b")
    List<String> findAllStaffNumbers();


    void deleteByBatchNumber(int batchNumber);


    long countByBatchNumber(int batchNumber);

    @Query("SELECT b FROM Batch b WHERE b.staffNumber NOT IN (SELECT r.staffNumber FROM Result r)")
    List<Batch> findStudentsWithoutResults();

    @Query("SELECT COUNT(b) FROM Batch b")
    long countTotalStudents();

    @Query("SELECT COUNT(b) FROM Batch b WHERE b.staffNumber NOT IN (SELECT r.staffNumber FROM Result r)")
    long countPendingStudents();


}
