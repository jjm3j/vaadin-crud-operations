package com.jjmj.application.data.repository;

import com.jjmj.application.data.entity.Helicopter;
import com.jjmj.application.data.entity.Style;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HelicopterRepository extends AbstractRepository<Helicopter> {
    @Query("select c from Helicopter c " +
            "where lower(c.model) like lower(concat('%', :searchTerm, '%'))")
    List<Helicopter> search(@Param("searchTerm") String searchTerm);
}