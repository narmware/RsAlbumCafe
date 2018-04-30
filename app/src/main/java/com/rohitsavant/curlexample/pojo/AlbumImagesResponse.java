package com.rohitsavant.curlexample.pojo;

/**
 * Created by savvy on 4/30/2018.
 */

public class AlbumImagesResponse {
    String response;
    AlbumImages[] data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public AlbumImages[] getData() {
        return data;
    }

    public void setData(AlbumImages[] data) {
        this.data = data;
    }
}
