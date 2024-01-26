package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.IntegerRangeValidator;

import java.util.List;

public class AirplaneEditDialog extends EditDialog<Airplane> {
    public final ComboBox<AircraftType> comboBox = new ComboBox<>("Тип самолёта");
    public final ComboBox<Manufacturer> manufacturer = new ComboBox<>("Производитель");
    public final ComboBox<FuelType> fuel = new ComboBox<>("Топливо");
    public final ComboBox<Producer> producerComboBox = new ComboBox<>("Поставщик");
    public final Button addAircraftTypeButton = new Button("Добавить тип");
    public final Button addFuelTypeButton = new Button("Добавить топливо");
    public final Button addManufacturerButton = new Button("Добавить производителя");
    final TextField model = new TextField("Модель");
    final IntegerField price = new IntegerField("Цена");
    final IntegerField year = new IntegerField("Год производства");
    final IntegerField maxSpeed = new IntegerField("Максимальная скорость");
    public AircraftTypeEditDialog typeEditDialog = new AircraftTypeEditDialog();
    public FuelTypeEditDialog fuelTypeEditDialog = new FuelTypeEditDialog();
    public ManufacturerEditDialog manufacturerEditDialog = new ManufacturerEditDialog();

    public AirplaneEditDialog(List<AircraftType> styles, List<Manufacturer> manufacturers, List<FuelType> fuels, List<Producer> producers) {
        super();
        configureComboBox(styles, manufacturers, fuels, producers);
        add(createFieldsLayout(model, comboBox, addAircraftTypeButton, manufacturer, addManufacturerButton, price, year, maxSpeed, fuel, addFuelTypeButton, producerComboBox));
        configureBinder();
    }

    @Override
    protected Airplane createEntity() {
        return new Airplane();
    }

    @Override
    protected void configureBinder() {
        binder.forField(model).withValidator(value -> value != null && !value.isEmpty(), "Поле должно быть заполнено").bind(Airplane::getModel, Airplane::setModel);
        binder.forField(price).withValidator(value -> value != null, "Поле должно быть заполнено").bind(Airplane::getPrice, Airplane::setPrice);
        binder.forField(year).withValidator(value -> value != null, "Поле должно быть заполнено").bind(Airplane::getYearOfManufacture, Airplane::setYearOfManufacture);
        binder.forField(maxSpeed).withValidator(new IntegerRangeValidator("Количество должно быть больше 0", 0, Integer.MAX_VALUE)).bind(Airplane::getMaxSpeed, Airplane::setMaxSpeed);
        binder.forField(comboBox).withValidator(value -> value != null && !value.getName().isEmpty(), "Поле должно быть заполнено").bind(Airplane::getAircraftType, Airplane::setAircraftType);
        binder.forField(manufacturer).withValidator(value -> value != null && !value.getName().isEmpty(), "Поле должно быть заполнено").bind(Airplane::getManufacturer, Airplane::setManufacturer);
        binder.forField(fuel).withValidator(value -> value != null && !value.getName().isEmpty(), "Поле должно быть заполнено").bind(Airplane::getFuelType, Airplane::setFuelType);
        binder.forField(producerComboBox).withValidator(value -> value != null && !value.getName().isEmpty(), "Поле должно быть заполнено").bind(Airplane::getProducer, Airplane::setProducer);

    }

    private void configureComboBox(List<AircraftType> types, List<Manufacturer> manufacturers, List<FuelType> fuels, List<Producer> producers) {
        comboBox.setItems(types);
        comboBox.setItemLabelGenerator(AircraftType::getName);

        manufacturer.setItems(manufacturers);
        manufacturer.setItemLabelGenerator(Manufacturer::getName);

        fuel.setItems(fuels);
        fuel.setItemLabelGenerator(FuelType::getName);

        producerComboBox.setItems(producers);
        producerComboBox.setItemLabelGenerator(producer -> producer.getName() + " " + producer.getEmail());

    }
}
