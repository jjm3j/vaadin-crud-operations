package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.Book;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.IntegerRangeValidator;

public class BookEditDialog extends EditDialog<Book> {
    public final TextField title = new TextField("Название");
    public final TextField firstName = new TextField("Имя");
    public final TextField lastName = new TextField("Фамилия");
    public final TextField genre = new TextField("Жанр");
    public final IntegerField count = new IntegerField("Осталось шт.");

    public BookEditDialog() {
        super();
        add(createFieldsLayout());
        configureBinder();
    }

    private VerticalLayout createFieldsLayout() {
        var fieldsLayout = new VerticalLayout (title, firstName, lastName, genre, count);
        //fieldsLayout.expand(title, firstName, lastName, genre, count);
        fieldsLayout.setSpacing(false);
        fieldsLayout.setPadding(false);
        fieldsLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldsLayout.getStyle().set("width", "300px").set("max-width", "100%");


        return fieldsLayout;
    }

    @Override
    protected Book createEntity() {
        return new Book();
    }

    @Override
    protected void configureBinder() {
        binder.forField(title).withValidator(value -> !value.isEmpty(), "Поле должно быть заполнено").bind(Book::getTitle, Book::setTitle);
        binder.forField(lastName).withValidator(value -> !value.isEmpty(), "Поле должно быть заполнено").bind(Book::getLastName, Book::setLastName);
        binder.forField(firstName).withValidator(value -> !value.isEmpty(), "Поле должно быть заполнено").bind(Book::getFirstName, Book::setFirstName);
        binder.forField(genre).withValidator(value -> !value.isEmpty(), "Поле должно быть заполнено").bind(Book::getGenre, Book::setGenre);
        binder.forField(count).withValidator(new IntegerRangeValidator("Количество должно быть больше 0", 0, Integer.MAX_VALUE)).bind(Book::getCount, Book::setCount);
    }
}
