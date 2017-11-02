package com.example.pc.engineerreq;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.engineerreq.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by pc on 2017-07-11.
 */

public class PostAdapter extends ArrayAdapter<Request> {

    public static LinkedHashMap<Request, String> postMap = new LinkedHashMap<>();

    public PostAdapter(@NonNull Context context) {
        super(context, R.layout.list_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater shahmirsInflater = LayoutInflater.from(getContext());
        View customView = shahmirsInflater.inflate(R.layout.list_item, parent, false);

        Request singePostItem = getItem(position);
        TextView tt = (TextView) customView.findViewById(R.id.toptext);
        TextView tt2 = (TextView) customView.findViewById(R.id.toptext2);
        TextView mt = (TextView) customView.findViewById(R.id.middletext);
        TextView mt2 = (TextView) customView.findViewById(R.id.middletext2);

        tt.setText(singePostItem.getEmail());
        tt2.setText(singePostItem.getPhoneNumber());
        mt.setText(singePostItem.getAddress());
        mt2.setText(singePostItem.getProblem());

        if(singePostItem.getStatus().contains("open")){
            customView.setBackgroundColor(Color.argb(100, 255, 0, 0));
        }

        else if(singePostItem.getStatus().contains("in progress")){
            customView.setBackgroundColor(Color.argb(100, 255, 255, 0));
        }

        else{
            customView.setBackgroundColor(Color.argb(100, 0, 255, 0));
        }


        return customView;
    }

    @Override
    public void insert(@Nullable Request object, int index) {
        super.insert(object, index);
    }
}
