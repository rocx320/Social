package com.example.social;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupBtn;

    FirebaseDatabase db;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        signupBtn = findViewById(R.id.signup_btn);

        loginRedirectText = findViewById(R.id.login_redirect);

        signupBtn.setOnClickListener(v -> {
            db = FirebaseDatabase.getInstance();
            ref = db.getReference("users");

            String name = signupName.getText().toString();
            String email = signupEmail.getText().toString();
            String username = signupUsername.getText().toString();
            String password = signupPassword.getText().toString();

            HelperClass helperClass = new HelperClass(name,email,username,password);
            ref.child(username).setValue(helperClass);

            Toast.makeText(Signup.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
            Intent redirect = new Intent(Signup.this, LoginActivity.class);
            startActivity(redirect);
        });

        loginRedirectText.setOnClickListener(v -> {
            Intent redirect = new Intent(Signup.this, LoginActivity.class);
            startActivity(redirect);
        });
    }
}