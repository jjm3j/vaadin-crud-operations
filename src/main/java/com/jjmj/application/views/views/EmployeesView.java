package com.jjmj.application.views.views;

import com.jjmj.application.data.entity.Employee;
import com.jjmj.application.data.entity.Job;
import com.jjmj.application.data.service.EmployeeService;
import com.jjmj.application.data.service.JobService;
import com.jjmj.application.security.SecurityService;
import com.jjmj.application.data.entity.Role;
import com.jjmj.application.views.MainLayout;
import com.jjmj.application.views.dialogs.EditDialogEvents;
import com.jjmj.application.views.dialogs.EmployeeEditDialog;
import com.jjmj.application.views.dialogs.JobEditDialog;
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
    EmployeeService employeeService;
    JobService jobService;
    private final SecurityService securityService;

    public EmployeesView(EmployeeService employeeService, JobService jobService, SecurityService securityService) {
        this.employeeService = employeeService;
        this.jobService = jobService;
        this.securityService = securityService;
        addClassName("personnel-view");
        setSizeFull();
        configureGrid();
        configureForm();
        configureJobForm();

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
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid () {
        employeeGrid.addClassName("book-grid");
        employeeGrid.setSizeFull();
        employeeGrid.setColumns("firstName", "lastName","dateOfBirth","phone");
        employeeGrid.addColumn(employee -> employee.getJob().getName()).setHeader("Job");
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
        form.addJobButton.addClickListener(e -> addJob());
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

    private void closeJobEditor() {
        form.jobForm.close();
        form.jobForm.setEntity(null);
    }

    private void addEmployee() {
        if (!isUserAdmin()) return;
        employeeGrid.asSingleSelect().clear();
        form.setHeaderTitle("Добавить сотрудника");
        form.setEntity(new Employee());
        form.open();
        addClassName("editing");
    }

    private void addJob() {
        if (!isUserAdmin()) return;
        form.jobForm.setHeaderTitle("Добавить должность");
        form.jobForm.setEntity(new Job());
        form.jobForm.open();
        addClassName("editing-style");
    }

    private void configureForm() {
        form = new EmployeeEditDialog(jobService.findAllJobs());
        form.setWidth("25em");
        form.addListener(EditDialogEvents.SaveEvent.class, this::saveEmployee);
        form.addListener(EditDialogEvents.DeleteEvent.class, this::deleteEmployee);
        form.addListener(EditDialogEvents.CloseEvent.class, e -> closeEditor());
    }

    private void configureJobForm() {
        form.jobForm = new JobEditDialog();
        form.jobForm.setWidth("25em");
        form.jobForm.addListener(EditDialogEvents.SaveEvent.class, this::saveJob);
        form.jobForm.addListener(EditDialogEvents.DeleteEvent.class, this::deleteJob);
        form.jobForm.addListener(EditDialogEvents.CloseEvent.class, e -> closeJobEditor());
    }

    private void saveEmployee(EditDialogEvents.SaveEvent event) {
        employeeService.add(form.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteEmployee(EditDialogEvents.DeleteEvent event) {
        employeeService.delete(form.getEntity());
        updateList();
        closeEditor();
    }

    private void saveJob(EditDialogEvents.SaveEvent event) {
        jobService.add(form.jobForm.getEntity());
        updateComboBox();
        closeJobEditor();
    }

    private void deleteJob(EditDialogEvents.DeleteEvent event) {
        jobService.delete(form.jobForm.getEntity());
        updateComboBox();
        closeJobEditor();
    }

    private void updateList() {
        employeeGrid.setItems(employeeService.findAll(filterText.getValue()));
    }
    private void updateComboBox() {
        form.jobComboBox.setItems(jobService.findAllJobs());
        form.jobComboBox.setItemLabelGenerator(Job::getName);
    }
}
