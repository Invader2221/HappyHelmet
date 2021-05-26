package com.example.happy_helmet;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName, InputEmail, InputPhoneNumber, InputPassword;
    private ProgressDialog loadingBar;
    ImageView closeIcon;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputEmail = (EditText) findViewById(R.id.register_user_email);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
        closeIcon = (ImageView) findViewById(R.id.register_close_icon);

        loadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }


    private void CreateAccount() {

        String name = InputName.getText().toString();
        String email = InputEmail.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            InputName.setError("Please enter your name");
//            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(email)) {
            InputEmail.setError("Please enter your email");

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            InputEmail.setError("Please provide a valid email");
            InputEmail.requestFocus();

        } else if (TextUtils.isEmpty(phone)) {
            InputPhoneNumber.setError("Please provide your phone number");

        } else if (TextUtils.isEmpty(password)) {
            InputPassword.setError("Please provide a password");

        } else if (password.length() < 6) {
            InputPassword.setError("Minimum length of password is 6 characters!");
            InputPassword.requestFocus();

        } else {
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Please wait, while you are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

//            saveToRealTimeDB(name, usrEmail, phone, password);


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                final String stuID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                Users user = new Users(name, stuID, phone, email);

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Congratulations, Check your mails to active your account", Toast.LENGTH_LONG).show();
                                            loadingBar.dismiss();

                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            user.sendEmailVerification();

                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);


                                        } else {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Network Error, please try again", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });

                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Email is already registered", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    public void openLogin(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
