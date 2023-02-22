package com.example.application.data.service;

import com.example.application.data.entity.Book;
import com.example.application.data.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmService {

    private final BookRepository bookRepository;


    public CrmService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAllBooks(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return bookRepository.findAll();
        } else {
            return bookRepository.search(stringFilter);
        }
    }

    public long countBooks() {
        return bookRepository.count();
    }

    public void deleteBooks(Book book) {
        bookRepository.delete(book);
    }

    public void saveBook(Book book) {
        if (book == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        bookRepository.save(book);
    }
}