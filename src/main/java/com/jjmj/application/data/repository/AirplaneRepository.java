package com.jjmj.application.data.repository;

import com.jjmj.application.data.entity.Airplane;
import com.jjmj.application.data.entity.Style;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AirplaneRepository extends AbstractRepository<Airplane> {
    @Query("select c from Airplane c " +
            "where lower(c.model) like lower(concat('%', :searchTerm, '%'))")
    List<Airplane> search(@Param("searchTerm") String searchTerm);
}
