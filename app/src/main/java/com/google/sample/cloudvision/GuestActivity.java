package com.google.sample.cloudvision;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class GuestActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnInfoWindowClickListener {
    GoogleMap mMap = null;
    ArrayList<Location> locations = new ArrayList<>();
    EditText et;
    boolean is_location_on = false;
    boolean is_first_move = true;
    GpsInfo jjjj;
    Marker personalMarker;
    Handler handler;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        int permissonCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);//권한요구
        if(permissonCheck == PackageManager.PERMISSION_GRANTED){
        }else{//권한이 없을때
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        jjjj = new GpsInfo(getApplicationContext());
        jjjj.getLocation();
        gson = new Gson();
        handler = new Handler();
        initActionbar();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("locations");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                locations.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Location get = postSnapshot.getValue(Location.class);
                    get.id = postSnapshot.getKey();
                    locations.add(get);
                }
                refreshMap();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("asdf", "Failed to read value.", error.toException());
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (is_location_on) {
                    jjjj.getLocation();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("asdf", jjjj.lat+ " " +jjjj.lng);
                            LatLng temp = new LatLng(jjjj.lat, jjjj.lng);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(temp);
                            markerOptions.title("내 위치");

                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.my_locate);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                            if (personalMarker != null) personalMarker.remove();
                            personalMarker = mMap.addMarker(markerOptions);
                            if (is_first_move) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(temp, 15f));
                                is_first_move = false;
                            }
                        }
                    });
                }
            }
        }, 0, 3000);
    }

    private void initActionbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_main, null);
        actionBar.setCustomView(mCustomView);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);

        et = findViewById(R.id.search_bar);

        findViewById(R.id.action_search).setOnClickListener(this);
        findViewById(R.id.action_locate).setOnClickListener(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.571015, 127.009382)));
    }

    public void refreshMap() {
        double tempLat = 0;
        double tempLng = 0;
        if (mMap != null)
            mMap.clear();
        for (Location location : locations) {
            MarkerOptions markerOptions = new MarkerOptions();
            tempLat += location.lat;
            tempLng += location.lng;
            Log.d("asdf", location.lat + " " + location.lng);
            markerOptions.position(new LatLng(location.lat, location.lng));
            markerOptions.title(location.name);

            long now = System.currentTimeMillis();
            Date mDate = new Date(now);
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddhhmmss");
            String getTime = simpleDate.format(mDate);
            double nowTime = Double.parseDouble(getTime);
            /*
            if (location.count == 0)
                markerOptions.snippet("0%");
            else
                markerOptions.snippet("" + (double) location.count / location.seat * 100 + "%");*/
            if((nowTime-location.time) > 100){
                markerOptions.snippet("현재촬영중이아님");
            }
            else if (location.count == 0) {
                markerOptions.snippet("여유");
            }
            else if(((double) location.count / location.seat * 100) < 30){
                markerOptions.snippet("여유");
            }
            else if(((double) location.count / location.seat * 100) < 70){
                markerOptions.snippet("보통");
            }
            else {
                markerOptions.snippet("혼잡");
            }


            if (mMap != null)
                mMap.addMarker(markerOptions).setTag(location.getId());
        }
        if (mMap != null && !is_location_on && is_first_move) {
            is_first_move = false;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.571015, 127.009382), 14f));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d("asdf", Double.parseDouble(data.getStringExtra("lat"))+" "+Double.parseDouble(data.getStringExtra("lng")));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(data.getStringExtra("lat")), Double.parseDouble(data.getStringExtra("lng"))), 15f));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_search:
                playButtonClickAnimation(R.id.action_search);

                Intent intent = new Intent(GuestActivity.this, SearchActivity.class);
                intent.putExtra("data", gson.toJson(locations));
                intent.putExtra("keyword", et.getText().toString());
                startActivityForResult(intent, 1);
                break;

            case R.id.action_locate:
                playButtonClickAnimation(R.id.action_locate);
                if (is_location_on) {
                    ((ImageView) findViewById(R.id.action_locate)).setImageDrawable(getResources().getDrawable(R.drawable.ic_action_locate_off));
                    if (personalMarker != null) personalMarker.remove();
                } else {
                    ((ImageView) findViewById(R.id.action_locate)).setImageDrawable(getResources().getDrawable(R.drawable.ic_action_locate_on));
                    is_first_move = true;
                }
                is_location_on = !is_location_on;

                break;
        }
        hideKeyboard();
    }

    private void playButtonClickAnimation(int id) {
        findViewById(id).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(GuestActivity.this, CommentActivity.class);
        intent.putExtra("id", marker.getTag()+"");
        startActivity(intent);
    }
}