package com.example.happy_helmet;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.happy_helmet.Recycler.MyHelmetActivity;
import com.example.happy_helmet.Recycler.UserHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import static com.example.happy_helmet.LoginActivity.MY_PREFS_NAME;


public class ProfileActivity extends AppCompatActivity {

    TextView btnLogOut, btnGoBacck, ProfileName, ProfilePhone, ProfileEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ProfileName = findViewById(R.id.prfile_name);
        ProfilePhone = findViewById(R.id.prfile_Phone_Number);
        ProfileEmail = findViewById(R.id.prfile_email);
        btnLogOut = findViewById(R.id.btn_logout);
        btnGoBacck = findViewById(R.id.profile_goback);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btnGoBacck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        ProfileName.setText(String.valueOf( prefs.getString("loggedUserName", "")));
        ProfilePhone.setText(String.valueOf( prefs.getString("loggedUserPhone", "")));
        ProfileEmail.setText(String.valueOf( prefs.getString("loggedUserEmail", "")));

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this);

                alertDialogBuilder.setTitle("Logout");
                alertDialogBuilder
                        .setMessage("Are you sure?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                FirebaseAuth.getInstance().signOut();
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(prefs.getString("loggedUserId", ""));

                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.remove("userPhoneNumber");
                                editor.remove("userIsStoreKeeper");
                                editor.remove("loggedUserName");
                                editor.remove("loggedUserId");
                                editor.remove("loggedUserPhone");
                                editor.remove("loggedUserEmail");
                                editor.apply();

                                Intent intent = new Intent(ProfileActivity.this, StartActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
