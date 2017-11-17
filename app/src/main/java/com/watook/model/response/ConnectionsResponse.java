package com.watook.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Avinash.Kumar on 13-Nov-17.
 */

public class ConnectionsResponse implements Serializable{

    /**
     * status : success
     * data : [{"userId":"30","lastName":"Arade","firstName":"Namdev","dob":"1991-05-24 14:11:14.068668","genderId":"201","contactMobile":"1223457698","contactMobile2":"5421346567","emailId":"namdev.arade@gmail.com","aboutYou":"hi","workEmployer":"EPI","workLocation":"EPI","workPosition":"Dev","statusInfo":"401","fbImages":"https://graph.facebook.com/1554109564657488/picture?type=large","profileImage":"https://graph.facebook.com/1554109564657488/picture?type=large","fireBaseToken":"cePSSnMrvPM:APA91bEpvr4Dy7ZwuunxzxVyYoKpNyomrPOtLRZ4uJnx5xmIvN95RBINZSmOvOnO3SNH0B0dID43-ke7lWnBdeZpsLFdPpKEEKQZwnuFUN5y9VtKiRhjaic8tYEdrfIIcNvg5fTQZu-O","location":{"latitude":"18.5914032999999996","longitude":"73.7675847999999945"},"request":{"requestId":4,"requestBy":30,"requestTo":27,"reqstatus":501,"rejectattemtcount":0}},{"userId":"40","lastName":"Patra","firstName":"Gourab","genderId":"201","emailId":"gourabpatra@gmail.com","statusInfo":"401","profileImage":"https://graph.facebook.com/1592830274093908/picture?type=large","fireBaseToken":"cmAgXjkP9Ak:APA91bEIWRmWTxQLE16p9kzo8D_n86-2uKGwYYEH5xGKrWOMXqawf2BvzsKRAXIXrk4yrk30SN_JfhLsxLnZbGP2edS7WlXN-N-Q6A1dCk8Z7BUITnx9JhoYg2OQ9AstANpACEZmDZ9l","location":{"latitude":"18.5914792200000001","longitude":"73.7678354399999989"},"request":{"requestId":6,"requestBy":40,"requestTo":27,"reqstatus":501,"rejectattemtcount":3}}]
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
         * userId : 30
         * lastName : Arade
         * firstName : Namdev
         * dob : 1991-05-24 14:11:14.068668
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
         * request : {"requestId":4,"requestBy":30,"requestTo":27,"reqstatus":501,"rejectattemtcount":0}
         */

        private Long userId;
        private String lastName;
        private String firstName;
        private String dob;
        private Long genderId;
        private String contactMobile;
        private String contactMobile2;
        private String emailId;
        private String aboutYou;
        private String workEmployer;
        private String workLocation;
        private String workPosition;
        private String statusInfo;
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

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
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

        public String getStatusInfo() {
            return statusInfo;
        }

        public void setStatusInfo(String statusInfo) {
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

        public static class LocationBean implements Serializable {
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
             * requestId : 4
             * requestBy : 30
             * requestTo : 27
             * reqstatus : 501
             * rejectattemtcount : 0
             */

            private int requestId;
            private int requestBy;
            private int requestTo;
            private int reqstatus;
            private int rejectattemtcount;

            public int getRequestId() {
                return requestId;
            }

            public void setRequestId(int requestId) {
                this.requestId = requestId;
            }

            public int getRequestBy() {
                return requestBy;
            }

            public void setRequestBy(int requestBy) {
                this.requestBy = requestBy;
            }

            public int getRequestTo() {
                return requestTo;
            }

            public void setRequestTo(int requestTo) {
                this.requestTo = requestTo;
            }

            public int getReqstatus() {
                return reqstatus;
            }

            public void setReqstatus(int reqstatus) {
                this.reqstatus = reqstatus;
            }

            public int getRejectattemtcount() {
                return rejectattemtcount;
            }

            public void setRejectattemtcount(int rejectattemtcount) {
                this.rejectattemtcount = rejectattemtcount;
            }
        }
    }
}
