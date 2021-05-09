package com.example.happy_helmet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.happy_helmet.GetterSetters.HelmetDetails;
import com.example.happy_helmet.Recycler.MyHelmetActivity;
import com.example.happy_helmet.Recycler.UserHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import static com.example.happy_helmet.LoginActivity.MY_PREFS_NAME;

public class MainActivity extends AppCompatActivity {

    private Button myHelmetButton, ProfileButton, UserHistoryButton,barCodeButton;
    private boolean userIsStoreKeeper = false;

    public static  List<HelmetDetails> myHelmetDetails;
    public static HelmetDetails mHelmetData;
    FirebaseFirestore db;
    KProgressHUD progressHUD;

    boolean doubleBackToExitPressedOnce = false;


    //press back button two time to exit
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);

        myHelmetButton = (Button) findViewById(R.id.button_my_helmet);
        ProfileButton = (Button) findViewById(R.id.button_profile);
        UserHistoryButton = (Button) findViewById(R.id.button_history);
        barCodeButton = (Button) findViewById(R.id.store_scan_barcode);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String isStoreKeeper = prefs.getString("userIsStoreKeeper", "");

        progressHUD = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        if (!isStoreKeeper.equals("")) {
            userIsStoreKeeper = true;
            barCodeButton.setVisibility(View.VISIBLE);
            UserHistoryButton.setVisibility(View.GONE);
            myHelmetButton.setVisibility(View.GONE);
        }

        myHelmetButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, MyHelmetActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        UserHistoryButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, UserHistory.class);
                startActivity(intent);
                finish();
            }
        });

        barCodeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, QRCodeScanner.class);
                startActivity(intent);
                finish();

            }
        });
        setDatalist();

    }



    private void setDatalist() {
        progressHUD.show();
        db = FirebaseFirestore.getInstance();
        myHelmetDetails = new ArrayList<>();

        db.collection("HelmetData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("Doc", document.getId() + " => " + document.getData());

                                final String helmetName = document.getString("Title");
                                final String helmetHid = document.getString("Hid");
                                final String helmetType = document.getString("Type");
                                final String helmetUser = document.getString("user");
                                final String helmetUserEmail = document.getString("email");

                                mHelmetData = new HelmetDetails( helmetHid,helmetName,helmetType,helmetUser,helmetUserEmail);
                                myHelmetDetails.add(mHelmetData);

                                progressHUD.dismiss();

                            }


                        } else {
                            progressHUD.dismiss();
                            Log.d("Doc", "Error getting documents: ", task.getException());
                            Toast.makeText(MainActivity.this, "Error getting Data", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
