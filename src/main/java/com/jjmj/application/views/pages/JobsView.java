package com.jjmj.application.views.pages;

import com.jjmj.application.data.entity.Job;
import com.jjmj.application.data.entity.Role;
import com.jjmj.application.data.service.JobService;
import com.jjmj.application.security.SecurityService;
import com.jjmj.application.views.MainLayout;
import com.jjmj.application.views.dialogs.EditDialogEvents;
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
@Route(value="jobs", layout = MainLayout.class)
@PageTitle("Должности")
public class JobsView extends VerticalLayout {
    Grid<Job> jobGrid = new Grid<>(Job.class);
    TextField filterText = new TextField();
    JobEditDialog form;
    JobService jobService;
    private final SecurityService securityService;

    public JobsView(JobService jobService, SecurityService securityService) {
        this.jobService = jobService;
        this.securityService = securityService;
        addClassName("list-view");
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
        var content = new HorizontalLayout(jobGrid);
        content.setFlexGrow(2, jobGrid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid () {
        jobGrid.addClassName("book-grid");
        jobGrid.setSizeFull();
        jobGrid.setColumns("name");
        jobGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        jobGrid.asSingleSelect().addValueChangeListener(event -> editJob(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск по названию...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        var addContactButton = new Button("Добавить должность");
        if(!isUserAdmin())
            addContactButton.setVisible(false);
        var toolbar = new HorizontalLayout(filterText, addContactButton);

        addContactButton.addClickListener(click -> addJob());
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editJob(Job job) {
        if (!isUserAdmin()) return;
        if (job == null) {
            closeEditor();
        } else {
            form.setHeaderTitle("Изменить должность");
            form.setEntity(job);
            form.open();
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.close();
        form.setEntity(null);
        removeClassName("editing");
    }

    private void addJob() {
        if (!isUserAdmin()) return;
        form.setHeaderTitle("Добавить должность");
        form.setEntity(new Job());
        form.open();
        addClassName("editing-style");
    }

    private void configureForm() {
        form = new JobEditDialog();
        form.setWidth("25em");
        form.addListener(EditDialogEvents.SaveEvent.class, this::saveJob);
        form.addListener(EditDialogEvents.DeleteEvent.class, this::deleteJob);
        form.addListener(EditDialogEvents.CloseEvent.class, e -> closeEditor());
    }

    private void saveJob(EditDialogEvents.SaveEvent event) {
        jobService.add(form.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteJob(EditDialogEvents.DeleteEvent event) {
        jobService.delete(form.getEntity());
        updateList();
        closeEditor();
    }

    private void updateList() {
        jobGrid.setItems(jobService.findAll(filterText.getValue()));
    }

}
