package com.example.social;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.social.extra.CharacterRemovalUtil;
import com.example.social.extra.CountUser;
import com.example.social.extra.User;
import com.example.social.utils.AndroidUtils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterMain extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;

    DatabaseReference databaseReference;
    DatabaseReference userRef, dataRef;

    EditText email, password, name, username, phone;
    Button register;

    String userId;

    long count;

    private String txt_username, txt_email, txt_pass, txt_name, txt_phone; // User Class Variable References

    private CountUser countUser;

    private static final String PREF_NAME = "CountID";
    private static final String COUNT_KEY = "countKey";

//    String userId = UserUtil.getUserId();

//    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//    SharedPreferences.Editor editor = sharedPreferences.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_main);

        // Retrieve the saved count from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int initialCount = sharedPreferences.getInt(COUNT_KEY, 1);

        // Initialize the CountUser object with the retrieved initial count
        countUser = new CountUser(initialCount);

        email = findViewById(R.id.signup_email);
        name = findViewById(R.id.signup_name);
        username = findViewById(R.id.signup_username);
        password = findViewById(R.id.signup_password);
        phone = findViewById(R.id.signup_phone);
        register = findViewById(R.id.signup_btn);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        register.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                register.performClick(); // Programmatically trigger login button click
                return true;
            }
            return false;
        });

        register.setOnClickListener(v -> {

            txt_username = username.getText().toString();
            txt_email = email.getText().toString();
            txt_pass = password.getText().toString();
            txt_name = name.getText().toString();
            txt_phone = "+91" + phone.getText().toString();


            String email = CharacterRemovalUtil.removeCharacters(txt_email);

//            editor.putString("username",txt_username);
//            editor.putString("name",txt_name);

            User register = new User(txt_email, txt_name, txt_username, txt_pass);

            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass)) {
                Toast.makeText(RegisterMain.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
            } else if (txt_pass.length() < 3) {
                Toast.makeText(RegisterMain.this, "Password too short!", Toast.LENGTH_SHORT).show();
            } else {
                checkDb();
                registerUser(txt_email, txt_pass); // User Registration using Firebase Authentication
                storeToDB(email, txt_email, txt_name, txt_username, txt_phone, txt_pass); // Store User Data to Firebase Database
                getChildCount(email); // Method to get child count

//                String userId = ref.child("users").push().getKey();
//                ref.child("users").child(userId).setValue(newUser);
            }
        });
    }

    private void checkDb() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    count = snapshot.getChildrenCount();
                    count++;
                } else {
                    count = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getChildCount(String email) {

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String idKey = databaseReference.child("users").child(email).child("id").getKey();
                String countCompare = Long.toString(count);

                if (idKey != null && idKey.equals(countCompare)) {
                    databaseReference.child(email).child("id").setValue(countCompare);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("FirebaseError", "Error fetching data: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void storeToDB(String emailPath, String email, String name, String username, String phone, String password) {


        int currentCount = countUser.getCount();

        // Above code is for using shared preferences to save user count data locally


        userRef = databaseReference.child("users").child(emailPath);
        userRef.child("id").setValue(currentCount);
        userRef.child("email").setValue(email);
        userRef.child("name").setValue(name);
        userRef.child("username").setValue(username);
        userRef.child("phone").setValue(phone);
        userRef.child("password").setValue(password);

        dataRef = databaseReference.child("userEmail").child(String.valueOf((currentCount)));
        dataRef.child("id").setValue(currentCount);
        dataRef.child("email").setValue(email);

        countUser.incrementCount();
        saveCountToSharedPreferences(countUser.getCount());


    }

    private void saveCountToSharedPreferences(int count) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(COUNT_KEY, count);
        editor.apply();
    }

    // Registration Using Firebase Authentication
    private void registerUser(String txtEmail, String txtPass) {
        auth.createUserWithEmailAndPassword(txtEmail, txtPass).
                addOnCompleteListener(RegisterMain.this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterMain.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterMain.this, LoginActivity.class);
                        intent.putExtra("phone", txt_phone);
                        startActivity(intent);

                        finish();
                    } else {

                        // Check if the failure reason is due to email already in use
                        if (task.getException() != null && task.getException().getMessage() != null
                                && task.getException().getMessage().contains("email address is already in use")) {
                            // Email already in use, display a toast
//                            Toast.makeText(RegisterMain.this, "User already exists. Try logging in.", Toast.LENGTH_SHORT).show();
                            AndroidUtils.showToast(getApplicationContext(), "User already exists. Try logging in");
                        } else {
                            // Other registration failure, handle accordingly
                            handleRegistrationFailure(task);
                        }
                    }
                });
    }

    private void handleRegistrationFailure(Task<AuthResult> task) {
        Exception exception = task.getException();

        if (exception instanceof FirebaseAuthInvalidUserException) {
            // Invalid email format
            Toast.makeText(RegisterMain.this, "Invalid email format.", Toast.LENGTH_SHORT).show();
        } else if (exception instanceof FirebaseAuthWeakPasswordException) {
            // Weak password
            Toast.makeText(RegisterMain.this, "Weak password. Please choose a stronger password.", Toast.LENGTH_SHORT).show();
        } else {
            // Other registration failure, display a generic error message
            Toast.makeText(RegisterMain.this, "Registration failed. Please try again later.", Toast.LENGTH_SHORT).show();
        }
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