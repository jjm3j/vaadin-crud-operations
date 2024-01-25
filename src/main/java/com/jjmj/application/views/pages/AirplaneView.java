package com.jjmj.application.views.pages;

import com.jjmj.application.data.entity.*;
import com.jjmj.application.data.service.*;
import com.jjmj.application.security.SecurityService;
import com.jjmj.application.views.MainLayout;
import com.jjmj.application.views.dialogs.*;
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
@Route(value="airplanes", layout = MainLayout.class)
@PageTitle("Книги")
public class AirplaneView extends VerticalLayout {
    Grid<Airplane> airplaneGrid = new Grid<>(Airplane.class);
    TextField filterText = new TextField();
    AirplaneEditDialog form;
    AirplaneService airplaneService;
    AircraftTypeService aircraftTypeService;
    FuelTypeService fuelTypeService;
    ManufacturerService manufacturerService;
    ProducerService producerService;
    private final SecurityService securityService;


    public AirplaneView(AirplaneService airplaneService, AircraftTypeService aircraftTypeService, SecurityService securityService,
                        ManufacturerService manufacturerService, FuelTypeService fuelTypeService, ProducerService producerService) {
        this.airplaneService = airplaneService;
        this.aircraftTypeService = aircraftTypeService;
        this.securityService = securityService;
        this.manufacturerService = manufacturerService;
        this.fuelTypeService = fuelTypeService;
        this.producerService = producerService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        configureAircraftTypeForm();
        configureFuelTypeForm();
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
        airplaneGrid.addColumn(airplane -> airplane.getAircraftType().getName()).setHeader("AircraftType");
        airplaneGrid.addColumn(airplane -> {
            FuelType fuel = airplane.getFuelType();
            return fuel != null ? fuel.getName() : "";
        }).setHeader("FuelType");
        airplaneGrid.addColumn(airplane -> {
            Manufacturer manufacturer = airplane.getManufacturer();
            return manufacturer != null ? manufacturer.getName() : "";
        }).setHeader("Manufacturer");
        airplaneGrid.addColumn(airplane -> {
            Producer producer = airplane.getProducer();
            return producer != null ? producer.getName() : "";
        }).setHeader("Поставщик");
        airplaneGrid.addColumn(airplane -> {
            Producer producer = airplane.getProducer();
            return producer != null ? producer.getAddress() : "";
        }).setHeader("Адрес поставщика");
        airplaneGrid.addColumn(airplane -> {
            Producer producer = airplane.getProducer();
            return producer != null ? producer.getPhone() : "";
        }).setHeader("Номер поставщика");
        airplaneGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        airplaneGrid.asSingleSelect().addValueChangeListener(event -> editAirplane(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по имени...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        var addContactButton = new Button("Добавить самолёт");
        if(!isUserAdmin())
            addContactButton.setVisible(false);
        var toolbar = new HorizontalLayout(filterText, addContactButton);

        addContactButton.addClickListener(click -> addAirplane());
        form.addAircraftTypeButton.addClickListener(e -> addAircraftType());
        form.addFuelTypeButton.addClickListener(e -> addFuelType());
        form.addManufacturerButton.addClickListener(e -> addManufacturer());
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editAirplane(Airplane airplane) {
        if (!isUserAdmin()) return;
        if (airplane == null) {
            closeEditor();
        } else {
            form.setHeaderTitle("Изменить самолёт");
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

    private void closeAircraftTypeEditor() {
        form.typeEditDialog.close();
        form.typeEditDialog.setEntity(null);
        removeClassName("editing-style");
    }

    private void addAirplane() {
        if (!isUserAdmin()) return;
        airplaneGrid.asSingleSelect().clear();
        form.setHeaderTitle("Добавить самолёт");
        form.setEntity(new Airplane());
        form.open();
        addClassName("editing");
    }

    private void addAircraftType() {
        if (!isUserAdmin()) return;
        form.typeEditDialog.setHeaderTitle("Добавить стиль");
        form.typeEditDialog.setEntity(new AircraftType());
        form.typeEditDialog.open();
        addClassName("editing-style");
    }

    private void addFuelType() {
        if (!isUserAdmin()) return;
        form.fuelTypeEditDialog.setHeaderTitle("Добавить тип топлива");
        form.fuelTypeEditDialog.setEntity(new FuelType());
        form.fuelTypeEditDialog.open();
        addClassName("editing-style");
    }

    private void addManufacturer() {
        if (!isUserAdmin()) return;
        form.manufacturerEditDialog.setHeaderTitle("Добавить производителя");
        form.manufacturerEditDialog.setEntity(new Manufacturer());
        form.manufacturerEditDialog.open();
        addClassName("editing-style");
    }

    private void configureForm() {
        form = new AirplaneEditDialog(aircraftTypeService.findAllTypes(), manufacturerService.findAllManufacturers(), fuelTypeService.findAllTypes(), producerService.findAllTypes());
        form.setWidth("25em");
        form.addListener(EditDialogEvents.SaveEvent.class, this::saveAirplane);
        form.addListener(EditDialogEvents.DeleteEvent.class, this::deleteAirplane);
        form.addListener(EditDialogEvents.CloseEvent.class, e -> closeEditor());
    }

    private void configureAircraftTypeForm() {
        form.typeEditDialog = new AircraftTypeEditDialog();
        form.typeEditDialog.setWidth("25em");
        form.typeEditDialog.addListener(EditDialogEvents.SaveEvent.class, this::saveAircraftType);
        form.typeEditDialog.addListener(EditDialogEvents.DeleteEvent.class, this::deleteAircraftType);
        form.typeEditDialog.addListener(EditDialogEvents.CloseEvent.class, e -> closeAircraftTypeEditor());
    }

    private void configureFuelTypeForm() {
        form.fuelTypeEditDialog = new FuelTypeEditDialog();
        form.fuelTypeEditDialog.setWidth("25em");
        form.fuelTypeEditDialog.addListener(EditDialogEvents.SaveEvent.class, this::saveFuelType);
        form.fuelTypeEditDialog.addListener(EditDialogEvents.DeleteEvent.class, this::deleteFuelType);
        form.fuelTypeEditDialog.addListener(EditDialogEvents.CloseEvent.class, e -> closeFuelTypeEditor());
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

    private void closeFuelTypeEditor() {
        form.manufacturerEditDialog.close();
        form.manufacturerEditDialog.setEntity(null);
        removeClassName("editing-style");
    }

    private void deleteManufacturer(EditDialogEvents.DeleteEvent deleteEvent) {
        manufacturerService.delete(form.manufacturerEditDialog.getEntity());
        updateComboBox();
        closeManufacturerEditDialog();
    }

    private void saveManufacturer(EditDialogEvents.SaveEvent saveEvent) {
        manufacturerService.add(form.manufacturerEditDialog.getEntity());
        updateComboBox();
        closeManufacturerEditDialog();
    }

    private void saveAirplane(EditDialogEvents.SaveEvent event) {
        airplaneService.add(form.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteAirplane(EditDialogEvents.DeleteEvent event) {
        airplaneService.delete(form.getEntity());
        updateList();
        closeEditor();
    }

    private void saveAircraftType(EditDialogEvents.SaveEvent event) {
        aircraftTypeService.add(form.typeEditDialog.getEntity());
        updateComboBox();
        closeAircraftTypeEditor();
    }

    private void deleteAircraftType(EditDialogEvents.DeleteEvent event) {
        aircraftTypeService.delete(form.typeEditDialog.getEntity());
        updateComboBox();
        closeAircraftTypeEditor();
    }

    private void saveFuelType(EditDialogEvents.SaveEvent event) {
        fuelTypeService.add(form.fuelTypeEditDialog.getEntity());
        updateComboBox();
        closeFuelTypeEditor();
    }

    private void deleteFuelType(EditDialogEvents.DeleteEvent event) {
        fuelTypeService.delete(form.fuelTypeEditDialog.getEntity());
        updateComboBox();
        closeFuelTypeEditor();
    }

    private void updateList() {
        airplaneGrid.setItems(airplaneService.findAll(filterText.getValue()));
    }
    private void updateComboBox() {
        form.comboBox.setItems(aircraftTypeService.findAllTypes());
        form.comboBox.setItemLabelGenerator(AircraftType::getName);
    }

}
