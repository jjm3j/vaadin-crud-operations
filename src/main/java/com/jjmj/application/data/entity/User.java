package com.jjmj.application.data.entity;

import com.jjmj.application.data.entity.AbstractEntity;
import com.jjmj.application.data.entity.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User extends AbstractEntity {
    @Column(unique = true)
    private String login;
    private String password;
    private String role;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return Role.valueOf(role);
    }

    public void setRole(Role role) {
        this.role = role.name();
    }

    public String getRoleString() {
        return role;
    }

    public void setRoleString(String role) {
        this.role = Role.valueOf(role).name();
    }
}

