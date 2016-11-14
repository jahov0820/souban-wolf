package com.souban.wolf.model.wolf;

import java.io.Serializable;

/**
 * Created by Apple on 11/9/16.
 */
public class Game implements Serializable {
    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getGodId() {
        return godId;
    }

    public void setGodId(String godId) {
        this.godId = godId;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    private Integer roomNumber;
    private String godId;
    private Integer memberCount;
    private Integer id;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public Integer getId() {
        return id;
    }
}
