package com.watook.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.watook.R;
import com.watook.adapter.MainActivityTabAdapter;
import com.watook.manager.DatabaseManager;
import com.watook.model.MyProfile;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    MyProfile myProfile;
    MainActivityTabAdapter mainActivityTabAdapter;
    ViewPager viewPager;

    NavigationView navigationView;
    View header;

    TextView txtName;
    TextView txtEmail;
    ImageView profileBackground;
    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolBar();
        inItView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setProfileData();
    }

    private void inItView() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_bar_main);
        viewPager = (ViewPager) findViewById(R.id.view_pager_main);
        mainActivityTabAdapter = new MainActivityTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainActivityTabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, FriendsListActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                (Toolbar) findViewById(R.id.toolbar),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        profilePic = (ImageView) findViewById(R.id.profile_pic);
        profileBackground = (ImageView) findViewById(R.id.profile_background);

//        menu items
        findViewById(R.id.nav_preferences).setOnClickListener(this);
        findViewById(R.id.nav_profile).setOnClickListener(this);
        findViewById(R.id.nav_settings).setOnClickListener(this);
        findViewById(R.id.nav_blocked).setOnClickListener(this);

    }


    private void setProfileData() {
        myProfile = DatabaseManager.getInstance(MainActivity.this).getMyProfile();


        Glide.with(this).load(myProfile.getProfilePicture()).centerCrop().listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                blurProfilePicBackground();
                return false;
            }
        }).into(profileBackground);


        String name = myProfile.getFirstName() + " " + myProfile.getLastName();
        txtName.setText(name);
        txtEmail.setText(myProfile.getBio());
        Glide.with(this).load(myProfile.getProfilePicture()).asBitmap().centerCrop().into(new BitmapImageViewTarget(profilePic) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                profilePic.setImageDrawable(circularBitmapDrawable);
            }
        });


    }

    private void blurProfilePicBackground() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Blurry.with(MainActivity.this)
                        .radius(25)
                        .sampling(1)
                        .async()
                        .capture(profileBackground)
                        .into(profileBackground);
            }
        }, 300);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.nav_preferences:
                intent = new Intent(MainActivity.this, PreferencesActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_blocked:
                intent = new Intent(MainActivity.this, BlockedActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;

        }
    }
}
