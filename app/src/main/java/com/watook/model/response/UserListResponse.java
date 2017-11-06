package com.watook.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Avinash.Kumar on 24-Oct-17.
 */

public class UserListResponse implements Serializable {


    /**
     * status : success
     * data : [{"userId":"15","fbId":"12366","lastName":"Kumar","middleName":"Kumar","firstName":"Avinash","genderId":"201","contactMobile":"8793865035","contactMobile2":"7020603346","emailId":"abc@gmail.com","advertiseId":"abc123","aboutYou":"i asdf","workEmployer":"Extrapreneurs","workPosition":"Java Dev","workLocation":"Pune","statusInfo":"401","fbImages":"C/images","profileImage":"C/images","location":{"latitude":"3.2445659999999998","longitude":"13.584555"}}]
     */

    private String status;
    private List<User> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }

    public static class User implements Serializable{
        /**
         * userId : 15
         * fbId : 12366
         * lastName : Kumar
         * middleName : Kumar
         * firstName : Avinash
         * genderId : 201
         * contactMobile : 8793865035
         * contactMobile2 : 7020603346
         * emailId : abc@gmail.com
         * advertiseId : abc123
         * aboutYou : i asdf
         * workEmployer : Extrapreneurs
         * workPosition : Java Dev
         * workLocation : Pune
         * statusInfo : 401
         * fbImages : C/images
         * profileImage : C/images
         * location : {"latitude":"3.2445659999999998","longitude":"13.584555"}
         */

        private long userId;
        private long fbId;
        private String lastName;
        private String middleName;
        private String firstName;
        private long genderId;
        private String birthday;
        private String contactMobile;
        private String contactMobile2;
        private String emailId;
        private String advertiseId;
        private String aboutYou;
        private String workEmployer;
        private String workPosition;
        private String workLocation;
        private long statusInfo;
        private String fbImages;
        private String profileImage;
        private LocationBean location;
        private String fireBaseToken;
        private long requestStatus;


        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public long getFbId() {
            return fbId;
        }

        public void setFbId(long fbId) {
            this.fbId = fbId;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public long getGenderId() {
            return genderId;
        }

        public void setGenderId(long genderId) {
            this.genderId = genderId;
        }

        public String getContactMobile() {
            return contactMobile;
        }

        public void setContactMobile(String contactMobile) {
            this.contactMobile = contactMobile;
        }

        public String getContactMobile2() {
            return contactMobile2;
        }

        public void setContactMobile2(String contactMobile2) {
            this.contactMobile2 = contactMobile2;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getAdvertiseId() {
            return advertiseId;
        }

        public void setAdvertiseId(String advertiseId) {
            this.advertiseId = advertiseId;
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

        public String getWorkPosition() {
            return workPosition;
        }

        public void setWorkPosition(String workPosition) {
            this.workPosition = workPosition;
        }

        public String getWorkLocation() {
            return workLocation;
        }

        public void setWorkLocation(String workLocation) {
            this.workLocation = workLocation;
        }

        public long getStatusInfo() {
            return statusInfo;
        }

        public void setStatusInfo(long statusInfo) {
            this.statusInfo = statusInfo;
        }

        public String getFbImages() {
            return fbImages;
        }

        public void setFbImages(String fbImages) {
            this.fbImages = fbImages;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }


        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public static class LocationBean implements Serializable{
            /**
             * double : 22.525497
             * latitude : 3.2445659999999998
             * longitude : 13.584555
             */

            private  double distance;
            private double latitude;
            private double longitude;

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }
        }


        public String getFireBaseToken() {
            return fireBaseToken;
        }

        public void setFireBaseToken(String fireBaseToken) {
            this.fireBaseToken = fireBaseToken;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public long getRequestStatus() {
            return requestStatus;
        }

        public void setRequestStatus(long requestStatus) {
            this.requestStatus = requestStatus;
        }
    }


}
