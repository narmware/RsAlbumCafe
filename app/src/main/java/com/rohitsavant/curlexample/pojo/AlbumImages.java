package com.rohitsavant.curlexample.pojo;

/**
 * Created by savvy on 4/28/2018.
 */

public class AlbumImages {
String photo_path,type;

    public AlbumImages(String photo_path, String type) {
        this.photo_path = photo_path;
        this.type = type;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
