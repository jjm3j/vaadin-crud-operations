package com.jjmj.application.views.pages;

import com.jjmj.application.data.entity.user.Role;
import com.jjmj.application.data.entity.user.User;
import com.jjmj.application.security.service.UserService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.Lumo;


@Route("register")
@PageTitle("Регистрация")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {
    private final UserService userService;

    private final Binder<User> binder = new BeanValidationBinder<>(User.class);

    private final H2 header = new H2("Регистрация");

    private final TextField login = new TextField("Логин");

    private final PasswordField password = new PasswordField("Пароль");

    private final Select<String> role = new Select<>();

    private final Button register = new Button("Зарегистрироваться");

    private final Button loginPageButton = new Button("Есть аккаунт");


    public RegistrationView(UserService userService) {
        this.userService = userService;
        setSpacing(false);
        setPadding(true);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        var themeButton = createChangeThemeButton();
        add(
                themeButton,
                createHeader(),
                login,
                password,
                role,
                register,
                loginPageButton
        );

        configureBinder();
        configureRoleSelector();
        configureLoginButton();
        configureRegisterButton();
    }

    private void configureBinder() {
        bindField(login, User::getLogin, User::setLogin);
        bindField(password, User::getPassword, User::setPassword);
        bindField(role, User::getRoleString, User::setRoleString);
        binder.addStatusChangeListener(e -> register.setEnabled(binder.isValid()));
    }

    private void bindField(AbstractField<?, String> field, ValueProvider<User, String> valueProvider, Setter<User, String> setterBindTextField) {
        binder.forField(field)
                .withValidator(value -> !value.isEmpty(), "Поле должно быть заполнено")
                .bind(valueProvider, setterBindTextField);
    }

    private void configureLoginButton() {
        loginPageButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        loginPageButton.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));
    }

        private void configureRegisterButton() {
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        register.addClickListener(e -> onCreateUser());
    }

    private void configureRoleSelector() {
        role.setLabel("Роль");
        role.setItems(Role.getNames());
        role.setPlaceholder("Роль...");
    }

    private void onCreateUser() {
        try {
            User user = new User();
            binder.writeBean(user);
            userService.add(user);
            UI.getCurrent().navigate(LoginView.class);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private Button createChangeThemeButton() {
        var button = new Button("",new Icon(VaadinIcon.MOON), click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
            } else {
                themeList.add(Lumo.DARK);
            }
        });
        return button;
    }

    private Component createHeader() {
        var layout = new HorizontalLayout(header);
        layout.setAlignItems(Alignment.START);
        return layout;
    }
}
