package com.rohitsavant.curlexample.pojo;

/**
 * Created by rohitsavant on 12/03/18.
 */

public class AlbumList {

    String url,id,title;

    public AlbumList(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
