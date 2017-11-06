package com.github.muffindreamers.rous.model;

/**
 * Creates enum of user type
 */
public enum UserType {
    USER("USER"),
    ADMIN("ADMIN");

    private String str;

    UserType(String permissions) {
       str = permissions;
    }

    /**
     * Assigns enum to user
     * @param string user type selected
     * @return UserType of user
     */
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
