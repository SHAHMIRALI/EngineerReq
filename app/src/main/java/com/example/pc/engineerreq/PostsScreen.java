package com.example.pc.engineerreq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;
import java.util.LinkedHashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class PostsScreen extends FragmentActivity {

    public Button postButton;
    public static PostAdapter adaptor;
    public static DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts");//
    private String name, postText, rank, platform, IGN, kdwl, mode, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_screen);

        ListView listView = (ListView) findViewById(R.id.postListView);

        adaptor = new PostAdapter(this);
        listView.setAdapter(adaptor);

        final Intent mapsActivity = new Intent(this, MapsActivity.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mapsActivity.putExtra("lat", adaptor.getItem(position).getLat());
                mapsActivity.putExtra("lng", adaptor.getItem(position).getLng());
                mapsActivity.putExtra("address", adaptor.getItem(position).getAddress());
                startActivity(mapsActivity);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Request request = adaptor.getItem(position);

                DatabaseReference postRoot = root.child(request.getTemp_key());
                LinkedHashMap<String, Object> map2 = new LinkedHashMap<String, Object>();

                if(request.getStatus().contains("open")){
                    request.setStatus("in progress");
                    map2.put("status", "in progress");
                    Toast.makeText(PostsScreen.this, "Status: in progress", Toast.LENGTH_SHORT).show();
                    adaptor.notifyDataSetChanged();
                }
                else if (request.getStatus().contains("in progress")){
                    request.setStatus("closed");
                    map2.put("status", "closed");
                    Toast.makeText(PostsScreen.this, "Status: closed", Toast.LENGTH_SHORT).show();
                    adaptor.notifyDataSetChanged();
                }
                else if (request.getStatus().contains("closed")){
                    request.setStatus("open");
                    map2.put("status", "open");
                    Toast.makeText(PostsScreen.this, "Status: open", Toast.LENGTH_SHORT).show();
                    adaptor.notifyDataSetChanged();
                }

                postRoot.updateChildren(map2);
                return true;
            }
        });


        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updatePostsScreen(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateStatus(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateStatus(DataSnapshot dataSnapshot){
        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()){
            IGN = (String) ((DataSnapshot)i.next()).getValue();//address
            kdwl = (String) ((DataSnapshot)i.next()).getValue();//email
            mode = (String) ((DataSnapshot)i.next()).getValue();//latlng
            lng = (String) ((DataSnapshot)i.next()).getValue();//latlng
            postText = (String) ((DataSnapshot)i.next()).getValue();//phone
            name = (String) ((DataSnapshot)i.next()).getValue();//problem
            rank = (String) ((DataSnapshot)i.next()).getValue();//status
            platform = (String) ((DataSnapshot)i.next()).getValue();//temp_key
        }

        for(Request request: PostAdapter.postMap.keySet()){
            if(request.getTemp_key().matches(platform)){
                request.setLat(mode);
                request.setLng(lng);
                request.setStatus(rank);
                adaptor.notifyDataSetChanged();
                return;
            }
        }


    }


    //CONSTANTLY UPDATING: NEED TO ADD LIST TO KEEP TRACK OF POSTS//
    private void updatePostsScreen(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()){
            IGN = (String) ((DataSnapshot)i.next()).getValue();//address
            kdwl = (String) ((DataSnapshot)i.next()).getValue();//email
            mode = (String) ((DataSnapshot)i.next()).getValue();//latlng
            lng = (String) ((DataSnapshot)i.next()).getValue();//latlng
            postText = (String) ((DataSnapshot)i.next()).getValue();//phone
            name = (String) ((DataSnapshot)i.next()).getValue();//problem
            rank = (String) ((DataSnapshot)i.next()).getValue();//status
            platform = (String) ((DataSnapshot)i.next()).getValue();//temp_key
            Request request = new Request(kdwl,postText, IGN, name, rank, platform, mode, lng);
            PostAdapter.postMap.put(request, "");
        }

//        this.email = email;
//        this.phoneNumber = phoneNumber;
//        this.address = address;
//        this.problem = problem;
//        this.status = status;
//        this.temp_key = temp_key;
//        this.coordinates = coordinates;


        PostsScreen.adaptor.clear();

        //Iterate over PostAdapter.postMap and update adapter
        for(Request key : PostAdapter.postMap.keySet()){
            PostsScreen.adaptor.insert(key, 0);
        }

    }

    public void logOffClicked(View view){
        final Intent loginScreen = new Intent(this, LoginScreen.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Logout");
        builder.setMessage("Confirm logout?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();

                        SharedPreferences sharedPref = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("email", "");
                        editor.putString("password", "");
                        editor.apply();
                        finish();
                        startActivity(loginScreen);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

