package com.google.sample.cloudvision.listview_resource;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.sample.cloudvision.R;

import java.util.ArrayList;

public class ListFragmentAdapter extends BaseAdapter {
    ArrayList<ListFragmentItem> listViewItemList = new ArrayList<>();

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_fragment_item, parent, false);
        }

        TextView shopname = convertView.findViewById(R.id.tv_shopname);
        TextView dif = convertView.findViewById(R.id.tv_difficulty);
        ImageView shopimg = convertView.findViewById(R.id.img_shop);
        ImageView difimg = convertView.findViewById(R.id.img_user);

        ListFragmentItem item = listViewItemList.get(position);

        shopname.setText(item.getName());
        dif.setText(item.getStatus());
        int drawable;
        if (item.getStatus().equals("정보없음")) {
            drawable = R.drawable.user;
            dif.setTextColor(Color.parseColor("#808080"));
        }else if (item.getStatus().equals("혼잡")) {
            drawable = R.drawable.user_hard;
            dif.setTextColor(Color.parseColor("#E02C1B"));
        } else if (item.getStatus().equals("보통")) {
            drawable = R.drawable.user_normal;
            dif.setTextColor(Color.parseColor("#FFAB44"));

        } else {
            drawable = R.drawable.user_easy;
            dif.setTextColor(Color.parseColor("#20D95B"));

        }
        Glide
                .with(context)
                .load(drawable)
                .centerCrop()
                .into(difimg);
        if (!item.getImg_shop_url().equals("")) {
            Glide
                    .with(context)
                    .load(item.getImg_shop_url())
                    .centerCrop()
                    .into(shopimg);
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    public void addItem(String name, String shopimg_url, String status) {
        ListFragmentItem item = new ListFragmentItem();
        item.setName(name);
        item.setImg_shop_url(shopimg_url);
        item.setStatus(status);
        listViewItemList.add(item);
    }
    public void clear(){
        listViewItemList.clear();
    }
}
