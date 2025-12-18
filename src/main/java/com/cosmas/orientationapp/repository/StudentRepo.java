package com.cosmas.orientationapp.repository;

import com.cosmas.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface StudentRepo extends JpaRepository<Result, String> {
    @Query("SELECT g FROM Result g WHERE LOWER(REPLACE(g.orientationClassNumber, ' ', '')) = LOWER(REPLACE(:orientationClassNumber, ' ', ''))")
    List<Result> findByOrientationClassNumber(String orientationClassNumber);

    @Query("""
SELECT g FROM Result g
WHERE LOWER(REPLACE(g.staffNumber, ' ', '')) =
      LOWER(REPLACE(:staffNumber, ' ', ''))
""")
    Optional<Result> findByStaffNumberIgnoreCase(@Param("staffNumber") String staffNumber);
}
