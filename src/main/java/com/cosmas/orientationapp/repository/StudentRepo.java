package com.cosmas.orientationapp.repository;

import com.cosmas.orientationapp.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface StudentRepo extends JpaRepository<Grade, String> {
    @Query("SELECT g FROM Grade g WHERE LOWER(REPLACE(g.orientationClassNumber, ' ', '')) = LOWER(REPLACE(:orientationClassNumber, ' ', ''))")
    List<Grade> findByOrientationClassNumber(String orientationClassNumber);

    @Query("""
SELECT g FROM Grade g
WHERE LOWER(REPLACE(g.staffNumber, ' ', '')) =
      LOWER(REPLACE(:staffNumber, ' ', ''))
""")
    Optional<Grade> findByStaffNumberIgnoreCase(@Param("staffNumber") String staffNumber);
}
