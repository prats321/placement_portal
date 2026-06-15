package com.placement.portal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The database access layer.
 *
 * Just by extending JpaRepository<Student, Long>, we automatically get
 * save(), findAll(), findById(), deleteById(), etc. — no implementation needed.
 * Spring creates the actual object for us at runtime.
 *
 * <Student, Long> = "this repository manages Student rows whose id type is Long".
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * One query that covers all four search cases from the original code:
     *  - empty keyword  -> the keyword filter is skipped (first line is TRUE)
     *  - minCgpa == 0   -> the cgpa filter is skipped (last line is TRUE)
     * LIKE + LOWER(...) gives case-insensitive matching (same as Postgres ILIKE).
     */
    @Query("""
            SELECT s FROM Student s
            WHERE (:keyword = ''
                   OR LOWER(s.skills) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(s.name)   LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:minCgpa = 0 OR s.cgpa >= :minCgpa)
            """)
    List<Student> search(@Param("keyword") String keyword,
                         @Param("minCgpa") double minCgpa);
}
