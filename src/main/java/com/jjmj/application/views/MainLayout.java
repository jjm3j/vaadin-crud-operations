package com.jjmj.application.views;

import com.jjmj.application.security.SecurityService;
import com.jjmj.application.views.pages.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private boolean isUserLoggedIn() {
        return securityService.getAuthenticatedUser() != null;
    }

    private void createHeader() {
        //H1 logo = new H1("Библиотека📚");
        //logo.addClassNames("text-l", "m-m");
        HorizontalLayout header = new HorizontalLayout();

        var logo = getMainTitle();
        var drawerToggle = new DrawerToggle();

        header.add(
                drawerToggle,
                logo,
                getThemeChangeButton(),
                isUserLoggedIn() ? getLogoutButton() : getLoginButton()
        );
        if (!isUserLoggedIn())
            header.add(getRegisterButton());

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");
        addToNavbar(header);
    }

    private void createDrawer() {
        Button link1 = new Button("Топливо");
        link1.addClickListener(e ->
                link1.getUI().ifPresent(ui -> ui.navigate(FuelTypeView.class)));

        Button link3 = new Button("Авиатранспорт");
        link3.addClickListener(e ->
                link3.getUI().ifPresent(ui -> ui.navigate(AirplaneView.class)));

        Button link4 = new Button("Производители");
        link4.addClickListener(e ->
                link4.getUI().ifPresent(ui -> ui.navigate(ManufacturerView.class)));

        Button link5 = new Button("Типы авиатранспортов");
        link5.addClickListener(e ->
                link5.getUI().ifPresent(ui -> ui.navigate(AircraftTypeView.class)));
        Button link6 = new Button("Поставщики");
        link6.addClickListener(e ->
                link6.getUI().ifPresent(ui -> ui.navigate(ProducerView.class)));

        link1.setWidth("10em");
        link3.setWidth("10em");
        link4.setWidth("10em");
        link5.setWidth("10em");
        link6.setWidth("10em");

        var PagesList = new VerticalLayout(link3, link1, link4, link5, link6);
        PagesList.setSpacing(false);
        PagesList.setPadding(true);
        PagesList.setAlignItems(FlexComponent.Alignment.CENTER);
        PagesList.getStyle().set("max-width", "100%");
        PagesList.setSpacing(false);
        addToDrawer(PagesList);
    }

    private Component getMainTitle() {
        Icon icon = new Icon(VaadinIcon.AIRPLANE);
        H1 title = new H1("Первым делом самолёты");
        title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.Vertical.MEDIUM);

        HorizontalLayout appName = new HorizontalLayout(icon, title);
        appName.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER);
        RouterLink logo = new RouterLink(HomeView.class);
        logo.add(appName);

        return logo;
    }

    private Button getLogoutButton() {
        Button button = new Button("Выйти", e -> securityService.logout());
        return button;
    }

    private Button getLoginButton() {
        Button button = new Button("Вход", e -> UI.getCurrent().navigate(LoginView.class));
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return button;
    }

    private Button getRegisterButton() {
        Button button = new Button("Регистрация", e -> UI.getCurrent().navigate(RegistrationView.class));
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return button;
    }

    private Button getThemeChangeButton() {
        Button button = new Button("", new Icon(VaadinIcon.MOON), click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
            } else {
                themeList.add(Lumo.DARK);
            }
        });
        return button;
    }
}