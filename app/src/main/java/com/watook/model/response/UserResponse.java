package com.watook.model.response;

import java.io.Serializable;

/**
 * Created by Avinash.Kumar on 09-Nov-17.
 */

public class UserResponse implements Serializable {


    /**
     * status : success
     * data : {"userId":"30","lastName":"Arade","firstName":"Namdev","genderId":"201","contactMobile":"1223457698","contactMobile2":"5421346567","emailId":"namdev.arade@gmail.com","aboutYou":"hi","workEmployer":"EPI","workLocation":"EPI","workPosition":"Dev","statusInfo":"401","fbImages":"https://graph.facebook.com/1554109564657488/picture?type=large","profileImage":"https://graph.facebook.com/1554109564657488/picture?type=large","fireBaseToken":"cePSSnMrvPM:APA91bEpvr4Dy7ZwuunxzxVyYoKpNyomrPOtLRZ4uJnx5xmIvN95RBINZSmOvOnO3SNH0B0dID43-ke7lWnBdeZpsLFdPpKEEKQZwnuFUN5y9VtKiRhjaic8tYEdrfIIcNvg5fTQZu-O","location":{"latitude":"18.5914032999999996","longitude":"73.7675847999999945"},"request":{"requestId":0,"requestFrom":27,"requestTo":30,"statusCode":503}}
     */

    private String status;
    private User data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public static class User implements Serializable{
        /**
         * userId : 30
         * lastName : Arade
         * firstName : Namdev
         * genderId : 201
         * contactMobile : 1223457698
         * contactMobile2 : 5421346567
         * emailId : namdev.arade@gmail.com
         * aboutYou : hi
         * workEmployer : EPI
         * workLocation : EPI
         * workPosition : Dev
         * statusInfo : 401
         * fbImages : https://graph.facebook.com/1554109564657488/picture?type=large
         * profileImage : https://graph.facebook.com/1554109564657488/picture?type=large
         * fireBaseToken : cePSSnMrvPM:APA91bEpvr4Dy7ZwuunxzxVyYoKpNyomrPOtLRZ4uJnx5xmIvN95RBINZSmOvOnO3SNH0B0dID43-ke7lWnBdeZpsLFdPpKEEKQZwnuFUN5y9VtKiRhjaic8tYEdrfIIcNvg5fTQZu-O
         * location : {"latitude":"18.5914032999999996","longitude":"73.7675847999999945"}
         * request : {"requestId":0,"requestFrom":27,"requestTo":30,"statusCode":503}
         */

        private Long userId;
        private String lastName;
        private String firstName;
        private Long genderId;
        private String contactMobile;
        private String contactMobile2;
        private String emailId;
        private String aboutYou;
        private String workEmployer;
        private String workLocation;
        private String workPosition;
        private Long statusInfo;
        private String fbImages;
        private String profileImage;
        private String fireBaseToken;
        private LocationBean location;
        private RequestBean request;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public Long getGenderId() {
            return genderId;
        }

        public void setGenderId(Long genderId) {
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

        public Long getStatusInfo() {
            return statusInfo;
        }

        public void setStatusInfo(Long statusInfo) {
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

        public String getFireBaseToken() {
            return fireBaseToken;
        }

        public void setFireBaseToken(String fireBaseToken) {
            this.fireBaseToken = fireBaseToken;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public RequestBean getRequest() {
            return request;
        }

        public void setRequest(RequestBean request) {
            this.request = request;
        }

        public static class LocationBean implements Serializable{
            /**
             * latitude : 18.5914032999999996
             * longitude : 73.7675847999999945
             */

            private Double latitude;
            private Double longitude;

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
        }

        public static class RequestBean implements Serializable{
            /**
             * requestId : 0
             * requestFrom : 27
             * requestTo : 30
             * statusCode : 503
             */

            private Long requestId;
            private Long requestBy;
            private Long requestTo;
            private Long reqstatus;
            private Integer rejectattemtcount;


            public Long getRequestId() {
                return requestId;
            }

            public void setRequestId(Long requestId) {
                this.requestId = requestId;
            }

            public Long getRequestBy() {
                return requestBy;
            }

            public void setRequestBy(Long requestBy) {
                this.requestBy = requestBy;
            }

            public Long getRequestTo() {
                return requestTo;
            }

            public void setRequestTo(Long requestTo) {
                this.requestTo = requestTo;
            }

            public Long getReqstatus() {
                return reqstatus;
            }

            public void setReqstatus(Long reqstatus) {
                this.reqstatus = reqstatus;
            }

            public Integer getRejectattemtcount() {
                return rejectattemtcount;
            }

            public void setRejectattemtcount(Integer rejectattemtcount) {
                this.rejectattemtcount = rejectattemtcount;
            }
        }
    }
}
