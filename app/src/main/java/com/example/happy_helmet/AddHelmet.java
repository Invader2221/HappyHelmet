package com.example.happy_helmet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.example.happy_helmet.LoginActivity.MY_PREFS_NAME;

public class AddHelmet extends AppCompatActivity {

    TextView helmetId;
    EditText helmetName, helmetBrand;
    Button addNowButton;
    FirebaseFirestore db;
    String helmetIdString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_helmet);

        helmetId = (TextView) findViewById(R.id.helmet_Id);
        helmetName = (EditText) findViewById(R.id.helmet_title);
        helmetBrand = (EditText) findViewById(R.id.helmet_type);
        addNowButton = (Button) findViewById(R.id.addNow);

        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();


        Random rand = new Random();
        int n = rand.nextInt(10000);
        helmetIdString = getSaltString() + n;
        helmetId.setText(helmetIdString);

        addNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String helmetNameText = helmetName.getText().toString();
                String helmetBrandText = helmetBrand.getText().toString();

                if (TextUtils.isEmpty(helmetNameText)) {
                    Toast.makeText(AddHelmet.this, "Please enter helmet name.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(helmetBrandText)) {
                    Toast.makeText(AddHelmet.this, "Please enter helmet brand.", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> get_data = new HashMap<>();
                    get_data.put("Hid", helmetIdString);
                    get_data.put("Title", helmetBrandText);
                    get_data.put("Type", helmetNameText);
                    get_data.put("email", prefs.getString("loggedUserEmail", ""));
                    get_data.put("user", prefs.getString("loggedUserId", ""));


                    db.collection("HelmetData").document(helmetIdString).set(get_data)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(AddHelmet.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(AddHelmet.this, "Data successfully written! update", Toast.LENGTH_LONG).show();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddHelmet.this, "Data writing Error update", Toast.LENGTH_LONG).show();

                                }
                            });
                }
            }
        });
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 1) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

    }
}