package com.jjmj.application.views.pages;

import com.jjmj.application.data.entity.Airship;
import com.jjmj.application.data.entity.Manufacturer;
import com.jjmj.application.data.entity.Role;
import com.jjmj.application.data.service.AirshipService;
import com.jjmj.application.data.service.ManufacturerService;
import com.jjmj.application.security.SecurityService;
import com.jjmj.application.views.MainLayout;
import com.jjmj.application.views.dialogs.EditDialogEvents;
import com.jjmj.application.views.dialogs.AirshipEditDialog;
import com.jjmj.application.views.dialogs.ManufacturerEditDialog;
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
@Route(value="airships", layout = MainLayout.class)
@PageTitle("Дирижабли")
public class AirshipView extends VerticalLayout {
    Grid<Airship> airplaneGrid = new Grid<>(Airship.class);
    TextField filterText = new TextField();
    AirshipEditDialog form;
    AirshipService airplaneService;
    ManufacturerService manufacturerService;
    private final SecurityService securityService;


    public AirshipView(AirshipService airplaneService, SecurityService securityService, ManufacturerService manufacturerService) {
        this.airplaneService = airplaneService;
        this.securityService = securityService;
        this.manufacturerService = manufacturerService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        configureManufacturerForm();

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
        HorizontalLayout content = new HorizontalLayout(airplaneGrid);
        content.setFlexGrow(2, airplaneGrid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid () {
        airplaneGrid.addClassName("airplane-grid");
        airplaneGrid.setSizeFull();
        airplaneGrid.setColumns("model","price", "yearOfManufacture","maxSpeed");
        airplaneGrid.addColumn(airplane -> {
            Manufacturer manufacturer = airplane.getManufacturer();
            return manufacturer != null ? manufacturer.getName() : "";
        }).setHeader("Manufacturer");        airplaneGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        airplaneGrid.asSingleSelect().addValueChangeListener(event -> editAirship(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по имени...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        var addContactButton = new Button("Добавить вертолёт");
        if(!isUserAdmin())
            addContactButton.setVisible(false);
        var toolbar = new HorizontalLayout(filterText, addContactButton);

        addContactButton.addClickListener(click -> addAirship());
        form.addManufacturerButton.addClickListener(e -> addManufacturer());
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editAirship(Airship airplane) {
        if (!isUserAdmin()) return;
        if (airplane == null) {
            closeEditor();
        } else {
            form.setHeaderTitle("Изменить вертолёт");
            form.setEntity(airplane);
            form.open();
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.close();
        form.setEntity(null);
        removeClassName("editing");
    }

    private void addAirship() {
        if (!isUserAdmin()) return;
        airplaneGrid.asSingleSelect().clear();
        form.setHeaderTitle("Добавить вертолёт");
        form.setEntity(new Airship());
        form.open();
        addClassName("editing");
    }

    private void addManufacturer() {
        if (!isUserAdmin()) return;
        form.manufacturerEditDialog.setHeaderTitle("Добавить производителя");
        form.manufacturerEditDialog.setEntity(new Manufacturer());
        form.manufacturerEditDialog.open();
        addClassName("editing-style");
    }

    private void configureForm() {
        form = new AirshipEditDialog(manufacturerService.findAllManufacturers());
        form.setWidth("25em");
        form.addListener(EditDialogEvents.SaveEvent.class, this::saveAirship);
        form.addListener(EditDialogEvents.DeleteEvent.class, this::deleteAirship);
        form.addListener(EditDialogEvents.CloseEvent.class, e -> closeEditor());
    }

    private void configureManufacturerForm() {
        form.manufacturerEditDialog = new ManufacturerEditDialog();
        form.manufacturerEditDialog.setWidth("25em");
        form.manufacturerEditDialog.addListener(EditDialogEvents.SaveEvent.class, this::saveManufacturer);
        form.manufacturerEditDialog.addListener(EditDialogEvents.DeleteEvent.class, this::deleteManufacturer);
        form.manufacturerEditDialog.addListener(EditDialogEvents.CloseEvent.class, e -> closeManufacturerEditDialog());
    }

    private void closeManufacturerEditDialog() {
        form.manufacturerEditDialog.close();
        form.manufacturerEditDialog.setEntity(null);
        removeClassName("editing-style");
    }

    private void deleteManufacturer(EditDialogEvents.DeleteEvent deleteEvent) {
        manufacturerService.delete(form.manufacturerEditDialog.getEntity());
        closeManufacturerEditDialog();
    }

    private void saveManufacturer(EditDialogEvents.SaveEvent saveEvent) {
        manufacturerService.add(form.manufacturerEditDialog.getEntity());
        closeManufacturerEditDialog();
    }

    private void saveAirship(EditDialogEvents.SaveEvent event) {
        airplaneService.add(form.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteAirship(EditDialogEvents.DeleteEvent event) {
        airplaneService.delete(form.getEntity());
        updateList();
        closeEditor();
    }

    private void updateList() {
        airplaneGrid.setItems(airplaneService.findAll(filterText.getValue()));
    }


}
