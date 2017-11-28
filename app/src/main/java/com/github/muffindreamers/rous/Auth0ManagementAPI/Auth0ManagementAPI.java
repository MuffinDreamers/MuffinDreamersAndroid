package com.github.muffindreamers.rous.Auth0ManagementAPI;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by carty on 11/27/2017.
 */

public class Auth0ManagementAPI {

    private User[] users;

    private GetAccessTokenTask accessTokenTask;
    private GetUserListTask usersTask;

    private String accessToken;

    public Auth0ManagementAPI()
    {
        try {
            accessTokenTask = new GetAccessTokenTask(this::accessTokenCallback);
            accessTokenTask.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void accessTokenCallback(String accessToken) {
        try {
            this.accessToken = accessToken;
            if(usersTask == null) {
                usersTask = new GetUserListTask(this::usersCallback);
                usersTask.execute(accessToken);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void usersCallback(User[] users) {
        this.users = users;
        if(loadedUsersListener != null) {
            loadedUsersListener.onFinished();
        }
        Log.d("Auth", "Finished loading users list");
    }

    public boolean isLoading() {
        return accessTokenTask.getStatus() != AsyncTask.Status.FINISHED
                || usersTask.getStatus() != AsyncTask.Status.FINISHED;
    }

    //Forces this to wait for loading to complete
    public void finishLoading() {
        try {
            if(accessTokenTask.getStatus() != AsyncTask.Status.FINISHED) {
                accessTokenCallback(accessTokenTask.get());
            }
            if(usersTask.getStatus() != AsyncTask.Status.FINISHED) {
                usersCallback(usersTask.get());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void reloadUsers() {
        usersTask = new GetUserListTask(this::usersCallback);
        usersTask.execute(accessToken);
    }

    //Gets an array of all user emails
    public String[] getEmails() {
        if(isLoading())
            finishLoading();

        ArrayList<String> emails = new ArrayList<>(users.length);
        for(int i = 0; i < users.length; i++) {
            User u = users[i];
            emails.add(u.getEmail());
        }

        return emails.toArray(new String[0]);
    }

    public User getUser(String email) {
        for(int i = 0; i < users.length; i++) {
            if(users[i].getEmail().equals(email))
                return users[i];
        }
        return null;
    }

    public User getUser(int index) {
        return users[index];
    }

    public void blockUser(User u) {
        try {
            UpdateUserTask blockTask = new UpdateUserTask(u.getUserId(), accessToken);
            blockTask.setFinishedListener(this::updateUserListener);
            blockTask.execute(new JSONObject().put("blocked", true));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void unblockUser(User u) {
        try {
            UpdateUserTask unblockTask = new UpdateUserTask(u.getUserId(), accessToken);
            unblockTask.setFinishedListener(this::updateUserListener);
            unblockTask.execute(new JSONObject().put("blocked", false));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface UpdatedUserListener {
        public void onFinished(boolean success);
    }

    private UpdatedUserListener updatedUserListener;

    public void setUpdatedUserListener(UpdatedUserListener listener) {
        updatedUserListener = listener;
    }
    private void updateUserListener(boolean success) {
        if(updatedUserListener != null)
            updatedUserListener.onFinished(success);
    }



    public interface LoadedUsersListener {
        public void onFinished();
    }

    private LoadedUsersListener loadedUsersListener;

    public void setLoadedUsersListener(LoadedUsersListener listener) {
        loadedUsersListener = listener;
    }
}
