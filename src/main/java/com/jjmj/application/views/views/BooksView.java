package com.jjmj.application.views.views;

import com.jjmj.application.data.entity.Book;
import com.jjmj.application.data.entity.Style;
import com.jjmj.application.data.service.BookService;
import com.jjmj.application.data.service.StyleService;
import com.jjmj.application.security.SecurityService;
import com.jjmj.application.data.entity.Role;
import com.jjmj.application.views.MainLayout;
import com.jjmj.application.views.dialogs.BookEditDialog;
import com.jjmj.application.views.dialogs.EditDialogEvents;
import com.jjmj.application.views.dialogs.StyleEditDialog;
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
    BookEditDialog form;
    BookService bookService;
    StyleService styleService;
    private final SecurityService securityService;


    public BooksView(BookService bookService, StyleService styleService, SecurityService securityService) {
        this.bookService = bookService;
        this.styleService = styleService;
        this.securityService = securityService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        configureStyleForm();

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
        HorizontalLayout content = new HorizontalLayout(bookGrid);
        content.setFlexGrow(2, bookGrid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid () {
        bookGrid.addClassName("book-grid");
        bookGrid.setSizeFull();
        bookGrid.setColumns("title", "lastName", "firstName","count");
        bookGrid.addColumn(book -> book.getStyle().getName()).setHeader("Style");
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
        form.addStyleButton.addClickListener(e -> addStyle());
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editBook(Book book) {
        if (!isUserAdmin()) return;
        if (book == null) {
            closeEditor();
        } else {
            form.setHeaderTitle("Изменить книгу");
            form.setEntity(book);
            form.open();
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.close();
        form.setEntity(null);
        removeClassName("editing");
    }

    private void closeStyleEditor() {
        form.styleForm.close();
        form.styleForm.setEntity(null);
        removeClassName("editing-style");
    }

    private void addBook() {
        if (!isUserAdmin()) return;
        bookGrid.asSingleSelect().clear();
        form.setHeaderTitle("Добавить книгу");
        form.setEntity(new Book());
        form.open();
        addClassName("editing");
    }

    private void addStyle() {
        if (!isUserAdmin()) return;
        form.styleForm.setHeaderTitle("Добавить стиль");
        form.styleForm.setEntity(new Style());
        form.styleForm.open();
        addClassName("editing-style");
    }

    private void configureForm() {
        form = new BookEditDialog(styleService.findAllStyles());
        form.setWidth("25em");
        form.addListener(EditDialogEvents.SaveEvent.class, this::saveBook);
        form.addListener(EditDialogEvents.DeleteEvent.class, this::deleteBook);
        form.addListener(EditDialogEvents.CloseEvent.class, e -> closeEditor());
    }

    private void configureStyleForm() {
        form.styleForm = new StyleEditDialog();
        form.styleForm.setWidth("25em");
        form.styleForm.addListener(EditDialogEvents.SaveEvent.class, this::saveStyle);
        form.styleForm.addListener(EditDialogEvents.DeleteEvent.class, this::deleteStyle);
        form.styleForm.addListener(EditDialogEvents.CloseEvent.class, e -> closeStyleEditor());
    }

    private void saveBook(EditDialogEvents.SaveEvent event) {
        bookService.add(form.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteBook(EditDialogEvents.DeleteEvent event) {
        bookService.delete(form.getEntity());
        updateList();
        closeEditor();
    }

    private void saveStyle(EditDialogEvents.SaveEvent event) {
        styleService.add(form.styleForm.getEntity());
        updateComboBox();
        closeStyleEditor();
    }

    private void deleteStyle(EditDialogEvents.DeleteEvent event) {
        styleService.delete(form.styleForm.getEntity());
        updateComboBox();
        closeStyleEditor();
    }

    private void updateList() {
        bookGrid.setItems(bookService.findAll(filterText.getValue()));
    }
    private void updateComboBox() {
        form.styleComboBox.setItems(styleService.findAllStyles());
        form.styleComboBox.setItemLabelGenerator(Style::getName);
    }

}
