package com.watook.model.response;


import java.io.Serializable;
import java.util.List;

/**
 * Created by Avinash.Kumar on 23-Oct-17.
 */

public class CodeValueResponse {
    
    private String status;
    private List<CodeValue> data;

    public CodeValueResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CodeValue> getData() {
        return data;
    }

    public void setData(List<CodeValue> data) {
        this.data = data;
    }


    public static class CodeValue implements Serializable {

        public CodeValue() {
        }

        /**
         * codeType : DocumentTypeID
         * codeValueID : 101
         * codeTypeID : 1
         * codeValue : Picture
         * codeValueDescription : Picture
         * isActive : 1
         */




        private String codeType;
        private long codeValueID;
        private long codeTypeID;
        private String codeValue;
        private String codeValueDescription;
        private String isActive;

        public String getCodeType() {
            return codeType;
        }

        public void setCodeType(String codeType) {
            this.codeType = codeType;
        }

        public long getCodeValueID() {
            return codeValueID;
        }

        public void setCodeValueID(long codeValueID) {
            this.codeValueID = codeValueID;
        }

        public long getCodeTypeID() {
            return codeTypeID;
        }

        public void setCodeTypeID(long codeTypeID) {
            this.codeTypeID = codeTypeID;
        }

        public String getCodeValue() {
            return codeValue;
        }

        public void setCodeValue(String codeValue) {
            this.codeValue = codeValue;
        }

        public String getCodeValueDescription() {
            return codeValueDescription;
        }

        public void setCodeValueDescription(String codeValueDescription) {
            this.codeValueDescription = codeValueDescription;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }
    }
}
