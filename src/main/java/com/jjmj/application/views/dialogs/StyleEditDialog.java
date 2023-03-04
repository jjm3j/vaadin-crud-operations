package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.Style;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public class StyleEditDialog extends EditDialog<Style> {
    public final TextField genre = new TextField("Жанр");

    public StyleEditDialog() {
        super();
        add(createFieldsLayout());
        configureBinder();
    }


    private VerticalLayout createFieldsLayout() {
        var fieldsLayout = new VerticalLayout (genre);
        //fieldsLayout.expand(title, firstName, lastName, genre, count);
        fieldsLayout.setSpacing(false);
        fieldsLayout.setPadding(false);
        fieldsLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldsLayout.getStyle().set("width", "300px").set("max-width", "100%");


        return fieldsLayout;
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
