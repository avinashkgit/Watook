package com.watook.model;

import java.io.Serializable;

/**
 * Created by Avinash on 06-11-2017.
 */

public class UserChat implements Serializable {
    private Long userId;
    private String name;
    private Long genderId;
    private Long statusInfo;
    private String profileImage;
    private Double distance;
    private Double latitude;
    private Double longitude;
    private String fireBaseToken;
    private Long requestStatus;
    private String dob;
    private Integer age;
    private String aboutYou;
    private String workEmployer;
    private String workLocation;
    private String workPosition;
    private String fbImages;
    private String lastMessage;
    private Long lastModified;
    private Long sentById;

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

    public Long getGenderId() {
        return genderId;
    }

    public void setGenderId(Long genderId) {
        this.genderId = genderId;
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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getFireBaseToken() {
        return fireBaseToken;
    }

    public void setFireBaseToken(String fireBaseToken) {
        this.fireBaseToken = fireBaseToken;
    }

    public Long getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Long requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAboutYou() {
        return aboutYou;
    }

    public void setAboutYou(String aboutYou) {
        this.aboutYou = aboutYou;
    }

    public String getWorkEmployer() {
        return workEmployer;
    }

    public void setWorkEmployer(String workEmployer) {
        this.workEmployer = workEmployer;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public String getWorkPosition() {
        return workPosition;
    }

    public void setWorkPosition(String workPosition) {
        this.workPosition = workPosition;
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
}
