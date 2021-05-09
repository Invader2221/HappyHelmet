package com.example.happy_helmet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happy_helmet.GetterSetters.UserHistoryDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserComfirmActivity extends AppCompatActivity {

    TextView confirmId, CheckIn, CheckOutText;
    Button approval;
    FirebaseFirestore db;
    List<UserHistoryDetails> myHelmetHistoryList;
    List<UserHistoryDetails> myHelmetHistoryListForId;
    UserHistoryDetails mHelmetHistoryObj;
    UserHistoryDetails finalHistoryObj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_comfirm);
        getSupportActionBar().setTitle("User Request");

        confirmId = findViewById(R.id.confirm_id);
        CheckIn = findViewById(R.id.confirm_checkIn);
        CheckOutText = findViewById(R.id.confirm_checkOut);
        approval = findViewById(R.id.approvalButon);

        final String helmetId = getIntent().getStringExtra("PUSH_HELMET_ID");
        final String pushTime = getIntent().getStringExtra("PUSH_HELMET_CHECK_IN");


        db = FirebaseFirestore.getInstance();
        myHelmetHistoryListForId = new ArrayList<>();
        myHelmetHistoryList = new ArrayList<>();


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

                                mHelmetHistoryObj = new UserHistoryDetails(HID, HelmetIdSmall, CheckIn, CheckOut);
                                myHelmetHistoryList.add(mHelmetHistoryObj);
                            }
                            for (int i = 0; i < myHelmetHistoryList.size(); i++) {
                                UserHistoryDetails mHistory = myHelmetHistoryList.get(i);
                                if (mHistory.getHelmetIdSmall().equals(helmetId)) {
                                    myHelmetHistoryListForId.add(mHistory);
                                }
                            }

                            if (!myHelmetHistoryListForId.isEmpty()) {
                                finalHistoryObj = myHelmetHistoryListForId.get(myHelmetHistoryListForId.size() - 1);
                            }

                            if (finalHistoryObj == null || !finalHistoryObj.getHelmetCkeckOut().equals("-")) {
                                approval.setText("Check In");
                                confirmId.setText("Helmet Id : " + helmetId);
                                CheckIn.setText("Check in time : " + pushTime);
                                CheckOutText.setText("Check Out time : -");
                            } else {
                                approval.setText("Check Out");
                                confirmId.setText("Helmet Id : " + helmetId);
                                CheckIn.setText("Check in time : " + finalHistoryObj.getHelmetCheckIn());
                                CheckOutText.setText("Check out time : " + pushTime);
                            }

                        } else {
                            //  progressHUD.dismiss();
                            Log.d("Doc", "Error getting documents: ", task.getException());
                            Toast.makeText(UserComfirmActivity.this, "Error getting Data", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        approval.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (finalHistoryObj == null || !finalHistoryObj.getHelmetCkeckOut().equals("-")) {
                    String docPath = "";

                    Map<String, Object> get_data = new HashMap<>();
                    get_data.put("HID", helmetId);
                    get_data.put("CheckIn", pushTime);
                    get_data.put("CheckOut", "-");

                    if (finalHistoryObj == null) {
                        docPath = helmetId + "_1";

                    } else {
                        String asd = finalHistoryObj.getHelmetHistoryId();
                        asd = asd.substring(asd.lastIndexOf("_") + 1);
                        int count = Integer.parseInt(asd);
                        count = count + 1;
                        docPath = helmetId + "_" + count;

                    }
                    Log.e("DocPath >>", docPath);
                    db.collection("HelmetHistory").document(docPath).set(get_data)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //progressDialog.dismiss();
                                    Intent intent = new Intent(UserComfirmActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(UserComfirmActivity.this, "Data successfully written! Set", Toast.LENGTH_LONG).show();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //progressDialog.dismiss();
                                    Toast.makeText(UserComfirmActivity.this, "Data writing Error set", Toast.LENGTH_LONG).show();

                                }
                            });
                } else {
                    String docPath = "";

                    Map<String, Object> get_data = new HashMap<>();
                    get_data.put("HID", helmetId);
                    get_data.put("CheckIn", finalHistoryObj.getHelmetCheckIn());
                    get_data.put("CheckOut", pushTime);


                    String asd = finalHistoryObj.getHelmetHistoryId();
                    asd = asd.substring(asd.lastIndexOf("_") + 1);
                    docPath = helmetId + "_" + asd;

                    Log.e("DocPath >>", docPath);

                    db.collection("HelmetHistory").document(docPath).update(get_data)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //progressDialog.dismiss();
                                    Intent intent = new Intent(UserComfirmActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(UserComfirmActivity.this, "Data successfully written! update", Toast.LENGTH_LONG).show();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //progressDialog.dismiss();
                                    Toast.makeText(UserComfirmActivity.this, "Data writing Error update", Toast.LENGTH_LONG).show();

                                }
                            });
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserComfirmActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
