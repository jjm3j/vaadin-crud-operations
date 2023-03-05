package com.jjmj.application.data.repository;

import com.jjmj.application.data.entity.Job;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends AbstractRepository<Job> {
    @Query("select c from Job c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%'))")
    List<Job> search(@Param("searchTerm") String searchTerm);
}
