package com.jjmj.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Добро пожаловать!")
@AnonymousAllowed
public class HomeView extends VerticalLayout {
    public HomeView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(createLogo(),createHeader(),creeateDescription());
    }

    private Component createLogo() {
        var icon = new Icon(VaadinIcon.BOOKMARK);
        icon.addClassNames(LumoUtility.FontSize.XXXLARGE, LumoUtility.TextColor.PRIMARY);
        return icon;
    }

    private Component createHeader() {
        var header = new H1("Добро пожаловать!");
        header.addClassNames(
                LumoUtility.Margin.Vertical.NONE,
                LumoUtility.TextAlignment.CENTER
        );

        return header;
    }

    private Component creeateDescription() {
        var description = new Paragraph("Тестовый проект на Vaadin. Приложение определяет предметную область \"библиотека\" и имеет две основных таблицы.");
        return description;
    }
}
