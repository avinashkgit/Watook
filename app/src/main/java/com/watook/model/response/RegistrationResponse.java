package com.watook.model.response;

import java.io.Serializable;

/**
 * Created by Avinash on 22-07-2017.
 */

public class RegistrationResponse implements Serializable {


    public RegistrationResponse() {
    }

    /**
     * status : success
     * reasonCode : Authentication Success!
     * data : XlGAO6uf4cd7MP1qvUsDzs5R8bPTa4XKHl2/LI2o/Zk=
     */


    private String status;
    private String reasonCode;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
