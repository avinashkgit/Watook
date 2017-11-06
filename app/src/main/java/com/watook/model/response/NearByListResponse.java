package com.watook.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Avinash.Kumar on 06-Nov-17.
 */

public class NearByListResponse implements Serializable {

    /**
     * status : success
     * data : [{"userId":"40","lastName":"Patra","firstName":"Gourab","genderId":"201","statusInfo":"401","profileImage":"https://graph.facebook.com/1592830274093908/picture?type=large","distance":"47.08917","latitude":"18.5914792200000001","longitude":"73.7678354399999989","fireBaseToken":"cmAgXjkP9Ak:APA91bEIWRmWTxQLE16p9kzo8D_n86-2uKGwYYEH5xGKrWOMXqawf2BvzsKRAXIXrk4yrk30SN_JfhLsxLnZbGP2edS7WlXN-N-Q6A1dCk8Z7BUITnx9JhoYg2OQ9AstANpACEZmDZ9l","requestStatus":"0"},{"userId":"32","lastName":"Suryavanshi","firstName":"Amarja","genderId":"202","statusInfo":"401","profileImage":"https://graph.facebook.com/1445816432192826/picture?type=large","distance":"15.806814","latitude":"18.5913931000000012","longitude":"73.7673326999999972","requestStatus":"0"},{"userId":"30","lastName":"Arade","firstName":"Namdev","dob":"1991-05-24 15:58:50.563628","age":"26","genderId":"201","aboutYou":"Hello","workEmployer":"EPI","workLocation":"PUNE","workPosition":"Devloper","statusInfo":"401","fbImages":"https://graph.facebook.com/1554109564657488/picture?type=large,https://graph.facebook.com/1554109564657488/picture?type=large","profileImage":"https://graph.facebook.com/1554109564657488/picture?type=large","distance":"22.98762","latitude":"18.5914190999999995","longitude":"73.7676064999999994","fireBaseToken":"e5lppWjW7qM:APA91bEpdI30wpIFIuEHFfAXElyQ5F9hFxAN3q-4di-TdxYVMf5oDiMqPCWhop1o-vX0iHJ7eJCl80g4d4yDH9SzCGawOtep1sMynVpHQZt1PqEVuYyM_qh-_u4mmArO1wY0rRwzaZH_","requestStatus":"0"},{"userId":"29","lastName":"Lonkar","firstName":"Ravi","genderId":"201","statusInfo":"401","profileImage":"https://graph.facebook.com/1293303400779078/picture?type=large","distance":"953.4538","latitude":"18.6102750999999991","longitude":"73.7607567000000017","fireBaseToken":"fb-Ff93W9Lk:APA91bEc_y5jmoOOwIzW7HYTNBkLsHhP09DTBVcWVKijJ0Eopvny41eBTFj_yJRBnh_X4avlJXxYpL7Ci0ceIe5F0zX3Qi9dG1-dW156bf-7i3xFC4GTP9EI2jLl1DnWCorFKxFmACgk","requestStatus":"0"},{"userId":"41","lastName":"Pardeshi","firstName":"Mahesh","genderId":"201","statusInfo":"401","profileImage":"https://graph.facebook.com/1657741670954898/picture?type=large","distance":"15.806814","latitude":"18.5913931000000012","longitude":"73.7673326999999972","fireBaseToken":"dRfS2pdr1N8:APA91bH1pFyCeP3eKhhWH_8-rGyjNsrkx2GNOpZt0k23KEanqystEMp2SIVCqqQJ6LaGLigD2XZ9gazIAJ1_9dVAkYZ5Td4w_LLc960co7kPPTCoJEhGnzY_QbqffXEmF90DutHieG0V","requestStatus":"0"},{"userId":"43","lastName":"Solge","firstName":"Gaurav","genderId":"201","statusInfo":"401","profileImage":"https://graph.facebook.com/10207462120149538/picture?type=large","distance":"5835.469","latitude":"18.5004214599999983","longitude":"73.8134148299999993","fireBaseToken":"ds9wocu0mWk:APA91bFt1ajjKPi10zQgh4NG6BqXhJB1DT0_Y_qs-0UpgYFlbXAMsWS5llxsgFi9D3jSZsP1X2tl5dk4uhA5ZR5ipgcP6ypMpTHSaXjRchiNMUAP-M3XbQuRT9-p1MnSUr5UVeJwIjY5","requestStatus":"0"}]
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
         * userId : 40
         * lastName : Patra
         * firstName : Gourab
         * genderId : 201
         * statusInfo : 401
         * profileImage : https://graph.facebook.com/1592830274093908/picture?type=large
         * distance : 47.08917
         * latitude : 18.5914792200000001
         * longitude : 73.7678354399999989
         * fireBaseToken : cmAgXjkP9Ak:APA91bEIWRmWTxQLE16p9kzo8D_n86-2uKGwYYEH5xGKrWOMXqawf2BvzsKRAXIXrk4yrk30SN_JfhLsxLnZbGP2edS7WlXN-N-Q6A1dCk8Z7BUITnx9JhoYg2OQ9AstANpACEZmDZ9l
         * requestStatus : 0
         * dob : 1991-05-24 15:58:50.563628
         * age : 26
         * aboutYou : Hello
         * workEmployer : EPI
         * workLocation : PUNE
         * workPosition : Devloper
         * fbImages : https://graph.facebook.com/1554109564657488/picture?type=large,https://graph.facebook.com/1554109564657488/picture?type=large
         */

        private Long userId;
        private String lastName;
        private String firstName;
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
    }
}
