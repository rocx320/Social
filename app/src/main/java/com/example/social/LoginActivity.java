package com.example.social;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v -> {
            String txt_email = loginUsername.getText().toString();
            String txt_pass = loginPassword.getText().toString();

            if (txt_email.isEmpty() || txt_pass.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            } else {
                // Perform login logic here
                loginUser(txt_email, txt_pass);
            }

        });

        signupRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterMain.class);
            startActivity(intent);
        });


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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
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
                                Toast.makeText(LoginActivity.this, "Sign-in failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


}