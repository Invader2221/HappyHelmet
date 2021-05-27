package com.example.happy_helmet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class ForgetPassword extends AppCompatActivity {

    private EditText editForgetEmail;
    private Button buttonFrogetSubmit;
    private ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().setTitle("Forget Password");

        editForgetEmail = (EditText) findViewById(R.id.forget_email_input);
        buttonFrogetSubmit = (Button) findViewById(R.id.forget_submit);
        progressBar = (ProgressBar) findViewById(R.id.forgetProgressBar);

        auth = FirebaseAuth.getInstance();

        buttonFrogetSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String email = editForgetEmail.getText().toString().trim();

        if (email.isEmpty()) {
            editForgetEmail.setError("Please enter your email");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editForgetEmail.setError("Please enter your email");
            editForgetEmail.requestFocus();
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(ForgetPassword.this, "Reset password link has been sent your email", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(ForgetPassword.this, LoginActivity.class);
                            startActivity(intent);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ForgetPassword.this, "" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}