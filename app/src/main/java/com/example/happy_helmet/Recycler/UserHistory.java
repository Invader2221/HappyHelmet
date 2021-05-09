package com.example.happy_helmet.Recycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.happy_helmet.Adapters.UserHistoryAdapter;
import com.example.happy_helmet.MainActivity;
import com.example.happy_helmet.ProfileActivity;
import com.example.happy_helmet.R;
import com.example.happy_helmet.GetterSetters.UserHistoryDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.happy_helmet.LoginActivity.MY_PREFS_NAME;
import static com.example.happy_helmet.MainActivity.myHelmetDetails;

public class UserHistory extends AppCompatActivity {


    RecyclerView mRecycleView;
    List<UserHistoryDetails> userHisroty;
    List<UserHistoryDetails> myUserHisroty;
    UserHistoryDetails uHistoryData;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);
        getSupportActionBar().setTitle("Helmet Storing History");

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }


        mRecycleView = (RecyclerView) findViewById(R.id.recycler_user_history);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(UserHistory.this, 1);
        mRecycleView.setLayoutManager(gridLayoutManager);

        db = FirebaseFirestore.getInstance();
        userHisroty = new ArrayList<>();

        db.collection("HelmetHistory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Doc", document.getId() + " => " + document.getData());

                                final String CheckIn = document.getString("CheckIn");
                                final String HID = document.getId();
                                final String HelmetIdSmall = document.getString("HID");
                                final String CheckOut = document.getString("CheckOut");

                                uHistoryData = new UserHistoryDetails(HID, HelmetIdSmall, CheckIn, CheckOut);
                                userHisroty.add(uHistoryData);
                            }

                            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                            myUserHisroty = new ArrayList<>();
                            if (userHisroty != null) {

                                for (int i = 0; i < userHisroty.size(); i++) {
                                    String helemtId = userHisroty.get(i).getHelmetIdSmall();

                                    if (myHelmetDetails != null) {

                                        for (int s = 0; s < myHelmetDetails.size(); s++) {
                                            String helmetIdTemp = myHelmetDetails.get(s).getHid();
                                            if (helmetIdTemp.equals(helemtId)) {
                                                if (myHelmetDetails.get(s).getUser().equals(prefs.getString("loggedUserId", ""))) {
                                                    myUserHisroty.add(userHisroty.get(i));
                                                }
                                            }
                                        }

                                    }
                                }

                                UserHistoryAdapter myAdapter = new UserHistoryAdapter(UserHistory.this, myUserHisroty);
                                mRecycleView.setAdapter(myAdapter);
                            }

                        } else {
                            //  progressHUD.dismiss();
                            Log.d("Doc", "Error getting documents: ", task.getException());
                            Toast.makeText(UserHistory.this, "Error getting Data", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserHistory.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

//        public boolean onOptionsItemSelected (MenuItem item){
//            switch (item.getItemId()) {
//                case android.R.id.home:
//                    finish();
//                    return true;
//            }
//            return super.onOptionsItemSelected(item);
//        }
//
//        public boolean onCreateOptionsMenu (Menu menu){
//            return true;
//        }
//
}