package com.rohitsavant.curlexample.pojo;

/**
 * Created by savvy on 4/30/2018.
 */

public class PhotographerResponse {
    String response;
    PhotographerDetails[] data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public PhotographerDetails[] getData() {
        return data;
    }

    public void setData(PhotographerDetails[] photographerDetails) {
        this.data = photographerDetails;
    }
}
