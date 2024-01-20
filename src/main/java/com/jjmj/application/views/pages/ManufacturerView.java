package com.jjmj.application.views.pages;

import com.jjmj.application.data.entity.Manufacturer;
import com.jjmj.application.data.entity.Role;
import com.jjmj.application.data.service.ManufacturerService;
import com.jjmj.application.security.SecurityService;
import com.jjmj.application.views.MainLayout;
import com.jjmj.application.views.dialogs.ManufacturerEditDialog;
import com.jjmj.application.views.dialogs.EditDialogEvents;
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
@Route(value="brands", layout = MainLayout.class)
@PageTitle("Типы самолётов")
public class ManufacturerView extends VerticalLayout {
    Grid<Manufacturer> grid = new Grid<>(Manufacturer.class);
    TextField filterText = new TextField();
    ManufacturerEditDialog form;
    ManufacturerService service;
    private final SecurityService securityService;


    public ManufacturerView(ManufacturerService service, SecurityService securityService) {
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

    private Component getContent() {
        var content = new HorizontalLayout(grid);
        content.setFlexGrow(2, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid () {
        grid.addClassName("book-grid");
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editManufacturer(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по имени...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        var addContactButton = new Button("Добавить тип");
        if(!isUserAdmin())
            addContactButton.setVisible(false);
        var toolbar = new HorizontalLayout(filterText, addContactButton);

        addContactButton.addClickListener(click -> addManufacturer());
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editManufacturer(Manufacturer aircraftType) {
        if (!isUserAdmin()) return;
        if (aircraftType == null) {
            closeEditor();
        } else {
            form.setHeaderTitle("Изменить тип");
            form.setEntity(aircraftType);
            form.open();
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.close();
        form.setEntity(null);
        removeClassName("editing");
    }

    private void addManufacturer() {
        if (!isUserAdmin()) return;
        //bookGrid.asSingleSelect().clear();
        form.setHeaderTitle("Добавить стиль");
        form.setEntity(new Manufacturer());
        form.open();
        addClassName("editing-aircraftType");
    }

    private void configureForm() {
        form = new ManufacturerEditDialog();
        form.setWidth("25em");
        form.addListener(EditDialogEvents.SaveEvent.class, this::saveManufacturer);
        form.addListener(EditDialogEvents.DeleteEvent.class, this::deleteManufacturer);
        form.addListener(EditDialogEvents.CloseEvent.class, e -> closeEditor());
    }


    private void saveManufacturer(EditDialogEvents.SaveEvent event) {
        service.add(form.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteManufacturer(EditDialogEvents.DeleteEvent event) {
        service.delete(form.getEntity());
        updateList();
        closeEditor();
    }

    private void updateList() {
        grid.setItems(service.findAll(filterText.getValue()));
    }

    private boolean isUserAdmin() {
        String role = securityService.getAuthenticatedUser()
                .getAuthorities()
                .iterator().next()
                .getAuthority();

        return Role.valueOf(role) == Role.Admin;
    }
}