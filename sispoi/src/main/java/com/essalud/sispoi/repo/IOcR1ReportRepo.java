package com.essalud.sispoi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IOcR1ReportRepo extends _IGenericRepo<Object, Integer> {

    @Query(value = "SELECT * FROM oc_r1(:dependencyId, :year, :modification)", nativeQuery = true)
    List<Object[]> getOcR1Report(
        @Param("dependencyId") Integer dependencyId,
        @Param("year") Integer year,
        @Param("modification") Integer modification
    );
    
}
