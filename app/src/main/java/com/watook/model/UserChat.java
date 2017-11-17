package com.watook.model;

import java.io.Serializable;

/**
 * Created by Avinash on 06-11-2017.
 */

public class UserChat implements Serializable {
    private Long userId;
    private String name;
    private Long statusInfo;
    private String profileImage;
    private String fireBaseToken;
    private Integer messageCount;
    private String fbImages;
    private String lastMessage;
    private Long lastModified;
    private Long sentById;
    private boolean hasNewMessage;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(Long statusInfo) {
        this.statusInfo = statusInfo;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFireBaseToken() {
        return fireBaseToken;
    }

    public void setFireBaseToken(String fireBaseToken) {
        this.fireBaseToken = fireBaseToken;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public String getFbImages() {
        return fbImages;
    }

    public void setFbImages(String fbImages) {
        this.fbImages = fbImages;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public Long getSentById() {
        return sentById;
    }

    public void setSentById(Long sentById) {
        this.sentById = sentById;
    }

    public boolean isHasNewMessage() {
        return hasNewMessage;
    }

    public void setHasNewMessage(boolean hasNewMessage) {
        this.hasNewMessage = hasNewMessage;
    }
}
