package com.github.muffindreamers.rous.model;

/**
 * Creates enum of user type
 */
public enum UserType {
    USER("USER"),
    ADMIN("ADMIN"),
    BANNED("BANNED");

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
        if(string == null)
            return null;
        switch (string.toUpperCase()) {
            case "ADMIN":
                return UserType.ADMIN;
            case "BANNED":
                return UserType.BANNED;
            case "USER":
            default:
                return UserType.USER;
        }
    }

    public String toString() {
        return str;
    }
}
