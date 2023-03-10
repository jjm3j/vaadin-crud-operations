package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.book.Style;
import com.vaadin.flow.component.textfield.TextField;

public class StyleEditDialog extends EditDialog<Style> {
    public final TextField genre = new TextField("Жанр");

    public StyleEditDialog() {
        super();
        add(createFieldsLayout(genre));
        configureBinder();
    }

    @Override
    protected Style createEntity() {
        return new Style();
    }

    @Override
    protected void configureBinder() {
        binder.forField(genre).withValidator(value -> !value.isEmpty(), "Поле должно быть заполнено").bind(Style::getName, Style::setName);
    }
}
