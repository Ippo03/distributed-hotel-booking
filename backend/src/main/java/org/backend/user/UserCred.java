package org.backend.user;

public class UserCred {
    public int userId;
    public String username;
    public String hashedPassword;

    public UserCred(int userId, String username, String hashedPassword){
        this.userId = userId;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public int getId(){
        return this.userId;
    }

    public String getUsername(){
        return this.username;
    }

    public String getHashedPassword(){
        return this.hashedPassword;
    }
}
