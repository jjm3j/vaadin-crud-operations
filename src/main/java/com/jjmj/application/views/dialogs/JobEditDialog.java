package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.Job;
import com.vaadin.flow.component.textfield.TextField;

public class JobEditDialog extends EditDialog<Job> {
    public final TextField job = new TextField("Должность");

    public JobEditDialog() {
        super();
        add(createFieldsLayout(job));
        configureBinder();
    }

    @Override
    protected Job createEntity() {
        return new Job();
    }

    @Override
    protected void configureBinder() {
        binder.forField(job).withValidator(value -> !value.isEmpty(), "Поле должно быть заполнено").bind(Job::getName, Job::setName);
    }
}
