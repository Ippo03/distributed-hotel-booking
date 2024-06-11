package org.backend.user;

import org.backend.misc.Util;

import java.io.Serializable;

public class UserData implements Serializable {
    public final int userId;
    public String username;
    public byte[] profilePicture;

    public UserData(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = Util.imageToByteArray(profilePicture, 150, 150, 0.75f);
    }
}
