package com.jjmj.application.data.repository;

import com.jjmj.application.data.entity.Book;
import com.jjmj.application.data.entity.Style;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StyleRepository extends AbstractRepository<Style> {
    @Query("select c from Style c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%'))")
    List<Style> search(@Param("searchTerm") String searchTerm);
}
