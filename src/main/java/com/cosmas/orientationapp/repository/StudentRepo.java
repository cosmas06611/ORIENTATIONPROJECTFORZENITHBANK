package com.cosmas.orientationapp.repository;

import com.cosmas.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StudentRepo extends JpaRepository<Grade, String> {
    List<Grade> findByOrientationClassNumber(String orientationClassNumber);
}
