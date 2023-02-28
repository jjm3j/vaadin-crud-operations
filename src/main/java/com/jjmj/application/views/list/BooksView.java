package com.jjmj.application.views.list;

import com.jjmj.application.data.entity.Book;
import com.jjmj.application.data.service.BookService;
import com.jjmj.application.security.SecurityService;
import com.jjmj.application.data.entity.Role;
import com.jjmj.application.views.AddBookForm;
import com.jjmj.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value="books", layout = MainLayout.class)
@PageTitle("Книги")
public class BooksView extends VerticalLayout {
    Grid<Book> bookGrid = new Grid<>(Book.class);
    TextField filterText = new TextField();
    AddBookForm form;
    BookService service;
    private final SecurityService securityService;


    public BooksView(BookService service, SecurityService securityService) {
        this.service = service;
        this.securityService = securityService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private boolean isUserAdmin() {
        String role = securityService.getAuthenticatedUser()
                .getAuthorities()
                .iterator().next()
                .getAuthority();

        return Role.valueOf(role) == Role.Admin;
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(bookGrid, form);
        content.setFlexGrow(2, bookGrid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid () {
        bookGrid.addClassName("book-grid");
        bookGrid.setSizeFull();
        bookGrid.setColumns("title", "lastName", "firstName","genre","count");
        bookGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        bookGrid.asSingleSelect().addValueChangeListener(event -> editBook(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по имени...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        var addContactButton = new Button("Добавить книгу");
        if(!isUserAdmin())
            addContactButton.setVisible(false);
        var toolbar = new HorizontalLayout(filterText, addContactButton);

        addContactButton.addClickListener(click -> addBook());
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editBook(Book book) {
        if (!isUserAdmin()) return;
        if (book == null) {
            closeEditor();
        } else {
            form.setBook(book);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setBook(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addBook() {
        if (!isUserAdmin()) return;
        bookGrid.asSingleSelect().clear();
        editBook(new Book());
    }

    private void configureForm() {
        form = new AddBookForm();
        form.setWidth("25em");
        form.addListener(AddBookForm.SaveEvent.class, this::saveBook);
        form.addListener(AddBookForm.DeleteEvent.class, this::deleteBook);
        form.addListener(AddBookForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveBook(AddBookForm.SaveEvent event) {
        service.add(event.getBook());
        updateList();
        closeEditor();
    }

    private void deleteBook(AddBookForm.DeleteEvent event) {
        service.delete(event.getBook());
        updateList();
        closeEditor();
    }

    private void updateList() {
        bookGrid.setItems(service.findAll(filterText.getValue()));
    }

}
