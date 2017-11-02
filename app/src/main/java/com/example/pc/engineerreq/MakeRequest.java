package com.example.pc.engineerreq;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MakeRequest extends AppCompatActivity{

    public EditText addressInput;
    public EditText phoneInput;
    public EditText problemInput;

    public static String address = "";
    public static String phone = "";
    public static String problem = "";
    public static String lat = "";
    public static String lng = "";
    public static String temp_key = "";

    public static DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);

        addressInput = (EditText) findViewById(R.id.addressInput);
        phoneInput = (EditText) findViewById(R.id.phoneNumberInput);
        problemInput = (EditText) findViewById(R.id.problemInput);
    }

    public void clickedRequest(View view){
        address = addressInput.getText().toString();
        phone = phoneInput.getText().toString();
        problem = problemInput.getText().toString();

        if(address.matches("") || phone.matches("") || problem.matches("")){
            Toast.makeText(MakeRequest.this, "Empty fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> map = new HashMap<String, Object>();//
        temp_key = root.push().getKey();
        root.updateChildren(map);

        DatabaseReference postRoot = root.child(temp_key);
        LinkedHashMap<String, Object> map2 = new LinkedHashMap<String, Object>();
        map2.put("address", address);
        map2.put("phone", phone);
        map2.put("problem", problem);
        map2.put("email", LoginScreen.e_mail);
        map2.put("status", "open");
        map2.put("temp_key", temp_key);
        map2.put("lat", lat);
        map2.put("lng", lng);
        new GetCoordinates().execute(address.replace(" ","+"));

        postRoot.updateChildren(map2);
    }


    private class GetCoordinates extends AsyncTask<String,Void,String> {
        ProgressDialog dialog = new ProgressDialog(MakeRequest.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try{
                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s",address);
                response = http.getHTTPData(url);
                return response;
            }
            catch (Exception ex)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);

                //Check if results returned something
                String status =jsonObject.getString("status");
                if(!status.matches("OK")){
                    if(dialog.isShowing())
                        dialog.dismiss();
                    return;
                }

                String lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();
                String lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();

                if(dialog.isShowing())
                    dialog.dismiss();

                Double latDouble = Double.parseDouble(lat);
                Double lngDouble = Double.parseDouble(lng);

                //pushing latlng to database
                DatabaseReference postRoot = root.child(temp_key);
                LinkedHashMap<String, Object> map2 = new LinkedHashMap<String, Object>();
                lat = "" + latDouble;
                lng = "" + lngDouble;
                map2.put("lat", lat);
                map2.put("lng", lng);
                postRoot.updateChildren(map2);
                Toast.makeText(MakeRequest.this, "Posted", Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void logOff(View view){
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
