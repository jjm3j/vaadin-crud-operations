package com.jjmj.application.data.repository;

import com.jjmj.application.data.entity.Manufacturer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManufacturerRepository extends AbstractRepository<Manufacturer> {
    @Query("select c from Manufacturer c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%'))")
    List<Manufacturer> search(@Param("searchTerm") String searchTerm);
}