package com.jjmj.application.data.repository;

import com.jjmj.application.data.entity.FuelType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FuelTypeRepository extends AbstractRepository<FuelType> {
    @Query("select c from FuelType c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%'))")
    List<FuelType> search(@Param("searchTerm") String searchTerm);
}
