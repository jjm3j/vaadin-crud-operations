package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.Employee;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.RegexpValidator;

public class EmployeeEditDialog extends EditDialog<Employee> {
    public final TextField firstName = new TextField("Имя");
    public final TextField lastName = new TextField("Фамилия");
    public final TextField position = new TextField("Должность");
    public final TextField phone = new TextField("Телефон");
    public final DatePicker dateOfBirth = new DatePicker("Дата рождения");

    public EmployeeEditDialog() {
        super();
        add(createFieldsLayout());
        configureBinder();
    }

    private VerticalLayout createFieldsLayout() {
        var fieldsLayout = new VerticalLayout (firstName, lastName, position,dateOfBirth,phone);
        //fieldsLayout.expand(title, firstName, lastName, genre, count);
        fieldsLayout.setSpacing(false);
        fieldsLayout.setPadding(false);
        fieldsLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldsLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return fieldsLayout;
    }

    @Override
    protected Employee createEntity() {
        return new Employee();
    }

    @Override
    protected void configureBinder() {
        binder.forField(firstName).withValidator(value -> !value.isEmpty(), "Поле должно быть заполнено").bind(Employee::getFirstName,Employee::setFirstName);
        binder.forField(lastName).withValidator(value -> !value.isEmpty(), "Поле должно быть заполнено").bind(Employee::getLastName,Employee::setLastName);
        binder.forField(position).withValidator(value -> !value.isEmpty(), "Поле должно быть заполнено").bind(Employee::getPosition,Employee::setPosition);
        binder.forField(phone).withValidator(new RegexpValidator(
                "Неверный формат номера",
                "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")).bind(Employee::getPhone,Employee::setPhone);
        binder.forField(dateOfBirth).bind(Employee::getDateOfBirth, Employee::setDateOfBirth);
    }
}
