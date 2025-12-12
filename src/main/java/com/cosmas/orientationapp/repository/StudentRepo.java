package com.cosmas.orientationapp.repository;

import com.cosmas.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface StudentRepo extends JpaRepository<Grade, String> {
    @Query("SELECT g FROM Grade g WHERE LOWER(REPLACE(g.orientationClassNumber, ' ', '')) = LOWER(REPLACE(:orientationClassNumber, ' ', ''))")
    List<Grade> findByOrientationClassNumber(String orientationClassNumber);
}
