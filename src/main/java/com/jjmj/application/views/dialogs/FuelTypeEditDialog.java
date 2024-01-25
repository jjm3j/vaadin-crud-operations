package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.FuelType;
import com.vaadin.flow.component.textfield.TextField;

public class FuelTypeEditDialog extends EditDialog<FuelType> {
    public final TextField type = new TextField("Тип топлива");

    public FuelTypeEditDialog() {
        super();
        add(createFieldsLayout(type));
        configureBinder();
    }

    @Override
    protected FuelType createEntity() {
        return new FuelType();
    }

    @Override
    protected void configureBinder() {
        binder.forField(type).withValidator(value -> value != null && !value.isEmpty(), "Поле должно быть заполнено")
                .bind(FuelType::getName, FuelType::setName);
    }
}
