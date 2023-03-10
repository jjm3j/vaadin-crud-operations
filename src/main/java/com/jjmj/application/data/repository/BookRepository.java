package com.jjmj.application.data.repository;

import com.jjmj.application.data.entity.book.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends AbstractRepository<Book> {
    @Query("select c from Book c " +
            "where lower(c.title) like lower(concat('%', :searchTerm, '%')) " )
    List<Book> search(@Param("searchTerm") String searchTerm);
}

