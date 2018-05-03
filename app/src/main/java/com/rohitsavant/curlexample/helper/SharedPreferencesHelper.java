package com.rohitsavant.curlexample.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by comp16 on 12/19/2017.
 */

public class SharedPreferencesHelper {

    private static final String DELETE_FLAG="delete";
    private static final String ALBUM_IMAGE_COUNT="image_count";

    public static void setDeleteFalg(boolean flag, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(DELETE_FLAG,flag);
        edit.commit();
    }

    public static boolean getDeleteFlag(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean flag=pref.getBoolean(DELETE_FLAG,false);
        return flag;
    }


    public static void setAlbumImageCount(int image_count, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putInt(ALBUM_IMAGE_COUNT,image_count);
        edit.commit();
    }

    public static int getAlbumImageCount(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        int image_count=pref.getInt(ALBUM_IMAGE_COUNT,0);
        return image_count;
    }
}
