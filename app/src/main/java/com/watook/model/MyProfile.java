package com.watook.model;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Avinash on 22-04-2017.
 */

public class MyProfile implements Serializable {
    private String userId;
    private String fbId;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String birthday;
    private String profilePicture;
    private String bio;
    private String relationshipStatus;
    private String workEmployer;
    private String workLocation;
    private String workPosition;
    private ArrayList<String> listOfProfilePic;

    public MyProfile() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public ArrayList<String> getListOfProfilePic() {
        return listOfProfilePic;
    }

    public void setListOfProfilePic(ArrayList<String> listOfProfilePic) {
        this.listOfProfilePic = listOfProfilePic;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
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
}
