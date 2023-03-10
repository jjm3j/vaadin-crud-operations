package com.jjmj.application.data.entity.user;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    User, Admin;

    public static List<String> getNames() {
        List<String> names = new ArrayList<>(Role.values().length);
        for (Role role : Role.values()) names.add(role.name());
        return names;
    }
}