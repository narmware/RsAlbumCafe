package com.rohitsavant.curlexample;

import android.support.multidex.MultiDexApplication;

/**
 * Created by savvy on 4/25/2018.
 */

public class MyApplication extends MultiDexApplication {
    public static final String SERVER_URL="http://onlineshopee.co.in/RS/api/";
    public static final String URL_GET_ALBUM=SERVER_URL+"AlbumValidate.php";
    public static final String URL_GET_PHOTOGRAPHER=SERVER_URL+"getphotographerdetails.php";

}
