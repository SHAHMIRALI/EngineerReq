package com.example.pc.engineerreq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity {

    public EditText emailInput;
    public EditText passwordInput;
    public static String e_mail = "";
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    public Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        loginButton = (Button) findViewById(R.id.button);
        emailInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        firebaseAuth = FirebaseAuth.getInstance();
        loadData();
    }

    public void signUpClicked(View view){
        String username = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        if(username.matches("") || password.matches("")){
            Toast.makeText(LoginScreen.this, "Empty fields" , Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        //Register user
        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //User is successfully registered
                    progressDialog.dismiss();
                    Toast.makeText(LoginScreen.this, "Registered" , Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginScreen.this, "Registration failed" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void clickedLogin(View view){
        final Intent makeRequest = new Intent(this, MakeRequest.class);
        final Intent mapsActivity = new Intent(this, MapsActivity.class);
        final Intent postsScreen = new Intent(this, PostsScreen.class);

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(email.matches("") || password.matches("")){
            return;
        }

        e_mail = email;

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email.trim(), password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    if(e_mail.matches("saeeda900@yahoo.ca") || e_mail.matches("anjumj2003@yahoo.ca")){
                        startActivity(postsScreen);
                        finish();
                    }
                    else {
                        startActivity(makeRequest);
                        finish();
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginScreen.this, "Login failed" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Saving data
        SharedPreferences sharedPref = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    public void loadData(){
        //Load prefered settings that user set last time
        SharedPreferences sharedPref = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        String password = sharedPref.getString("password", "");
        emailInput.setText(email);
        passwordInput.setText(password);
        loginButton.performClick();
    }
}
