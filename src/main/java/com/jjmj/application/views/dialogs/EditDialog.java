package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.AbstractEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public abstract class EditDialog<T extends AbstractEntity> extends Dialog {
    private final Button save = new Button("Сохранить");
    private final Button delete = new Button("Удалить");
    private final Button close = new Button("Отмена");
    @SuppressWarnings("unchecked")
    protected Binder<T> binder = new BeanValidationBinder<>((Class<T>) createEntity().getClass());
    private T entity;

    public EditDialog() {
        super();
        createButtonsLayout();
    }

    protected abstract T createEntity();

    protected abstract void configureBinder();

    protected VerticalLayout createFieldsLayout(Component... components) {
        var layout = new VerticalLayout(components);
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        layout.getStyle().set("max-width", "100%");

        return layout;
    }

    protected void createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new EditDialogEvents.DeleteEvent(this)));
        close.addClickListener(event -> fireEvent(new EditDialogEvents.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        getFooter().add(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(entity);
            fireEvent(new EditDialogEvents.SaveEvent(this));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
        binder.readBean(this.entity);
    }

    @Override
    public <TEvent extends ComponentEvent<?>> Registration addListener(
            Class<TEvent> eventType,
            ComponentEventListener<TEvent> listener
    ) {
        return getEventBus().addListener(eventType, listener);
    }
}
