package com.jjmj.application.data.service;

import com.jjmj.application.data.entity.Book;
import com.jjmj.application.data.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService extends AbstractService<Book> {
    protected BookService(BookRepository repository) {
        super(repository);
    }
}