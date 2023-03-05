package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.Employee;
import com.jjmj.application.data.entity.Job;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.RegexpValidator;

import java.util.List;

public class EmployeeEditDialog extends EditDialog<Employee> {
    public final TextField firstName = new TextField("Имя");
    public final TextField lastName = new TextField("Фамилия");
    public final TextField phone = new TextField("Телефон");
    public final DatePicker dateOfBirth = new DatePicker("Дата рождения");
    public final ComboBox<Job> jobComboBox = new ComboBox<>("Должность");
    public final Button addJobButton = new Button("Добавить должность");
    public JobEditDialog jobForm = new JobEditDialog();

    public EmployeeEditDialog() {
        super();
        add(createFieldsLayout());
        configureBinder();
    }

    public EmployeeEditDialog(List<Job> jobs) {
        super();
        jobComboBox.setItems(jobs);
        jobComboBox.setItemLabelGenerator(Job::getName);
        add(createFieldsLayout());
        configureBinder();
    }

    private VerticalLayout createFieldsLayout() {
        var fieldsLayout = new VerticalLayout (firstName, lastName, dateOfBirth, phone, jobComboBox, addJobButton);
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
        binder.forField(phone).withValidator(new RegexpValidator(
                "Неверный формат номера",
                "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")).bind(Employee::getPhone,Employee::setPhone);
        binder.forField(dateOfBirth).bind(Employee::getDateOfBirth, Employee::setDateOfBirth);
        binder.forField(jobComboBox).withValidator(value -> !value.getName().isEmpty(),"Поле должно быть заполнено").bind(Employee::getJob,Employee::setJob);
    }
}
