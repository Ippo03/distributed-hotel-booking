package org.backend.modules;

import org.backend.misc.RoomInfo;
import org.backend.user.UserData;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Review implements Serializable {
    UserData userData;
    RoomInfo roomInfo;
    int rating;
    String comment;
    Date date;

    public Review(UserData userData,  RoomInfo roomInfo, int rating, String comment, Date date) {
        this.userData = userData;
        this.roomInfo = roomInfo;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    public UserData getUserData() {
        return userData;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public int hashCode() {
        return this.getRoomInfo().hashCode();
    }

    @Override
    public String toString() {
        return STR."Review{userData=\{userData}, roomInfo=\{roomInfo.getRoomId()} - - - \{roomInfo.getRoomName()}, rating=\{rating}, comment='\{comment}\{'\''}\{'}'}";
    }

}