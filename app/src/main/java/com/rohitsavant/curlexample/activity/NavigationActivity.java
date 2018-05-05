package com.rohitsavant.curlexample.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.FadeIn;
import com.google.gson.Gson;
import com.rohitsavant.curlexample.MyApplication;
import com.rohitsavant.curlexample.R;
import com.rohitsavant.curlexample.adapter.AlbumListAdapter;
import com.rohitsavant.curlexample.helper.Constants;
import com.rohitsavant.curlexample.helper.DatabaseAccess;
import com.rohitsavant.curlexample.helper.SharedPreferencesHelper;
import com.rohitsavant.curlexample.helper.SupportFunctions;
import com.rohitsavant.curlexample.pojo.AlbumList;
import com.rohitsavant.curlexample.pojo.AlbumListResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity
         {
    @BindView(R.id.recycler) RecyclerView mRecyclerView;
    @BindView(R.id.btn_nav) ImageButton mBtnNav;
    @BindView(R.id.btn_delete) ImageButton mBtnDelete;
    @BindView(R.id.btn_about) Button mBtnAbout;
    @BindView(R.id.btn_contact) Button mBtnContact;
    @BindView(R.id.btn_home) Button mBtnHome;
    @BindView(R.id.slider) protected SliderLayout mSlider;
    @BindView(R.id.fab_add) protected FloatingActionButton mFabAdd;
    Dialog dialogBuilder;

             DrawerLayout drawer;
    public static LinearLayout mLinearLeft;
    AlbumListAdapter mAdapter;
    RequestQueue mVolleyRequest;
    ArrayList<AlbumList> mAlbumList=new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;
    String mAlbumId;
    DatabaseAccess databaseAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        SharedPreferencesHelper.setDeleteFalg(false,NavigationActivity.this);
        mLinearLeft=findViewById(R.id.linear_left);
        drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        init();

        mBtnNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(NavigationActivity.this, "Delete", Toast.LENGTH_SHORT).show();

                if(SharedPreferencesHelper.getDeleteFlag(NavigationActivity.this)==false)
                {
                    mBtnDelete.setImageResource(R.drawable.ic_home);
                    SharedPreferencesHelper.setDeleteFalg(true,NavigationActivity.this);
                }
                else {
                    mBtnDelete.setImageResource(R.drawable.ic_delete);
                    SharedPreferencesHelper.setDeleteFalg(false,NavigationActivity.this);
                }
                mAdapter.notifyDataSetChanged();
            }
        });


        mBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        mBtnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent=new Intent(NavigationActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });

        mBtnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent=new Intent(NavigationActivity.this,ContactActivity.class);
                startActivity(intent);
            }
        });

        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new Dialog(NavigationActivity.this);
                dialogBuilder.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialogBuilder.setContentView(R.layout.lay_dialog_album);
                dialogBuilder.setCancelable(false);
                dialogBuilder.show();

               /* Button exit = dialogBuilder.findViewById(R.id.dialog_no_connec_exit);
                Button tryAgain = dialogBuilder.findViewById(R.id.dialog_no_connec_try_again);

                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AppCompatActivity act = (AppCompatActivity) mContext;
                        act.finish();
                    }
                });

                tryAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new SendAlbumFrame().execute();
                        mNoConnectionDialog.dismiss();
                    }
                });*/

            }
        });
    }

    private void init() {

        databaseAccess = DatabaseAccess.getInstance(NavigationActivity.this);
        databaseAccess.open();

        ButterKnife.bind(this);
        mVolleyRequest = Volley.newRequestQueue(NavigationActivity.this);

        //used to hide keyboard bydefault
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setSlider();
        setAlbumAdapter(new LinearLayoutManager(NavigationActivity.this));

        ArrayList<AlbumList> albumLists=databaseAccess.getAllAlbums();
        Log.e("Local",albumLists.size()+"");
        for(int i=1;i<albumLists.size();i++)
        {
            AlbumList album=albumLists.get(i);
            Log.e("Local db data",albumLists.get(i).getTitle());
            mAlbumList.add(album);
        }
}
             private void setSlider() {
                 // HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
                 HashMap<String,String> file_maps = new HashMap<String, String>();
                 file_maps.put("Key","https://i.ytimg.com/vi/RUn5NE0RP5Y/maxresdefault.jpg");
                 file_maps.put("key1","https://i.ytimg.com/vi/ipd6ZBnnQOo/maxresdefault.jpg");

                 for(String name : file_maps.keySet()){
                     //textSliderView displays image with text title
                     //TextSliderView textSliderView = new TextSliderView(NavigationActivity.this);

                     //DefaultSliderView displays only image
                     DefaultSliderView textSliderView = new DefaultSliderView(NavigationActivity.this);
                     // initialize a SliderLayout
                     textSliderView
                             .description(name)
                             .image(file_maps.get(name))
                             .setScaleType(BaseSliderView.ScaleType.Fit);

                     //add your extra information
                     textSliderView.bundle(new Bundle());
                     textSliderView.getBundle()
                             .putString("extra",name);

                     mSlider.addSlider(textSliderView);
                 }
                 mSlider.setPresetTransformer(SliderLayout.Transformer.ZoomIn);
                 //mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                 //mSlider.setCustomIndicator(custom_indicator);
                 mSlider.setCustomAnimation(new DescriptionAnimation());
                 mSlider.setFitsSystemWindows(true);
                 mSlider.setDuration(3000);

             }

    public void setAlbumAdapter(RecyclerView.LayoutManager mLayoutManager){
        SnapHelper snapHelper = new LinearSnapHelper();

        mAdapter = new AlbumListAdapter(NavigationActivity.this,mAlbumList);
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusable(false);

        mAdapter.notifyDataSetChanged();

    }
    @Override
    public void onBackPressed() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit app", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }

    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
