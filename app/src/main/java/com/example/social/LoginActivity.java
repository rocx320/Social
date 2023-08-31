package com.example.social;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.social.extra.CharacterRemovalUtil;
import com.example.social.extra.LocalAccountManager;
import com.example.social.extra.UserUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginBtn,loginPhoneBtn;
    TextView signupRedirectText;

    FirebaseAuth auth;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersRef = ref.child("users");
    String userId = user != null ? user.getUid() : null;

    private LocalAccountManager localAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        loginPhoneBtn = findViewById(R.id.login_addPhone);
        signupRedirectText = findViewById(R.id.signup_redirect);

        auth = FirebaseAuth.getInstance();

        localAccountManager = new LocalAccountManager(this);


        // When pressed Enter after Password Enter will also act as Login Button
        loginPassword.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                loginBtn.performClick(); // Programmatically trigger login button click
                return true;
            }
            return false;
        });

        //Firebase Authentication Login

//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String txt_email = loginUsername.getText().toString().trim();
//                String txt_pass = loginPassword.getText().toString().trim();
//
//                loginUser(txt_email,txt_pass);
//            }
//        });

        loginPhoneBtn.setOnClickListener(v -> {startActivity(new Intent(this, LoginOtp.class));});

        loginBtn.setOnClickListener(v -> {
            String txt_email = loginUsername.getText().toString().trim();
            String txt_pass = loginPassword.getText().toString().trim();

            if (txt_email.equals("Admin") && txt_pass.equals("123456")) {
                localAccountManager.saveLocalAccount(txt_email, txt_pass);
                Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                launchHomeScreen();
            } else if (!validateUserEmail(txt_email) || !validatePassword(txt_pass)) {

            } else {
//                checkUser();
                loginUser(txt_email, txt_pass);
            }
        });

        signupRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterMain.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

//    private void loginAdmin(String admin, String pass) {
//
//        Toast.makeText(LoginActivity.this, "Admin Login Successful!", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(LoginActivity.this, HomeScreen.class);
//        intent.putExtra("username", admin);
//        startActivity(intent);
//        finish();
//    }


    // Login Using Firebase Authentication
    private void loginUser(String txtEmail, String txtPass) {

        auth.signInWithEmailAndPassword(txtEmail, txtPass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeScreen.class));
                        finish();

                        String emailText = CharacterRemovalUtil.removeCharacters(txtEmail);
                        usersRef.child(emailText).child("active").setValue(true);

                    } else {
                        // Sign in failed
                        if (task.getException() != null &&
                                Objects.requireNonNull(task.getException().getMessage()).contains("no user record")) {
                            // User doesn't exist
                            Toast.makeText(LoginActivity.this, "User does not exist, Register now!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Other sign-in errors
                            Toast.makeText(LoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // To change Email login to Username name login call this function and comment out the login user using authentication

    private void checkUser() {
        String uname = loginUsername.getText().toString().trim();
        String pass = loginPassword.getText().toString().trim();


        if (user != null) {
            String userId = user.getUid();
            UserUtil.setUserId(userId);
        }

        usersRef.orderByChild("username").equalTo(uname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Username exists, proceed to home screen
                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeScreen.class);
                    intent.putExtra("username", uname);
                    startActivity(intent);
                    finish();
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