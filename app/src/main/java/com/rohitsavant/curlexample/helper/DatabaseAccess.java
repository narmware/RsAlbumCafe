package com.rohitsavant.curlexample.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rohitsavant.curlexample.pojo.AlbumList;
import com.rohitsavant.curlexample.pojo.PhotographerDetails;

import java.util.ArrayList;


public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    ArrayList<AlbumList> albumLists;
    AlbumList album;
    PhotographerDetails photographerDetails;

    //QuizPojo quesDetails;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public void setAlbum(String server_id,String title,String url) {

        ContentValues values = new ContentValues();
        values.put(Constants.ALBUM_URL, url);
        values.put(Constants.ALBUM_TITLE, title);
        values.put(Constants.ALBUM_SERVER_ID, server_id);

        database.insert("Album", null, values);
        //database.close();
    }

    public ArrayList<AlbumList> getAllAlbums() {
        albumLists = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Album", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
            String url=cursor.getString(1);
            String title=cursor.getString(2);
            String server_id=cursor.getString(3);

            AlbumList album= new AlbumList(url,title,server_id);
            albumLists.add(album);

            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return albumLists;
    }

    public void setPhotographer(String name, String address, String email, String mobile, String logo,String album_server_id) {

        ContentValues values = new ContentValues();
        values.put(Constants.PHOTOGRAPHER_NAME, name);
        values.put(Constants.PHOTOGRAPHER_ADDR, address);
        values.put(Constants.PHOTOGRAPHER_EMAIL, email);
        values.put(Constants.PHOTOGRAPHER_MOBILE, mobile);
        values.put(Constants.PHOTOGRAPHER_LOGO, logo);
        values.put(Constants.ALBUM_SERVER_ID,album_server_id);

        database.insert("Photographer", null, values);
        //database.close();
    }

    public PhotographerDetails getPhotographer(String album_server_id) {
        Cursor cursor = database.rawQuery("SELECT * FROM Photographer where "+Constants.ALBUM_SERVER_ID+"="+album_server_id, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
            String name=cursor.getString(1);
            String addr=cursor.getString(2);
            String email=cursor.getString(3);
            String mobile=cursor.getString(3);
            String logo=cursor.getString(4);

            photographerDetails=new PhotographerDetails(name,addr,email,mobile,logo);

            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return photographerDetails;
    }

 /*

    public List<Image> getAllDetails() {
        quesAnsList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM image", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
             long id= Long.parseLong(cursor.getString(0));
             String name=cursor.getString(1);
             String path=cursor.getString(2);
             boolean isSelected= Boolean.parseBoolean(cursor.getString(3));
            String album=cursor.getString(4);
            String height=cursor.getString(5);
            String width=cursor.getString(6);

            Image quesDetails = new Image(id,name,path,isSelected,album,height,width);
                quesAnsList.add(quesDetails);

            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return quesAnsList;
    }

    public List<Image> getSelectedImages(String albumName) {
        quesAnsList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM image where isSelected = 'true' and album = '"+ albumName+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
            long id= Long.parseLong(cursor.getString(0));
            String name=cursor.getString(1);
            String path=cursor.getString(2);
            boolean isSelected= Boolean.parseBoolean(cursor.getString(3));
            String album=cursor.getString(4);
            String height=cursor.getString(5);
            String width=cursor.getString(6);

            if(cursor.getString(0)!="id") {
                Image quesDetails = new Image(id, name, path, isSelected, album, height, width);
                quesAnsList.add(quesDetails);
            }

            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return quesAnsList;
    }

    public List<Image> getSelectedImagesOfOtherAlbums(String albumName) {
        quesAnsList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM image where isSelected = 'true' and album <> '"+ albumName+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
            long id= Long.parseLong(cursor.getString(0));
            String name=cursor.getString(1);
            String path=cursor.getString(2);
            boolean isSelected= Boolean.parseBoolean(cursor.getString(3));
            String album=cursor.getString(4);
            String height=cursor.getString(5);
            String width=cursor.getString(6);

            Image quesDetails = new Image(id,name,path,isSelected,album,height,width);
            quesAnsList.add(quesDetails);

            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return quesAnsList;
    }

    public void UpdateQty(String qty, int f_id) {
        ContentValues values = new ContentValues();
        values.put(Constants.FRND_FRAME_QTY, qty);

        database.update("friend_profile", values,"f_server_id=" + f_id, null);
    }

    public Friends getSingleFrnd(int f_id) {
        friendsList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM friend_profile where f_server_id ="+f_id, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
            int id = cursor.getInt(0);
            String addr = cursor.getString(1);
            String state = cursor.getString(2);
            String dist = cursor.getString(3);
            String city = cursor.getString(4);
            String pin = cursor.getString(5);
            String mobile = cursor.getString(6);
            String name = cursor.getString(7);
            String email = cursor.getString(8);
            String server_id = cursor.getString(9);

            if (id == -1)
            {

            }
            else {
                friends = new Friends(id,addr, state, city, pin, dist, null, mobile, null,name,email,server_id,null,0);
                friendsList.add(friends);
            }
            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return friends;
    }

    public void deleteSingleFriend(String f_server_id)
    {
        database.execSQL("delete from friend_profile where f_server_id = '"+f_server_id+"'");
    }

    public void deleteAll()
    {
        database.execSQL("delete from image");

    }*/

}