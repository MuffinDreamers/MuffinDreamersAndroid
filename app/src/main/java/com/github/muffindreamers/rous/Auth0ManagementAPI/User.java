package com.github.muffindreamers.rous.Auth0ManagementAPI;

import java.util.Date;

/**
 * Created by carty on 11/28/2017.
 */

public class User {

    private String userId;
    private String name;
    private String email;
    private boolean verified;
    private Date created;
    private int loginCount;
    private String lastIp;
    private Date lastLogin;
    private boolean blocked;

    public User(String userId, String name, String email, boolean verified, Date created, int loginCount,
                String lastIp, Date lastLogin, boolean blocked) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.verified = verified;
        this.created = created;
        this.loginCount = loginCount;
        this.lastIp = lastIp;
        this.lastLogin = lastLogin;
        this.blocked = blocked;
    }

    public String getUserId() { return userId; }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isVerified() {
        return verified;
    }

    public Date getCreated() {
        return created;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public String getLastIp() {
        return lastIp;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public boolean isBlocked() { return blocked; }

    public void setBlocked(boolean blocked) { this.blocked = blocked; }
}
