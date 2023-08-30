package com.example.social.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.social.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class updateProfile extends AppCompatActivity {

    private TextView showName, showUname;
    private ImageView profileImageView;
    private RadioGroup genderRadioGroup;
    private Button editEmail, editDob;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button save;

    // Firebase
    private DatabaseReference userReference;
    private FirebaseUser currentUser;
    private StorageReference storageReference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        showName = findViewById(R.id.update_name);
        showUname = findViewById(R.id.update_username);

        profileImageView = findViewById(R.id.update_profile_pic);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        editEmail = findViewById(R.id.update_email);
        editDob = findViewById(R.id.update_dob);
        save = findViewById(R.id.update_saveBtn);

        userReference = FirebaseDatabase.getInstance().getReference("users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser!=null){ userId = currentUser.getUid(); };

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditEmailDialog();
            }
        });

        editDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectBirthdayDialog();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfilePicture();
                saveUserData();
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(String.class);
                showName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userReference.child(userId).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uname = snapshot.getValue(String.class);
                showUname.setText(uname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showUserData();
    }

    private void uploadProfilePicture() {
        if (selectedImageUri != null && currentUser != null) {
            // Upload profile picture to Firebase Storage

            StorageReference imageRef = storageReference.child(userId);
            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Update profile details in the database
                            String profileImageUrl = uri.toString();
                            // You can also update other profile details here
                            userReference.child(userId).child("profileImageUrl").setValue(profileImageUrl);
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        }
    }


    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri);
        }
    }

    private void showSelectBirthdayDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_select_birthday);
        DatePicker datePicker = dialog.findViewById(R.id.datePicker);
        Button saveButton = dialog.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1; // Month is 0-based
                int year = datePicker.getYear();

                String birthday = String.format("%02d-%02d-%04d", month, day, year);

                // Update the birthday in the database
                Toast.makeText(updateProfile.this, "Birthday Updated!", Toast.LENGTH_SHORT).show();
                userReference.child(userId).child("DOB").setValue(birthday);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showEditEmailDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_edit_email);
        EditText emailEditText = dialog.findViewById(R.id.emailEditText);
        Button saveButton = dialog.findViewById(R.id.saveButton);

        // Pre-fill the email EditText with the current user's email
//        emailEditText.setText(currentUser.getEmail());

        saveButton.setOnClickListener(v -> {

            String newEmail = emailEditText.getText().toString();
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("email", newEmail);

            if (!newEmail.isEmpty()) {
                userReference.child(userId).updateChildren(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Update the email in the database
                            Toast.makeText(updateProfile.this, "Email Updated Successfully!", Toast.LENGTH_SHORT).show();
//                            userReference.child(userId).child("email").setValue(newEmail);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        dialog.show();
    }


    // Method to save user data to Firebase Realtime Database
    private void saveUserData() {

        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            String gender = selectedRadioButton.getText().toString();

            userReference.child(userId).child("gender").setValue(gender)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Data saved successfully
                            Toast.makeText(updateProfile.this, "Profile updated!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                            Toast.makeText(updateProfile.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

    public void showUserData() {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        showUname.setText("@" + username);
    }
}