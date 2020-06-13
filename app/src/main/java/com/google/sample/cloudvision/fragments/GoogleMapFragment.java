package com.google.sample.cloudvision.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import com.google.sample.cloudvision.CommentActivity;
import com.google.sample.cloudvision.GpsInfo;
import com.google.sample.cloudvision.GuestActivity;
import com.google.sample.cloudvision.Location;
import com.google.sample.cloudvision.MarkerItem;
import com.google.sample.cloudvision.R;
import com.google.sample.cloudvision.SearchActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener {
    public static GoogleMap mMap = null;
    ArrayList<Location> locations = new ArrayList<>();
    boolean is_location_on = false;
    boolean is_first_move = true;
    GpsInfo jjjj;
    Marker personalMarker;
    HashMap<String, Marker> markers = new HashMap<>();
    View marker_root_view;
    TextView tv_marker;
    Context context;
    EditText et;
    Gson gson;
    MapView mapView;
    View layout;
    boolean isFragmentPause = false;
    boolean isFirstStart = true;

    public GoogleMapFragment(Context c) {
        context = c;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (isFirstStart) {
            layout = inflater.inflate(R.layout.fragment_map, container, false);
            mapView = (MapView) layout.findViewById(R.id.map);
            mapView.getMapAsync(this);
            Log.d("mv", mapView.toString());
        }
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFirstStart) {
            initActionbar();
            jjjj = new GpsInfo(context);
            jjjj.getLocation();
            gson = new Gson();

            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("locations");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    locations.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Location get = postSnapshot.getValue(Location.class);
                        get.setId(postSnapshot.getKey());
                        locations.add(get);
                    }
                    if (mMap != null)
                        refreshMap();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("asdf", "Failed to read value.", error.toException());
                }
            });
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (is_location_on) {
                        jjjj.getLocation();
                        GuestActivity.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!isFragmentPause) {
                                    LatLng temp = new LatLng(jjjj.getLat(), jjjj.getLng());
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
                            }
                        });
                    }
                }
            }, 0, 3000);
            mapView.onCreate(savedInstanceState);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        isFragmentPause = false;
        mapView.onStart();
        Log.d("mv start", mapView.toString());
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        Log.d("mv stop", mapView.toString());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        Log.d("mv resume", mapView.toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentPause = true;
        isFirstStart = false;
        mapView.onPause();
        Log.d("mv pause", mapView.toString());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
        Log.d("mv destroy", mapView.toString());
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.571015, 127.009382)));
        setCustomMarkerView();
        getSampleMarkerItems();
    }


    private void getSampleMarkerItems() {
        ArrayList<MarkerItem> sampleList = new ArrayList();
        for (Location location : locations) {
            sampleList.add(new MarkerItem(location.getLat(), location.getLng(), location.getSeat(), location.getCount(), location.getName(), location.getId()));
        }

        for (MarkerItem markerItem : sampleList) {
            addMarker(markerItem, false).setTag(markerItem.getId());
        }

    }

    private Marker addMarker(MarkerItem markerItem, boolean isSelectedMarker) {
        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        int Seat = markerItem.getSeat();
        String storeName = markerItem.getstoreName();


//        if (isSelectedMarker) {
//            tv_marker.setBackgroundResource(R.drawable.user_green);
//            tv_marker.setTextColor(Color.WHITE);
//        } else {
//            tv_marker.setBackgroundResource(R.drawable.user_green);
//            tv_marker.setTextColor(Color.BLACK);
//        }
        String status = "";
        Log.d("asdf", "% : "+((double) markerItem.getCount() / markerItem.getSeat() * 100));
        if (markerItem.getCount() == 0 || (((double) markerItem.getCount() / markerItem.getSeat() * 100) < 30)) {
            tv_marker.setBackgroundResource(R.drawable.user_green);
            status = "여유";
        } else if (((double) markerItem.getCount() / markerItem.getSeat() * 100) < 70) {
            status = "보통";
            tv_marker.setBackgroundResource(R.drawable.user_yellow);
        } else {
            status = "혼잡";
            tv_marker.setBackgroundResource(R.drawable.user_red);
        }
        tv_marker.setText(storeName + "\n" + status);


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(storeName);
        markerOptions.snippet(Integer.toString(Seat));
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), marker_root_view)));

        return mMap.addMarker(markerOptions);
    }

    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private void setCustomMarkerView() {
        marker_root_view = LayoutInflater.from(context).inflate(R.layout.marker_layout, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.MarkerTextView);
    }

    public void refreshMap() {
//        for (Location location : locations) {
//            long now = System.currentTimeMillis();
//            Date mDate = new Date(now);
//            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss");
//            String getTime = simpleDate.format(mDate);
//            double nowTime = Double.parseDouble(getTime);
//
//            String ID = location.getId();
//
//            if (markers.containsKey(ID)) {//이미 있었던 매장 (객체에 내용만 수정)
//                if ((nowTime - location.getTime()) > 100) {
//                    markers.get(ID).setSnippet("현재촬영중이아님");
//                } else if (location.getCount() == 0) {
//                    markers.get(ID).setSnippet("여유");
//                } else if (((double) location.getCount() / location.getSeat() * 100) < 30) {
//                    markers.get(ID).setSnippet("여유");
//                } else if (((double) location.getCount() / location.getSeat() * 100) < 70) {
//                    markers.get(ID).setSnippet("보통");
//                } else {
//                    markers.get(ID).setSnippet("혼잡");
//                }
//            } else {//새로운 매장 (객체를 추가)
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(new LatLng(location.getLat(), location.getLng()));
//                markerOptions.title(location.getName());
//            /*
//            if (location.count == 0)
//                markerOptions.snippet("0%");
//            else
//                markerOptions.snippet("" + (double) location.count / location.seat * 100 + "%");*/
//                if ((nowTime - location.getTime()) > 100) {
//                    markerOptions.snippet("현재촬영중이아님");
//                } else if (location.getCount() == 0) {
//                    markerOptions.snippet("여유");
//                } else if (((double) location.getCount() / location.getSeat() * 100) < 30) {
//                    markerOptions.snippet("여유");
//                } else if (((double) location.getCount() / location.getSeat() * 100) < 70) {
//                    markerOptions.snippet("보통");
//                } else {
//                    markerOptions.snippet("혼잡");
//                }
//
//
//                if (mMap != null) {
//                    markers.put(location.getId(), mMap.addMarker(markerOptions));
//                    markers.get(location.getId()).setTag(location.getId());
//                }
//            }
//
//        }
        getSampleMarkerItems();

        if (mMap != null && !is_location_on && is_first_move) {
            is_first_move = false;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.571015, 127.009382), 14f));

            GuestActivity.isPrograssing = false;
            GuestActivity.pgbar.setVisibility(View.GONE);
        }
    }

    private void initActionbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(getView().findViewById(R.id.toolbar));
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View mCustomView = LayoutInflater.from(context).inflate(R.layout.actionbar_main, null);
        actionBar.setCustomView(mCustomView);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);

        et = getView().findViewById(R.id.search_bar);

        getView().findViewById(R.id.action_search).setOnClickListener(this);
        getView().findViewById(R.id.action_locate).setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Log.d("asdf", Double.parseDouble(data.getStringExtra("lat")) + " " + Double.parseDouble(data.getStringExtra("lng")));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(data.getStringExtra("lat")), Double.parseDouble(data.getStringExtra("lng"))), 15f));
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_search:
                playButtonClickAnimation(R.id.action_search);
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra("data", gson.toJson(locations));
                intent.putExtra("keyword", et.getText().toString());
                startActivityForResult(intent, 1);
                break;

            case R.id.action_locate:
                playButtonClickAnimation(R.id.action_locate);
                if (is_location_on) {
                    ((ImageView) getView().findViewById(R.id.action_locate)).setImageDrawable(getResources().getDrawable(R.drawable.ic_action_locate_off));
                    if (personalMarker != null) personalMarker.remove();
                } else {
                    ((ImageView) getView().findViewById(R.id.action_locate)).setImageDrawable(getResources().getDrawable(R.drawable.ic_action_locate_on));
                    is_first_move = true;
                }
                is_location_on = !is_location_on;
                break;
        }
        hideKeyboard();
    }

    private void playButtonClickAnimation(int id) {
        getView().findViewById(id).startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("id", marker.getTag() + "");
        startActivity(intent);
        return true;
    }
}
