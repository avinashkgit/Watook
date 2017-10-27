package com.watook.model.response;

/**
 * Created by Avinash.Kumar on 23-Oct-17.
 */

public class ProfileSaveResponse {

    public ProfileSaveResponse() {
    }

    /**
     * status : success
     * reasonCode : User Saved Successfully!
     * data : {"userId":"25"}
     */


    private String status;
    private String reasonCode;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userId : 25
         */

        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
