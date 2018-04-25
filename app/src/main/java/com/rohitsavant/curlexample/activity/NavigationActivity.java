package com.rohitsavant.curlexample.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.rohitsavant.curlexample.R;
import com.rohitsavant.curlexample.adapter.AlbumListAdapter;
import com.rohitsavant.curlexample.helper.SharedPreferencesHelper;
import com.rohitsavant.curlexample.pojo.AlbumList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.recycler) RecyclerView mRecyclerView;
    @BindView(R.id.btn_nav) ImageButton mBtnNav;
    @BindView(R.id.btn_delete) ImageButton mBtnDelete;
    @BindView(R.id.btn_submit) Button mBtnSubmit;
    @BindView(R.id.edt_alb_id) EditText mEdtAlbumId;

    AlbumListAdapter mAdapter;
    RequestQueue mVolleyRequest;
    ArrayList<AlbumList> mAlbumList=new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;
    String mAlbumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        SharedPreferencesHelper.setDeleteFalg(false,NavigationActivity.this);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                    SharedPreferencesHelper.setDeleteFalg(true,NavigationActivity.this);
                }
                else {
                    SharedPreferencesHelper.setDeleteFalg(false,NavigationActivity.this);
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlbumId=mEdtAlbumId.getText().toString().trim();

                if(mAlbumId.equals(""))
                {
                    mEdtAlbumId.setError("Please enter album id");
                }
                else {
                    Toast.makeText(NavigationActivity.this, "Album added successfully", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    mAlbumList.add(new AlbumList("http://ak5.picdn.net/shutterstock/videos/6261785/thumb/1.jpg","My Wedding"));
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void init() {
        ButterKnife.bind(this);
        mVolleyRequest = Volley.newRequestQueue(NavigationActivity.this);

        //used to hide keyboard bydefault
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setAlbumAdapter(new LinearLayoutManager(NavigationActivity.this));
}

    public void setAlbumAdapter(RecyclerView.LayoutManager mLayoutManager){
        SnapHelper snapHelper = new LinearSnapHelper();

        mAlbumList.clear();
        mAlbumList.add(new AlbumList("https://mccainphotography.files.wordpress.com/2012/11/mccainpictures071.jpg","My Wedding"));
        mAlbumList.add(new AlbumList("http://www.coolhdwallpapers.net/gallery/romantic-park-wedding-hd-wallpaper.jpg","My Birthday"));
        mAlbumList.add(new AlbumList("https://onehdwallpaper.com/wp-content/uploads/2015/07/Punjabi-Wedding-Couple-in-Rain-HD-Picture.jpg","My Wedding"));
        mAlbumList.add(new AlbumList("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_xW6BPnyIKNoOrB15uTjjtE34BOjNJOdIOA7VsYWne37BuEmQCw","My Wedding"));
        mAlbumList.add(new AlbumList("http://ak5.picdn.net/shutterstock/videos/6261785/thumb/1.jpg","My Wedding"));

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @SuppressWarnings("StatementWithEmptyBody")
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
}
