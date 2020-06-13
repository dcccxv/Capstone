package com.google.sample.cloudvision;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        JSONArray data;
        String keyword;
        final ArrayList<HashMap<String, String>> stores = new ArrayList<>();
        try {
            data = new JSONArray(getIntent().getStringExtra("data"));
            keyword = getIntent().getStringExtra("keyword");

            for (int i = 0; i < data.length(); i++) {
                String s = new JSONObject("" + data.get(i)).getString("name");
                if (s.contains(keyword)) {
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("item1", s);
                    item.put("item2", new JSONObject("" + data.get(i)).getString("lat") + " " + new JSONObject("" + data.get(i)).getString("lng"));
                    stores.add(item);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SimpleAdapter adapter = new SimpleAdapter(this, stores, android.R.layout.simple_list_item_2,
                new String[]{"item1", "item2"},
                new int[]{android.R.id.text1, android.R.id.text2});
        ListView listview = findViewById(R.id.search_list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                String[] LatLng = stores.get(position).get("item2").split(" ");
                intent.putExtra("lat", LatLng[0]);
                intent.putExtra("lng", LatLng[1]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
