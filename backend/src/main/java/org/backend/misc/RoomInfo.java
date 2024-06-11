package org.backend.misc;

import java.io.Serializable;
import java.util.Objects;

public class RoomInfo implements Serializable {
    String roomId;
    String roomName;
    byte[] roomImage;

    public RoomInfo(String roomId, String roomName, byte[] roomImage){
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomImage = roomImage;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public byte[] getRoomImage() {
        return roomImage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RoomInfo roomInfo = (RoomInfo) obj;
        return Objects.equals(roomName, roomInfo.roomName);
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(roomName);
        return hash & Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return STR."RoomInfo{roomId=\{roomId}, roomName='\{roomName}\{'\''}\{'}'}";
    }
}