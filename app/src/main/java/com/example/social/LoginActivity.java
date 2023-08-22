package com.example.social;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginBtn;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        signupRedirectText = findViewById(R.id.signup_redirect);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateUsername() | !validatePassword()){

                }else{
                    checkUser();
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Signup.class);
                startActivity(intent);
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
    }

    public Boolean validateUsername(){
        String val = loginUsername.getText().toString();

        if(val.isEmpty()){
            loginUsername.setError("Username cannot be empty!");
            return false;
        }else{
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String pass = loginPassword.getText().toString();

        if(pass.isEmpty()){
            loginPassword.setError("Username cannot be empty!");
            return false;
        }else{
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase =  ref.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    assert passwordFromDB != null;
                    if (passwordFromDB.equals(userPassword)) {

                        loginUsername.setError(null);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        loginPassword.setError("Invalid Credentials!");
                        loginPassword.requestFocus();
                    }
                }else{
                        loginUsername.setError("User does not exists");
                        loginUsername.requestFocus();
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}