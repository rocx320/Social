package com.example.social;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginBtn;
    TextView signupRedirectText;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        signupRedirectText = findViewById(R.id.signup_redirect);

//        auth = FirebaseAuth.getInstance();


        loginBtn.setOnClickListener(v -> {
            String txt_email = loginUsername.getText().toString();
            String txt_pass = loginPassword.getText().toString();

            if (!validateUserEmail(txt_email) || !validatePassword(txt_pass)) {

            } else {
                checkUser();
            }
        });

        signupRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterMain.class);
            startActivity(intent);
        });

//        loginBtn.setOnClickListener(v -> {
//
//            String txt_email = loginUsername.getText().toString();
//            String txt_pass = loginPassword.getText().toString();
//
//            if (txt_email.isEmpty() || txt_pass.isEmpty()) {
//                Toast.makeText(LoginActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
//            } else {
//                // Perform login logic here
//                loginUser(txt_email, txt_pass);
//            }
//        });
    }

    private void loginUser(String txtEmail, String txtPass) {
/*
        auth.signInWithEmailAndPassword(txtEmail, txtPass).addOnSuccessListener(ignored -> {
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeScreen.class));
            finish();
        }).addOnFailureListener(fail -> Toast.makeText(this, fail.getMessage(), Toast.LENGTH_SHORT).show());
*/
        auth.signInWithEmailAndPassword(txtEmail, txtPass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeScreen.class));
                        finish();
                    } else {
                        // Sign in failed
                        if (task.getException() != null &&
                                task.getException().getMessage().contains("no user record")) {
                            // User doesn't exist
                            Toast.makeText(LoginActivity.this, "User does not exist, Register now!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Other sign-in errors
                            Toast.makeText(LoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkUser() {
        String uname = loginUsername.getText().toString().trim();
        String pass = loginPassword.getText().toString().trim();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersRef = ref.child("users");
        usersRef.orderByChild("username").equalTo(uname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Username exists, proceed to home screen
                    Intent intent = new Intent(LoginActivity.this, HomeScreen.class);
                    intent.putExtra("username", uname);
                    startActivity(intent);
                } else {
                    // Username does not exist, show error message or handle accordingly
                    Toast.makeText(LoginActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


//        Query checkUserDatabase = reference.orderByChild("username").equalTo(uname);


//        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    loginUsername.setError(null);
//                    String passwordFromDB = snapshot.child(uname).child("password").getValue(String.class);
//
//                    if(passwordFromDB.equals(pass)){
//                        loginUsername.setError(null);
//
//                        //Pass Data using intents
//                        String nameFromDB = snapshot.child(uname).child("username").getValue(String.class);
//
//                        Intent intent = new Intent(LoginActivity.this,HomeScreen.class);
//                        intent.putExtra("name",nameFromDB);
//
//                        startActivity(intent);
//
//                    }else{
//                        loginPassword.setError("Invalid Credentials");
//                        loginPassword.requestFocus();
//                    }
//                }else{
//                    Toast.makeText(LoginActivity.this, "User doesn't exist! Please Register.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    private boolean validateUserEmail(String email) {

        if (email.isEmpty()) {
            loginUsername.setError("Please Enter Username!");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String pass) {
        if (pass.isEmpty()) {
            loginPassword.setError("Password Cannot be empty!");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

}