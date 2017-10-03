package com.github.muffindreamers.rous.model;

import java.io.Serializable;
import java.io.SerializablePermission;

/**
 * Created by Lee Mracek on 9/21/17.
 *
 * // TODO Replace this with something that probably reads from a database
 * // or an ORM package
 */

public class User implements Serializable {
    // not final so we can handle changes while logged in
    private String m_email;

    private Permissions m_permissions;

    private String m_accessToken;

    private String m_fullname;

    public User(String email, String fullname, String accessToken, Permissions
                permissions) {
        this.m_email = email;
        this.m_fullname = fullname;
        this.m_permissions = permissions;
        this.m_accessToken = accessToken;
    }

    public String getAccessToken() {
        return m_accessToken;
    }

    public String getEmail() {
        return m_email;
    }

    public String getFullname() {
        return m_fullname;
    }

    public Permissions getPermissions() {
        return m_permissions;
    }
}
