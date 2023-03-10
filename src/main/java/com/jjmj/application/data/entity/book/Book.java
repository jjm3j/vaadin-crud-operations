package com.jjmj.application.data.entity.book;

import com.jjmj.application.data.entity.AbstractEntity;
import com.jjmj.application.data.reader.ReadersBooks;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Book extends AbstractEntity {

    @NotEmpty
    private String title = "";

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull
    private Author author;

    @ManyToOne
    @JoinColumn(name = "style_id")
    @NotNull
    private Style style;

    private int count;

    @OneToMany(mappedBy = "book")
    @Nullable
    private List<ReadersBooks> readersBooks = new LinkedList<>();

    @Nullable
    public List<ReadersBooks> getReadersBooks() {
        return readersBooks;
    }

    public void setReadersBooks(@Nullable List<ReadersBooks> readersBooks) {
        this.readersBooks = readersBooks;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

}
