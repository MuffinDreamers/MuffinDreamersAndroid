package com.github.muffindreamers.rous.model;

/**
 * Created by Lee Mracek on 9/21/17.
 *
 * // TODO Replace this with something that probably reads from a database
 * // or an ORM package
 */

public class User {
    // not final so we can handle changes while logged in
    private String m_email;

    private String m_password;

    private Permissions m_permissions;

    public User(String email, String password, Permissions permissions) {
        this.m_email = email;
        this.m_password = password;
        this.m_permissions = permissions;
    }
}
