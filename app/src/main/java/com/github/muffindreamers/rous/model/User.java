package com.github.muffindreamers.rous.model;

import java.io.Serializable;
/*import java.io.SerializablePermission;*/

/**
 * Created by Lee Mracek on 9/21/17.
 *
 * // TODO Replace this with something that probably reads from a database
 * // or an ORM package
 */

public class User implements Serializable {
    // not final so we can handle changes while logged in
    private String m_email;

    private UserType m_permissions;

    private String m_accessToken;

    private String m_fullName;

    /**
     * Creates a user instance
     * @param email email of user
     * @param fullName name of user
     * @param accessToken token from auth0
     * @param permissions user type
     */
    public User(String email, String fullName, String accessToken, UserType
                permissions) {
        this.m_email = email;
        this.m_fullName = fullName;
        this.m_permissions = permissions;
        this.m_accessToken = accessToken;
    }

    /**
     * Gets access token
     * @return string of token
     */
    public String getAccessToken() {
        return m_accessToken;
    }

    /**
     * Gets email of user
     * @return string of email
     */
    public String getEmail() {
        return m_email;
    }

    /**
     * Gets name of user
     * @return string of full name
     */
    public String getFullName() {
        return m_fullName;
    }

    /**
     * Gets user type
     * @return UserType of permissions
     */
    public UserType getPermissions() {
        return m_permissions;
    }
}
