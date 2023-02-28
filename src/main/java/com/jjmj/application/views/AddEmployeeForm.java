package com.jjmj.application.views;

import com.jjmj.application.data.entity.Employee;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class AddEmployeeForm extends FormLayout {
    TextField position = new TextField("Должность сотрудника");
    TextField lastName = new TextField("Имя сотрудника");
    TextField firstName = new TextField("Фамилия сотрудника");
    TextField phone = new TextField("Телефон");
    DatePicker dateOfBirth = new DatePicker("Дата рождения");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");

    Binder<Employee> binder = new BeanValidationBinder<>(Employee.class);

    private Employee employee;

    public void setEmployee(Employee employee) {
        this.employee = employee;
        binder.readBean(employee);
    }

    public AddEmployeeForm() {
        addClassName("add-employee-form");
        add(position, lastName, firstName, phone, dateOfBirth, createButtonLayout());
        binder.bindInstanceFields(this);
    }

    private HorizontalLayout createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, employee)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    public static abstract class AddEmployeeFormEvent extends ComponentEvent<AddEmployeeForm> {
        private Employee employee;

        protected AddEmployeeFormEvent(AddEmployeeForm source, Employee employee) {
            super(source, false);
            this.employee = employee;
        }

        public Employee getEmployee() {
            return employee;
        }
    }
    public static class SaveEvent extends AddEmployeeFormEvent {
        SaveEvent(AddEmployeeForm source, Employee employee) {
            super(source, employee);
        }
    }

    public static class DeleteEvent extends AddEmployeeFormEvent {
        DeleteEvent(AddEmployeeForm source, Employee employee) {
            super(source, employee);
        }

    }

    public static class CloseEvent extends AddEmployeeFormEvent {
        CloseEvent(AddEmployeeForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(employee);
            fireEvent(new SaveEvent(this, employee));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
