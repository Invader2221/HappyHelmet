package com.example.happy_helmet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.happy_helmet.GetterSetters.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.jar.Attributes;

public class LoginActivity extends AppCompatActivity {

    EditText email, pass;
    private DatabaseReference ref;
    Button btn_Login;
    TextView loginSignUPNow, loginForgetPassword;
    boolean userFound = false;
    public static final String MY_PREFS_NAME = "UserLogin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.login_student_id);
        pass = (EditText) findViewById(R.id.login_password_input);
        loginSignUPNow = (TextView) findViewById(R.id.Login_signUp_text);
        loginForgetPassword = (TextView) findViewById(R.id.Login_forgetPassword_text);
        btn_Login = findViewById(R.id.login_btn);

        ref = FirebaseDatabase.getInstance().getReference();

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot datas : dataSnapshot.getChildren()) {

                            final Users userNew = datas.getValue(Users.class);
                            String emailText = userNew.getEmail();


                            if (emailText.equals(email.getText().toString().trim())) {

                                userFound = true;
                                String password = userNew.getPassword();
                                String isStoreKeeper = userNew.getAdmin();

                                if (password.equals(pass.getText().toString().trim())) {
                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("userPhoneNumber", dataSnapshot.getKey());
                                    editor.putString("loggedUserName", userNew.getName());
                                    editor.putString("loggedUserId", userNew.getStuid());
                                    editor.putString("loggedUserPhone", userNew.getPhone());
                                    editor.putString("loggedUserEmail", userNew.getEmail());

                                    if (isStoreKeeper != null) {
                                        editor.putString("userIsStoreKeeper", "YES");
                                    }
                                    Log.e("TAG", dataSnapshot.getKey());


                                    FirebaseMessaging.getInstance().subscribeToTopic(userNew.getStuid())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    String msg = "Welcome to" + userNew.getEmail();

                                                    Log.e("TAG", msg);
                                                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    finish();
                                    startActivity(intent);
                                    editor.apply();

                                } else {
                                    Toast.makeText(LoginActivity.this, "Password is wrong", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        if (!userFound) {
                            Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_LONG).show();
                        }
                    }



               /* DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                mDatabase.child(email.getText().toString().trim()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            final Users userNew = dataSnapshot.getValue(Users.class);
                            String password = userNew.getPassword();
                            String isStoreKeeper = userNew.getAdmin();

                            if (password.equals(pass.getText().toString().trim())) {
                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("userPhoneNumber", dataSnapshot.getKey());
                                editor.putString("loggedUserName", userNew.getName());
                                editor.putString("loggedUserId", userNew.getStuid());
                                editor.putString("loggedUserPhone", userNew.getPhone());
                                editor.putString("loggedUserEmail", userNew.getEmail());

//                                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//                                String welcomeUser = prefs.getString("loggedUserName","");

                                if (isStoreKeeper != null) {
                                    editor.putString("userIsStoreKeeper", "YES");
                                }
                                Log.e("TAG", dataSnapshot.getKey());

                                FirebaseMessaging.getInstance().subscribeToTopic("qwqwqwqwqwfghgf")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                String msg = "Welcome to" + userNew.getEmail();

                                                Log.e("TAG", msg);
                                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            }
                                        });
//                                FirebaseMessaging.getInstance().subscribeToTopic(dataSnapshot.getKey());

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                finish();
                                startActivity(intent);
                                editor.apply();

                            } else {
                                Toast.makeText(LoginActivity.this, "Password is wrong", Toast.LENGTH_LONG).show();
                            }
                            for (DataSnapshot user : dataSnapshot.getChildren()) {
                                Log.e("zxzc", "" + userNew);

                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_LONG).show();
                        }
                    }*/

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(LoginActivity.this, "No internet found", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        loginForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter you email");

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }


    public void openSignup(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
