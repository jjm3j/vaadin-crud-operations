package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.Airship;
import com.jjmj.application.data.entity.Manufacturer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.IntegerRangeValidator;

import java.util.List;

public class AirshipEditDialog extends EditDialog<Airship> {
    final TextField model = new TextField("Модель");
    public final ComboBox<Manufacturer> manufacturer = new ComboBox<>("Производитель");

    final IntegerField price = new IntegerField("Цена");
    final IntegerField year = new IntegerField("Год производства");
    final IntegerField maxSpeed = new IntegerField("Максимальная скорость");
    public final Button addManufacturerButton = new Button("Добавить производителя");
    public ManufacturerEditDialog manufacturerEditDialog = new ManufacturerEditDialog();

    public AirshipEditDialog(List<Manufacturer> manufacturers) {
        super();
        configureComboBox(manufacturers);
        add(createFieldsLayout(model, manufacturer, price, year, maxSpeed, addManufacturerButton));
        configureBinder();
    }

    @Override
    protected Airship createEntity() {
        return new Airship();
    }

    @Override
    protected void configureBinder() {
        binder.forField(model).withValidator(value -> value != null && !value.isEmpty(), "Поле должно быть заполнено").bind(Airship::getModel, Airship::setModel);
        binder.forField(price).withValidator(value -> value != null, "Поле должно быть заполнено").bind(Airship::getPrice, Airship::setPrice);
        binder.forField(year).withValidator(value -> value != null, "Поле должно быть заполнено").bind(Airship::getYearOfManufacture, Airship::setYearOfManufacture);
        binder.forField(maxSpeed).withValidator(new IntegerRangeValidator("Количество должно быть больше 0", 0, Integer.MAX_VALUE)).bind(Airship::getMaxSpeed, Airship::setMaxSpeed);
        binder.forField(manufacturer).withValidator(value -> value != null && !value.getName().isEmpty(), "Поле должно быть заполнено").bind(Airship::getManufacturer, Airship::setManufacturer);

    }

    private void configureComboBox(List<Manufacturer> manufacturers) {

        manufacturer.setItems(manufacturers);
        manufacturer.setItemLabelGenerator(Manufacturer::getName);
    }
}
