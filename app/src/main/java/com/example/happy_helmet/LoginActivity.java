package com.example.happy_helmet;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.happy_helmet.GetterSetters.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passEditText;
    private DatabaseReference ref;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    Button btn_Login;
    TextView loginSignUPNow, loginForgetPassword;
    boolean userFound = false;
    public static final String MY_PREFS_NAME = "UserLogin";
    private ProgressBar progressBar;

    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.login_student_id);
        passEditText = (EditText) findViewById(R.id.login_password_input);
        loginSignUPNow = (TextView) findViewById(R.id.Login_signUp_text);
        loginForgetPassword = (TextView) findViewById(R.id.Login_forgetPassword_text);
        btn_Login = findViewById(R.id.login_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();


        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userLogin();

            }
        });

        loginForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(intent);

            }
        });

    }

    private void userLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Please enter your email");
            emailEditText.requestFocus();

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide a valid email");
            emailEditText.requestFocus();

        } else if (password.isEmpty()) {
            passEditText.setError("Please provide a valid email");
            passEditText.requestFocus();
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    ref = FirebaseDatabase.getInstance().getReference("Users");
                    userId = user.getUid();

                    ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Users userNew = snapshot.getValue(Users.class);
                                String isStoreKeeper = userNew.getAdmin();

                                if (user.isEmailVerified()){

                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("userPhoneNumber", snapshot.getKey());
                                    editor.putString("loggedUserName", userNew.getName());
                                    editor.putString("loggedUserId", userNew.getStuid());
                                    editor.putString("loggedUserPhone", userNew.getPhone());
                                    editor.putString("loggedUserEmail", userNew.getEmail());

                                    if (isStoreKeeper != null) {
                                        editor.putString("userIsStoreKeeper", "YES");
                                    }
                                    Log.e("TAG", snapshot.getKey());

                                    FirebaseMessaging.getInstance().subscribeToTopic(userNew.getStuid())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    String msg = "Welcome " + userNew.getName();

                                                    Log.e("TAG", msg);
                                                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    finish();
                                    startActivity(intent);
                                    editor.apply();




                                } else{
                                    progressBar.setVisibility(View.GONE);
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                                    alertDialogBuilder.setTitle("Account is not activated");
                                    alertDialogBuilder
                                            .setMessage("Press send to receive account confirmation link")
                                            .setCancelable(true)
                                            .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    user.sendEmailVerification();
                                                    Toast.makeText(LoginActivity.this,"Please check your email to verify your account",Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();

                                }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "No internet found", Toast.LENGTH_LONG).show();

                        }
                    });

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_LONG).show();
                }

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
