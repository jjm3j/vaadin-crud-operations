package com.example.application.views.list;

import com.example.application.data.entity.Book;
import com.example.application.data.service.CrmService;
import com.example.application.views.AddBookForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

@Route(value = "")
@PageTitle("Books grid")
public class ListView extends VerticalLayout {
    Grid<Book> bookGrid = new Grid<>(Book.class);
    TextField filterText = new TextField();
    AddBookForm form;
    CrmService service;

    public ListView(CrmService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
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
        bookGrid.setColumns("title", "lastName", "firstName");
        bookGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        bookGrid.asSingleSelect().addValueChangeListener(event -> editBook(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по имени...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        var addContactButton = new Button("Добавить книгу");
        var toolbar = new HorizontalLayout(filterText, addContactButton);

        addContactButton.addClickListener(click -> addBook());
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editBook(Book book) {
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
        service.saveBook(event.getBook());
        updateList();
        closeEditor();
    }

    private void deleteBook(AddBookForm.DeleteEvent event) {
        service.deleteBooks(event.getBook());
        updateList();
        closeEditor();
    }

    private void updateList() {
        bookGrid.setItems(service.findAllBooks(filterText.getValue()));
    }

}
