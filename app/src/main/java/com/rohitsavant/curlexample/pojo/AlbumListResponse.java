package com.rohitsavant.curlexample.pojo;

/**
 * Created by savvy on 4/30/2018.
 */

public class AlbumListResponse {
    String response;
    AlbumList[] data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public AlbumList[] getData() {
        return data;
    }

    public void setData(AlbumList[] data) {
        this.data = data;
    }
}
