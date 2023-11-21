package com.jjmj.application.data.entity;

import org.hibernate.annotations.Formula;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Style extends AbstractEntity {
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "style")
    @Nullable
    private List<Book> books = new LinkedList<>();

    @Formula("(select count(c.id) from Book c where c.style_id = id)")
    private int booksCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> employees) {
        this.books = books;
    }

    public int getBooksCount(){
        return booksCount;
    }

}
