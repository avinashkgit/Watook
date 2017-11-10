package com.watook.model.response;

import java.io.Serializable;

/**
 * Created by Avinash.Kumar on 10-Nov-17.
 */

public class RequestSaveResponse implements Serializable {


    /**
     * status : success
     * data : 5
     */

    private String status;
    private String data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
