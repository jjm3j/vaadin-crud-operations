package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.book.Author;
import com.jjmj.application.data.entity.book.Book;
import com.jjmj.application.data.entity.book.Style;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.IntegerRangeValidator;

import java.util.List;

public class BookEditDialog extends EditDialog<Book> {
    public final TextField title = new TextField("Название");

    public final IntegerField count = new IntegerField("Осталось шт.");
    public final ComboBox<Author> authorComboBox = new ComboBox<>("Автор");
    public final ComboBox<Style> styleComboBox = new ComboBox<>("Стиль");
    public final Button addStyleButton = new Button("Добавить стиль");
    public StyleEditDialog styleForm = new StyleEditDialog();

    public BookEditDialog(List<Style> styles, List<Author> authors) {
        super();
        configureStyleComboBox(styles);
        configureAuthorComboBox(authors);
        add(createFieldsLayout(title, count, authorComboBox,styleComboBox, addStyleButton));
        configureBinder();
    }

    @Override
    protected Book createEntity() {
        return new Book();
    }

    @Override
    protected void configureBinder() {
        binder.forField(title).withValidator(value -> !value.isEmpty(), "Поле должно быть заполнено").bind(Book::getTitle, Book::setTitle);
        binder.forField(count).withValidator(new IntegerRangeValidator("Количество должно быть больше 0", 0, Integer.MAX_VALUE)).bind(Book::getCount, Book::setCount);
        binder.forField(styleComboBox).withValidator(value -> !value.getName().isEmpty(),"Поле должно быть заполнено").bind(Book::getStyle, Book::setStyle);
        binder.forField(authorComboBox).withValidator(value -> !value.getName().isEmpty(),"Поле должно быть заполнено").bind(Book::getAuthor, Book::setAuthor);

    }

    private void configureStyleComboBox(List<Style> styles) {
        styleComboBox.setItems(styles);
        styleComboBox.setItemLabelGenerator(Style::getName);
    }

    private void configureAuthorComboBox(List<Author> authors) {
        authorComboBox.setItems(authors);
        authorComboBox.setItemLabelGenerator(Author::toString);
    }
}
