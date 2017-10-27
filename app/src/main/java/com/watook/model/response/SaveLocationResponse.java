package com.watook.model.response;

import java.io.Serializable;

/**
 * Created by Avinash.Kumar on 24-Oct-17.
 */

public class SaveLocationResponse implements Serializable {

    /**
     * status : success
     * reasonCode : Location Saved Successfully!
     * data : {"locId":"1"}
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
         * locId : 1
         */

        private String locId;

        public String getLocId() {
            return locId;
        }

        public void setLocId(String locId) {
            this.locId = locId;
        }
    }
}
