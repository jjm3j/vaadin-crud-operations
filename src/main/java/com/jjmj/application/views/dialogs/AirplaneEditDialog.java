package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.AircraftType;
import com.jjmj.application.data.entity.Airplane;
import com.jjmj.application.data.entity.Manufacturer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.IntegerRangeValidator;

import java.util.List;

public class AirplaneEditDialog extends EditDialog<Airplane> {
    final TextField model = new TextField("Модель");
    public final ComboBox<AircraftType> comboBox = new ComboBox<>("Тип самолёта");
    public final ComboBox<Manufacturer> manufacturer = new ComboBox<>("Производитель");

    final IntegerField price = new IntegerField("Цена");
    final IntegerField year = new IntegerField("Год производства");
    final IntegerField maxSpeed = new IntegerField("Максимальная скорость");
    public final Button addAircraftTypeButton = new Button("Добавить тип");
    public final Button addManufacturerButton = new Button("Добавить производителя");
    public AircraftTypeEditDialog typeEditDialog = new AircraftTypeEditDialog();
    public ManufacturerEditDialog manufacturerEditDialog = new ManufacturerEditDialog();

    public AirplaneEditDialog(List<AircraftType> styles, List<Manufacturer> manufacturers) {
        super();
        configureComboBox(styles, manufacturers);
        add(createFieldsLayout(model, comboBox, manufacturer, price, year, maxSpeed, addAircraftTypeButton, addManufacturerButton));
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

    }

    private void configureComboBox(List<AircraftType> types, List<Manufacturer> manufacturers) {
        comboBox.setItems(types);
        comboBox.setItemLabelGenerator(AircraftType::getName);

        manufacturer.setItems(manufacturers);
        manufacturer.setItemLabelGenerator(Manufacturer::getName);
    }
}
