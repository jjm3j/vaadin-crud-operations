package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.Producer;
import com.vaadin.flow.component.textfield.TextField;

public class ProducerEditDialog extends EditDialog<Producer> {
    public final TextField type = new TextField("Название организации");
    public final TextField address = new TextField("Адрес");
    public final TextField email = new TextField("Электронная почта");
    public final TextField phone = new TextField("Телефон");

    public ProducerEditDialog() {
        super();
        add(createFieldsLayout(type, address, email, phone));
        configureBinder();
    }

    @Override
    protected Producer createEntity() {
        return new Producer();
    }

    @Override
    protected void configureBinder() {
        binder.forField(type).withValidator(value -> value != null && !value.isEmpty(), "Поле должно быть заполнено")
                .bind(Producer::getName, Producer::setName);
        binder.forField(address).withValidator(value -> value != null && !value.isEmpty(), "Поле должно быть заполнено")
                .bind(Producer::getAddress, Producer::setAddress);
        binder.forField(email).withValidator(value -> value != null && !value.isEmpty(), "Поле должно быть заполнено")
                .bind(Producer::getEmail, Producer::setEmail);
        binder.forField(phone).withValidator(value -> value != null && !value.isEmpty(), "Поле должно быть заполнено")
                .bind(Producer::getPhone, Producer::setPhone);
    }
}
