 package com.jjmj.application.data.repository;

import com.jjmj.application.data.entity.FuelType;
import com.jjmj.application.data.entity.Producer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProducerRepository extends AbstractRepository<Producer> {
    @Query("select c from Producer c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%'))")
    List<Producer> search(@Param("searchTerm") String searchTerm);
}