*/
    private void GetPhotoBook() {


        final ProgressDialog dialog = new ProgressDialog(NavigationActivity.this);
        dialog.setMessage("getting details ...");
        dialog.setCancelable(false);
        dialog.show();

        HashMap<String,String> param = new HashMap();
        param.put(Constants.REQUEST_ALBUM,mAlbumId);

        String url= SupportFunctions.appendParam(MyApplication.URL_GET_ALBUM,param);
        Log.e("Album url",url);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            //getting test master array
                            // testMasterDetails = testMasterArray.toString();

                            Log.e("sharedphoto Json_string",response.toString());
                            Gson gson = new Gson();

                            AlbumListResponse photoResponse= gson.fromJson(response.toString(), AlbumListResponse.class);

                            if(photoResponse.getResponse().equals("100")) {
                                AlbumList[] photo = photoResponse.getData();
                                for (AlbumList item : photo) {

                                    AlbumList album=databaseAccess.getSingleAlbum(item.getServer_id());
                                    if(album!=null)
                                    {
                                        Toast.makeText(NavigationActivity.this, "This album alredy exist", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        mAlbumList.add(item);
                                        Log.e("Featured img title", item.getTitle());
                                        databaseAccess.setAlbum(item.getServer_id(), item.getTitle(), item.getUrl(),item.getAlbum_code());
                                        mLinearLeft.setVisibility(View.VISIBLE);
                                        mAdapter.notifyDataSetChanged();
                                        Toast.makeText(NavigationActivity.this, "Album added successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                            if(photoResponse.getResponse().equals("300"))
                            {
                                //mEdtAlbumId.setError("Invalid album id");
                               // Toast.makeText(NavigationActivity.this, "Invalid album id", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                            //Toast.makeText(NavigationActivity.this, "Invalid album id", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        dialog.dismiss();
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Test Error");
                        // showNoConnectionDialog();
                        dialog.dismiss();

                    }
                }
        );
        mVolleyRequest.add(obreq);
    }

}
