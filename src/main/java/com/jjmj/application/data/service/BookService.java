package com.jjmj.application.data.service;

import com.jjmj.application.data.entity.book.Book;
import com.jjmj.application.data.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService extends AbstractService<Book> {
    private final BookRepository bookRepository;
    protected BookService(BookRepository repository) {
        super(repository);
        bookRepository = repository;
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

}