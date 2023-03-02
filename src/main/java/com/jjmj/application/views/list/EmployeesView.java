package com.jjmj.application.views.list;

import com.jjmj.application.data.entity.Employee;
import com.jjmj.application.data.service.EmployeeService;
import com.jjmj.application.security.SecurityService;
import com.jjmj.application.data.entity.Role;
import com.jjmj.application.views.AddEmployeeForm;
import com.jjmj.application.views.MainLayout;
import com.jjmj.application.views.dialogs.EditDialogEvents;
import com.jjmj.application.views.dialogs.EmployeeEditDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
@PermitAll
@Route(value = "personnel", layout = MainLayout.class)
@PageTitle("Персонал")
public class EmployeesView extends VerticalLayout {
    Grid<Employee> employeeGrid = new Grid<>(Employee.class);
    TextField filterText = new TextField();
    EmployeeEditDialog form;
    EmployeeService service;
    private final SecurityService securityService;

    public EmployeesView(EmployeeService service, SecurityService securityService) {
        this.service = service;
        this.securityService = securityService;
        addClassName("personnel-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private boolean isUserAdmin() {
        String role = securityService.getAuthenticatedUser()
                .getAuthorities()
                .iterator().next()
                .getAuthority();

        return Role.valueOf(role) == Role.Admin;
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(employeeGrid, form);
        content.setFlexGrow(2, employeeGrid);
        //content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid () {
        employeeGrid.addClassName("book-grid");
        employeeGrid.setSizeFull();
        employeeGrid.setColumns("firstName", "lastName", "position","dateOfBirth","phone");
        employeeGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        employeeGrid.asSingleSelect().addValueChangeListener(event -> editEmployee(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по имени...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        var addContactButton = new Button("Добавить сотрудника");
        if(!isUserAdmin())
            addContactButton.setVisible(false);
        var toolbar = new HorizontalLayout(filterText, addContactButton);

        addContactButton.addClickListener(click -> addEmployee());
        toolbar.addClassName("toolbar-employee");
        return toolbar;
    }

    public void editEmployee(Employee employee) {
        if (!isUserAdmin()) return;
        if (employee == null) {
            closeEditor();
        } else {
            form.setHeaderTitle("Изменить данные сотрудника");
            form.setEntity(employee);
            form.open();
            addClassName("editing-employee");
        }
    }

    private void closeEditor() {
        form.close();
        form.setEntity(null);
        removeClassName("editing-employee");
    }

    private void addEmployee() {
        if (!isUserAdmin()) return;
        employeeGrid.asSingleSelect().clear();
        form.setHeaderTitle("Добавить сотрудника");
        form.setEntity(new Employee());
        form.open();
        addClassName("editing");
    }

    private void configureForm() {
        form = new EmployeeEditDialog();
        form.setWidth("25em");
        form.addListener(EditDialogEvents.SaveEvent.class, this::saveEmployee);
        form.addListener(EditDialogEvents.DeleteEvent.class, this::deleteEmployee);
        form.addListener(EditDialogEvents.CloseEvent.class, e -> closeEditor());
    }

    private void saveEmployee(EditDialogEvents.SaveEvent event) {
        service.add(form.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteEmployee(EditDialogEvents.DeleteEvent event) {
        service.delete(form.getEntity());
        updateList();
        closeEditor();
    }

    private void updateList() {
        employeeGrid.setItems(service.findAll(filterText.getValue()));
    }

}
