package com.jjmj.application.data.repository;

import com.jjmj.application.data.entity.Airship;
import com.jjmj.application.data.entity.Style;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AirshipRepository extends AbstractRepository<Airship> {
    @Query("select c from Airship c " +
            "where lower(c.model) like lower(concat('%', :searchTerm, '%'))")
    List<Airship> search(@Param("searchTerm") String searchTerm);
}
