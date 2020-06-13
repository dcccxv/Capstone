package com.google.sample.cloudvision;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.sample.cloudvision.fragments.GoogleMapFragment;
import com.google.sample.cloudvision.fragments.ShopListFragment;

import java.util.ArrayList;


public class GuestActivity extends AppCompatActivity implements View.OnClickListener{
    static public GoogleMapFragment mapFragment;
    static public ShopListFragment listFragment;
    static public ArrayList<ImageView> tabImageViews;
    static public boolean isPrograssing = true;
    static public ProgressBar pgbar;


    public static Handler handler;

    public enum FragmentType {
        MAP_FRAGMENT, LIST_FRAGMENT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        pgbar = findViewById(R.id.pg_bar);
        int permissonCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);//권한요구
        if (permissonCheck == PackageManager.PERMISSION_GRANTED) {
        } else {//권한이 없을때
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        findViewById(R.id.btn_list).setOnClickListener(this);
        findViewById(R.id.btn_map).setOnClickListener(this);
        listFragment = new ShopListFragment();
        mapFragment = new GoogleMapFragment(getApplicationContext());
        tabImageViews = new ArrayList<>();
        tabImageViews.add(findViewById(R.id.img_map));
        tabImageViews.add(findViewById(R.id.img_list));

        handler = new Handler();
        callFragment(FragmentType.MAP_FRAGMENT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onClick(View view) {
        if(!isPrograssing) {
            switch (view.getId()) {
                case R.id.btn_list:
                    callFragment(FragmentType.LIST_FRAGMENT);
                    break;

                case R.id.btn_map:
                    callFragment(FragmentType.MAP_FRAGMENT);
                    break;

            }
        }
    }

    public void callFragment(FragmentType n) {
        switch (n){
            case LIST_FRAGMENT:
                switchFragment(listFragment);
                tabImageViews.get(0).setImageResource(R.drawable.ic_map_non_seleted);
                tabImageViews.get(1).setImageResource(R.drawable.ic_list_selet);
                break;

            case MAP_FRAGMENT:
                switchFragment(mapFragment);
                tabImageViews.get(0).setImageResource(R.drawable.ic_map_selected);
                tabImageViews.get(1).setImageResource(R.drawable.ic_list_non_seleted);
                break;
        }
    }

    public void switchFragment(Fragment fr){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fr);
        fragmentTransaction.commit();
    }



}