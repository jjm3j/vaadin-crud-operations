package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.Manufacturer;
import com.vaadin.flow.component.textfield.TextField;

public class ManufacturerEditDialog extends EditDialog<Manufacturer> {
    public final TextField type = new TextField("Производитель");

    public ManufacturerEditDialog() {
        super();
        add(createFieldsLayout(type));
        configureBinder();
    }

    @Override
    protected Manufacturer createEntity() {
        return new Manufacturer();
    }

    @Override
    protected void configureBinder() {
        binder.forField(type).withValidator(value -> value != null && !value.isEmpty(), "Поле должно быть заполнено")
                .bind(Manufacturer::getName, Manufacturer::setName);
    }
}
