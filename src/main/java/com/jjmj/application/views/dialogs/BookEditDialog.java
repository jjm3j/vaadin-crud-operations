package com.jjmj.application.views.dialogs;

import com.jjmj.application.data.entity.Book;
import com.jjmj.application.data.entity.Style;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.IntegerRangeValidator;

import java.util.List;

public class BookEditDialog extends EditDialog<Book> {
    public final TextField title = new TextField("Название");
    public final TextField firstName = new TextField("Имя");
    public final TextField lastName = new TextField("Фамилия");
    public final IntegerField count = new IntegerField("Осталось шт.");
    public final ComboBox<Style> styleComboBox = new ComboBox<>("Стиль");
    public final Button addStyleButton = new Button("Добавить стиль");
    public StyleEditDialog styleForm = new StyleEditDialog();

    public BookEditDialog(List<Style> styles) {
        super();
        configureComboBox(styles);
        add(createFieldsLayout(title, firstName, lastName, count, styleComboBox, addStyleButton));
        configureBinder();
    }

    @Override
    protected Book createEntity() {
        return new Book();
    }

    @Override
    protected void configureBinder() {
        binder.forField(title).withValidator(value -> value != null && !value.isEmpty(), "Поле должно быть заполнено").bind(Book::getTitle, Book::setTitle);
        binder.forField(lastName).withValidator(value -> value != null && !value.isEmpty(), "Поле должно быть заполнено").bind(Book::getLastName, Book::setLastName);
        binder.forField(firstName).withValidator(value -> value != null && !value.isEmpty(), "Поле должно быть заполнено").bind(Book::getFirstName, Book::setFirstName);
        binder.forField(count).withValidator(new IntegerRangeValidator("Количество должно быть больше 0", 0, Integer.MAX_VALUE)).bind(Book::getCount, Book::setCount);
        binder.forField(styleComboBox).withValidator(value -> value != null && !value.getName().isEmpty(), "Поле должно быть заполнено").bind(Book::getStyle, Book::setStyle);
    }

    private void configureComboBox(List<Style> styles) {
        styleComboBox.setItems(styles);
        styleComboBox.setItemLabelGenerator(Style::getName);
    }
}
