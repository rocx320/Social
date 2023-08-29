package com.example.social;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.social.extra.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterMain extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference ref;
    EditText email, password, name, username;
    Button register;

    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_main);

        email = findViewById(R.id.signup_email);
        name = findViewById(R.id.signup_name);
        username = findViewById(R.id.signup_username);
        password = findViewById(R.id.signup_password);
        register = findViewById(R.id.signup_btn);

        ref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();


        register.setOnClickListener(v -> {

            String txt_username = username.getText().toString();
            String txt_email = email.getText().toString();
            String txt_pass = password.getText().toString();
            String txt_name = name.getText().toString();

            editor.putString("username",txt_username);
            editor.putString("name",txt_name);

            User newUser = new User(txt_name, txt_email, txt_username, txt_pass);

            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass)) {
                Toast.makeText(RegisterMain.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
            } else if (txt_pass.length() < 3) {
                Toast.makeText(RegisterMain.this, "Password too short!", Toast.LENGTH_SHORT).show();
            } else {
                // User Registration using Firebase Authentication
                registerUser(txt_email, txt_pass);
//                String userId = ref.child("users").push().getKey();
                ref.child("users").child(txt_username).setValue(newUser);
                Toast.makeText(RegisterMain.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterMain.this, LoginActivity.class));
                finish();
            }
        });
    }

    // Registration Using Firebase Authentication
    private void registerUser(String txtEmail, String txtPass) {
        auth.createUserWithEmailAndPassword(txtEmail, txtPass).
                addOnCompleteListener(RegisterMain.this, task -> {
                    if (task.isSuccessful()) {

                    } else {
                        Toast.makeText(RegisterMain.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onBackPressed() {
        showCancelConfirmationDialog();
    }

    private void showCancelConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Registration");
        builder.setMessage("Are you sure you want to cancel the Registration?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Handle cancellation, e.g., navigate back to the previous screen or finish the activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Dismiss the dialog
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}