package com.google.sample.cloudvision.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.sample.cloudvision.GuestActivity;
import com.google.sample.cloudvision.Location;
import com.google.sample.cloudvision.R;
import com.google.sample.cloudvision.listview_resource.ListFragmentAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShopListFragment extends Fragment {
    boolean isFirstStart = true;
    ListView listview;
    ListFragmentAdapter adapter;
    View layout;
    ArrayList<Location> locations;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (isFirstStart) {
            layout = inflater.inflate(R.layout.fragment_list, container, false);
            adapter = new ListFragmentAdapter();

        }
        refresh();
        return layout;
    }
    public void switchFragment(Fragment fr){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fr);
        fragmentTransaction.commit();
    }

    private void refresh() {
        adapter.clear();
        locations = new ArrayList<>();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("locations");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Location get = postSnapshot.getValue(Location.class);
                    get.setId(postSnapshot.getKey());
                    locations.add(get);
                }

                long now = System.currentTimeMillis();
                Date mDate = new Date(now);
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss");
                String getTime = simpleDate.format(mDate);
                double nowTime = Double.parseDouble(getTime);

                for (Location location : locations) {
                    String status;
                    if(nowTime - location.getTime() > 100){
                        status = "종료";
                    }else if (location.getCount() == 0)
                        status = "여유";
                    else if (((double) location.getCount() / location.getSeat() * 100) < 30)
                        status = "여유";
                    else if (((double) location.getCount() / location.getSeat() * 100) < 70)
                        status = "보통";
                    else
                        status = "혼잡";
                    adapter.addItem(location.getName(), location.getShopimgUrl(), status);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        isFirstStart = false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(isFirstStart){
            listview = (ListView) getView().findViewById(R.id.list_fragment);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    GoogleMapFragment.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locations.get(i).getLat(), locations.get(i).getLng()), 15f));
                    switchFragment(GuestActivity.mapFragment);
                    GuestActivity.tabImageViews.get(0).setImageResource(R.drawable.ic_map_selected);
                    GuestActivity.tabImageViews.get(1).setImageResource(R.drawable.ic_list_non_seleted);
                }
            });
        }
    }
}
