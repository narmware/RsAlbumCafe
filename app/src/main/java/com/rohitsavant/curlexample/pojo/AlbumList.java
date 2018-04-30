package com.rohitsavant.curlexample.pojo;

/**
 * Created by rohitsavant on 12/03/18.
 */

public class AlbumList {

    String url,id,title,server_id;

    public AlbumList(String url, String title,String server_id) {
        this.url = url;
        this.title = title;
        this.server_id=server_id;
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

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }
}
