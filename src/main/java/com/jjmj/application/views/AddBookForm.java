package com.jjmj.application.views;

import com.jjmj.application.data.entity.Book;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class AddBookForm extends FormLayout {
    TextField title = new TextField("Название книги");
    TextField lastName = new TextField("Фамилия автора");
    TextField firstName = new TextField("Имя автора");
    TextField genre = new TextField("Жанр книги");
    IntegerField count = new IntegerField("Осталось на складе");


    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");

    Binder<Book> binder = new BeanValidationBinder<>(Book.class);

    private Book book;

    public void setBook(Book book) {
        this.book = book;
        binder.readBean(book);
    }

    public AddBookForm() {
       addClassName("add-book-form");
       add(title, lastName, firstName, genre, count, createButtonLayout());
       binder.bindInstanceFields(this);
    }

    private HorizontalLayout createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, book)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    public static abstract class AddBookFormEvent extends ComponentEvent<AddBookForm> {
        private Book book;

        protected AddBookFormEvent(AddBookForm source, Book book) {
            super(source, false);
            this.book = book;
        }

        public Book getBook() {
            return book;
        }
    }
        public static class SaveEvent extends AddBookFormEvent {
            SaveEvent(AddBookForm source, Book book) {
                super(source, book);
            }
        }

        public static class DeleteEvent extends AddBookFormEvent {
            DeleteEvent(AddBookForm source, Book book) {
                super(source, book);
            }

        }

        public static class CloseEvent extends AddBookFormEvent {
            CloseEvent(AddBookForm source) {
                super(source, null);
            }
        }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
        ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(book);
            fireEvent(new SaveEvent(this, book));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
