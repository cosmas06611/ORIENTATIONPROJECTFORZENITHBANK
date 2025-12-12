package com.cosmas.orientationapp.repository;

import com.cosmas.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentRepo extends JpaRepository<Grade, String> {
}
