package com.watook.model.response;

/**
 * Created by Avinash.Kumar on 27-Oct-17.
 */

public class PreferencesSaveResponse {


    /**
     * status : success
     * reasonCode : Setting Saved Successfully!
     * data : {"settingId":"1"}
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
         * settingId : 1
         */

        private String settingId;

        public String getSettingId() {
            return settingId;
        }

        public void setSettingId(String settingId) {
            this.settingId = settingId;
        }
    }
}
