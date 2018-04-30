package com.rohitsavant.curlexample.pojo;

/**
 * Created by savvy on 4/30/2018.
 */

public class PhotographerDetails {
    String name,address,email,mobile,logo;

    public PhotographerDetails(String name, String address, String email, String mobile, String logo) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.mobile = mobile;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
