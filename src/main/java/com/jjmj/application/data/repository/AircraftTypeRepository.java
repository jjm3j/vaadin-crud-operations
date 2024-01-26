package com.jjmj.application.data.repository;

import com.jjmj.application.data.entity.AircraftType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AircraftTypeRepository extends AbstractRepository<AircraftType> {
    @Query("select c from AircraftType c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%'))")
    List<AircraftType> search(@Param("searchTerm") String searchTerm);
}
