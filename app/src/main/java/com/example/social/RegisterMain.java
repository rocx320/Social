package com.example.social;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

    FirebaseDatabase db;
    DatabaseReference ref;

    EditText email;
    EditText password;

    EditText name;

    EditText username;
    Button register;

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


        register.setOnClickListener(v -> {

            String txt_username = username.getText().toString();
            String txt_email = email.getText().toString();
            String txt_pass = password.getText().toString();
            String txt_name = name.getText().toString();


            auth = FirebaseAuth.getInstance();

            User newUser = new User(txt_name, txt_email, txt_username, txt_pass);

            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass)) {
                Toast.makeText(RegisterMain.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
            } else if (txt_pass.length() < 6) {
                Toast.makeText(RegisterMain.this, "Password too short!", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(txt_email, txt_pass);
                String userId = ref.child("users").push().getKey();
                ref.child("users").child(txt_name).setValue(newUser);
            }
        });
    }

    private void registerUser(String txtEmail, String txtPass) {
        auth.createUserWithEmailAndPassword(txtEmail, txtPass).
                addOnCompleteListener(RegisterMain.this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterMain.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterMain.this, LoginActivity.class));
                        finish();
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
        builder.setMessage("Are you sure you want to cancel the registration?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle cancellation, e.g., navigate back to the previous screen or finish the activity
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}