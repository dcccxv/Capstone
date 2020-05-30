package com.google.sample.cloudvision;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ListviewAdapter extends BaseAdapter {
    ArrayList<ListviewItem> listViewItemList = new ArrayList<>() ;

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView comment = convertView.findViewById(R.id.list_commnet);
        RatingBar ratingBar = convertView.findViewById(R.id.list_rating);

        ListviewItem item = listViewItemList.get(position);

        comment.setText(item.getComment());
        ratingBar.setRating(item.getRating());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public void addItem(String commnet, float rating) {
        ListviewItem item = new ListviewItem();

        item.setComment(commnet);
        item.setRating(rating);

        listViewItemList.add(item);
    }
}
