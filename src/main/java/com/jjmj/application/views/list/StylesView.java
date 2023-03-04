package com.jjmj.application.views.list;

import com.jjmj.application.data.entity.Role;
import com.jjmj.application.data.entity.Style;
import com.jjmj.application.data.service.StyleService;
import com.jjmj.application.security.SecurityService;
import com.jjmj.application.views.MainLayout;
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
@Route(value="styles", layout = MainLayout.class)
@PageTitle("Стили")
public class StylesView extends VerticalLayout {
    Grid<Style> styleGrid = new Grid<>(Style.class);
    TextField filterText = new TextField();
    StyleEditDialog form;
    StyleService styleService;
    private final SecurityService securityService;


    public StylesView(StyleService styleService, SecurityService securityService) {
        this.styleService = styleService;
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
        HorizontalLayout content = new HorizontalLayout(styleGrid);
        content.setFlexGrow(2, styleGrid);
        //content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid () {
        styleGrid.addClassName("book-grid");
        styleGrid.setSizeFull();
        styleGrid.setColumns("name");
        styleGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        styleGrid.asSingleSelect().addValueChangeListener(event -> editStyle(event.getValue()));
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

        addContactButton.addClickListener(click -> addStyle());
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editStyle(Style style) {
        if (!isUserAdmin()) return;
        if (style == null) {
            closeEditor();
        } else {
            form.setHeaderTitle("Изменить книгу");
            form.setEntity(style);
            form.open();
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.close();
        form.setEntity(null);
        removeClassName("editing");
    }

    private void addStyle() {
        if (!isUserAdmin()) return;
        //bookGrid.asSingleSelect().clear();
        form.setHeaderTitle("Добавить стиль");
        form.setEntity(new Style());
        form.open();
        addClassName("editing-style");
    }

    private void configureForm() {
        form = new StyleEditDialog();
        form.setWidth("25em");
        form.addListener(EditDialogEvents.SaveEvent.class, this::saveStyle);
        form.addListener(EditDialogEvents.DeleteEvent.class, this::deleteStyle);
        form.addListener(EditDialogEvents.CloseEvent.class, e -> closeEditor());
    }


    private void saveStyle(EditDialogEvents.SaveEvent event) {
        styleService.add(form.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteStyle(EditDialogEvents.DeleteEvent event) {
        styleService.delete(form.getEntity());
        updateList();
        closeEditor();
    }

    private void updateList() {
        styleGrid.setItems(styleService.findAll(filterText.getValue()));
    }

}