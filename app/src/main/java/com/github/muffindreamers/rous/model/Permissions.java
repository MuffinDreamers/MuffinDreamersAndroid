package com.github.muffindreamers.rous.model;

public enum Permissions {
    USER("USER"),
    ADMIN("ADMIN");

    private String str;

    Permissions(String permissions) {
       str = permissions;
    }

    public static Permissions fromString(String string) {
        switch (string.toUpperCase()) {
            case "ADMIN":
                return Permissions.ADMIN;
            case "USER":
            default:
                return Permissions.USER;
        }
    }

    public String toString() {
        return str;
    }
}
