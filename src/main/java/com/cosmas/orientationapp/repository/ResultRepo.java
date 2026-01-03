package com.cosmas.orientationapp.repository;

import com.cosmas.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepo extends JpaRepository<Result, String> {
    @Query("SELECT g FROM Result g WHERE LOWER(REPLACE(g.orientationClassNumber, ' ', '')) = LOWER(REPLACE(:orientationClassNumber, ' ', ''))")
    List<Result> findByOrientationClassNumber(String orientationClassNumber);

    @Query("""
SELECT g FROM Result g
WHERE LOWER(REPLACE(g.staffNumber, ' ', '')) =
      LOWER(REPLACE(:staffNumber, ' ', ''))
""")
    Optional<Result> findByStaffNumberIgnoreCase(@Param("staffNumber") String staffNumber);

    @Query("""
    SELECT r FROM Result r
    WHERE LOWER(r.staffNumber) = LOWER(:keyword)
""")
    Result searchResult(@Param("keyword") String keyword);

//    count number of results per remark
@Query("SELECT LOWER(r.remark), COUNT(r) FROM Result r GROUP BY LOWER(r.remark)")
    List<Object[]> countByRemark();

//    Get all results for specific remark
    List<Result> findByRemark(String remark);

}
