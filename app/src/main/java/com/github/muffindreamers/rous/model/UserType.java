package com.github.muffindreamers.rous.model;

public enum UserType {
    USER("USER"),
    ADMIN("ADMIN");

    private String str;

    UserType(String permissions) {
       str = permissions;
    }

    public static UserType fromString(String string) {
        switch (string.toUpperCase()) {
            case "ADMIN":
                return UserType.ADMIN;
            case "USER":
            default:
                return UserType.USER;
        }
    }

    public String toString() {
        return str;
    }
}
