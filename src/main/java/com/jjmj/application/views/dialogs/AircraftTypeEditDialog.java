package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.AircraftType;
import com.vaadin.flow.component.textfield.TextField;

public class AircraftTypeEditDialog extends EditDialog<AircraftType> {
    public final TextField type = new TextField("Тип самолёта");

    public AircraftTypeEditDialog() {
        super();
        add(createFieldsLayout(type));
        configureBinder();
    }

    @Override
    protected AircraftType createEntity() {
        return new AircraftType();
    }

    @Override
    protected void configureBinder() {
        binder.forField(type).withValidator(value -> value != null && !value.isEmpty(), "Поле должно быть заполнено")
                .bind(AircraftType::getName, AircraftType::setName);
    }
}
