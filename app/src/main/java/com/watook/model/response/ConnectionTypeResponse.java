package com.watook.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Avinash.Kumar on 13-Nov-17.
 */

public class ConnectionTypeResponse implements Serializable {


    /**
     * status : success
     * data : [{"userId":"46","firstName":"Avinash","profileImage":"https://graph.facebook.com/1301501726554364/picture?width=9999","location":{"latitude":"18.5916111599999994","longitude":"73.7673852199999942"}},{"userId":"49","firstName":"Suresh","profileImage":"https://graph.facebook.com/2049378208628329/picture?width=9999","location":{"latitude":"16.3935401353151136","longitude":"74.1889519015254848"}}]
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
         * userId : 46
         * firstName : Avinash
         * profileImage : https://graph.facebook.com/1301501726554364/picture?width=9999
         * location : {"latitude":"18.5916111599999994","longitude":"73.7673852199999942"}
         */

        private Long userId;
        private String firstName;
        private String lastName;
        private String profileImage;
        private LocationBean location;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
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

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public static class LocationBean {
            /**
             * latitude : 18.5916111599999994
             * longitude : 73.7673852199999942
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
    }
}
