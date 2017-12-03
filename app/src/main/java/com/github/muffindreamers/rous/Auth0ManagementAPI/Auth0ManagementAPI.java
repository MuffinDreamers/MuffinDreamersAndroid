package com.github.muffindreamers.rous.Auth0ManagementAPI;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Will Carty on 11/27/2017.
 */

public class Auth0ManagementAPI {

    private User[] users;

    private GetAccessTokenTask accessTokenTask;
    private GetUserListTask usersTask;

    private String accessToken;

    /**
     * Constructs the Auth0ManagementAPI and automatically begins to request an access token and get tbe user list
     */
    public Auth0ManagementAPI()
    {
        try {
            accessTokenTask = new GetAccessTokenTask(this::accessTokenCallback);
            accessTokenTask.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Called once the GetAccessTokenTask finishes
     * @param accessToken the access token that was returned from the Auth0 server
     */
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

    /**
     * Called once the GetUserListTask finishes
     * @param users the array of users returned by the Auth0 server
     */
    protected void usersCallback(User[] users) {
        this.users = users;
        Log.d("Auth", "Finished loading users list");
    }

    /**
     * Whether or not we are actively waiting on an access token or user list request
     * Checks whether or not the GetAccessTokenTask and GetUserListTask are running
     * @return Whether or not an Auth0 server request is currently loading
     */
    public boolean isLoading() {
        return accessTokenTask.getStatus() != AsyncTask.Status.FINISHED
                || usersTask.getStatus() != AsyncTask.Status.FINISHED;
    }

    /**
     * Forces the thread to wait for the API to finish loading the access token and user list requests
     */
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

    /**
     * Refreshes the list of users from the Auth0 servers
     */
    public void reloadUsers() {
        usersTask = new GetUserListTask(this::usersCallback);
        usersTask.execute(accessToken);
    }

    /**
     * Gets all the emails corresponding to the current list of users.
     * Automatically calls finishLoading() if needed, to make sure the users list has been loaded
     * Does NOT automatically reload the users list
     * @return A String array of all emails
     */
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

    /**
     * Gets a specific user from the currently saved list of users, specified by email/
     * @param email The email of the User to get
     * @return The user if found, or null if the user is not found
     */
    public User getUser(String email) {
        for(int i = 0; i < users.length; i++) {
            if(users[i].getEmail().equals(email))
                return users[i];
        }
        return null;
    }

    /**
     * Blocks the specified user, so they cannot log in.
     * It doesn't matter if the user is already blocked. This will just block them again (having no practical effect)
     * This function starts the blocking call on a seperate thread, and returns before the request has finished.
     * @param u The user to block. Only the user ID is used
     */
    public void blockUser(User u) {
        try {
            UpdateUserTask blockTask = new UpdateUserTask(u.getUserId(), accessToken);
            blockTask.setFinishedListener(this::updateUserListener);
            blockTask.execute(new JSONObject().put("blocked", true));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unblocks the specified user, so they cannot log in.
     * It doesn't matter if the user is already unblocked. This will just unblock them again (having no practical effect)
     * This function starts the blocking call on a seperate thread, and returns before the request has finished.
     * @param u The user to unblock. Only the user ID is used
     */
    public void unblockUser(User u) {
        try {
            UpdateUserTask unblockTask = new UpdateUserTask(u.getUserId(), accessToken);
            unblockTask.setFinishedListener(this::updateUserListener);
            unblockTask.execute(new JSONObject().put("blocked", false));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Interface for a listener class that is called when a request to update a user's data has completed
     */
    public interface UpdatedUserListener {
        /**
         * Called when a request to update a user's data has completed
         * @param success whether or not the user was successfully updated
         */
        public void onFinished(boolean success);
    }

    private UpdatedUserListener updatedUserListener;

    /**
     * Sets the function to be called when a request to update a user's data has completed
     * @param listener The function to call, implementing UpdatedUserListener interface
     */
    public void setUpdatedUserListener(UpdatedUserListener listener) {
        updatedUserListener = listener;
    }

    /**
     * Called when a request to update a user's data has been completed by an UpdateUserTask
     * @param success whether or not the request succeeded
     */
    private void updateUserListener(boolean success) {
        if(updatedUserListener != null)
            updatedUserListener.onFinished(success);
    }
}
