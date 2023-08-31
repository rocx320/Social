package com.example.social.profile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.social.extra.CharacterRemovalUtil;
import com.example.social.extra.DBHelperSqllite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class updateProfile extends AppCompatActivity {

    private TextView showName, showUname;
    private RadioGroup genderRadioGroup;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    // Firebase
    private DatabaseReference userReference;
    private FirebaseUser currentUser;
    private StorageReference storageReference;

    private DBHelperSqllite dbHelper;
    ImageView profileImageView;
    Button save;

    private boolean imageSelected = false; // Flag to track if an image is selected
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        dbHelper = new DBHelperSqllite(this, "profile_pics.db", null, 1);

        showName = findViewById(R.id.update_name);
        showUname = findViewById(R.id.update_username);

        profileImageView = findViewById(R.id.update_profile_pic);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        Button editEmail = findViewById(R.id.update_email);
        Button editDob = findViewById(R.id.update_dob);
        save = findViewById(R.id.update_saveBtn);

        userReference = FirebaseDatabase.getInstance().getReference("users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            userId = currentUser.getEmail();
            userId = CharacterRemovalUtil.removeCharacters(userId);
        }

        editEmail.setOnClickListener(v -> showEditEmailDialog());
        editDob.setOnClickListener(v -> showSelectBirthdayDialog());

        save.setOnClickListener(v -> saveUserData());

        profileImageView.setOnClickListener(v -> openImagePicker());


        showUserData();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Read the selected image from storage
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Store the image in the SQLite database
                if (bitmap != null) {
                    byte[] imageBytes = getBytesFromBitmap(bitmap);
                    dbHelper.insertProfilePicture(userId, Arrays.toString(imageBytes));
                    profileImageView.setImageBitmap(bitmap);
                    imageSelected = true;
                }else{
                    Toast.makeText(this, "Please Select a profile picture to proceed!", Toast.LENGTH_SHORT).show();
                }
                
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


//    private void uploadProfilePicture() {
//        if (selectedImageUri != null) {
//            // Upload profile picture to Firebase Storage
//
//            StorageReference imageRef = storageReference.child(userId);
//            imageRef.putFile(selectedImageUri)
//                    .addOnSuccessListener(taskSnapshot -> {
//                        // Get the download URL
//                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                            // Update profile details in the database
//                            String profileImageUrl = uri.toString();
//                            // You can also update other profile details here
//                            userReference.child(userId).child("profileImageUrl").setValue(profileImageUrl);
//                        });
//                    })
//                    .addOnFailureListener(e -> {
//                        // Handle failure
//                    });
//        }
//    }
//
//
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
//            selectedImageUri = data.getData();
//            profileImageView.setImageURI(selectedImageUri);
//        }
//    }

    private void showSelectBirthdayDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_select_birthday);
        DatePicker datePicker = dialog.findViewById(R.id.datePicker);
        Button saveButton = dialog.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1; // Month is 0-based
            int year = datePicker.getYear();

            String birthday = String.format("%02d-%02d-%04d", month, day, year);
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("DOB", birthday);

            // Update the birthday in the database
            Toast.makeText(updateProfile.this, "Birthday Updated!", Toast.LENGTH_SHORT).show();
            userReference.child(userId).updateChildren(updateData);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showEditEmailDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_edit_email);
        EditText emailEditText = dialog.findViewById(R.id.emailEditText);
        Button saveButton = dialog.findViewById(R.id.saveButton);

        // Pre-fill the email EditText with the current user's email
        emailEditText.setText(currentUser.getEmail());

        saveButton.setOnClickListener(v -> {

            String newEmail = emailEditText.getText().toString();
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("email", newEmail);

            if (!newEmail.isEmpty()) {
                userReference.child(userId).updateChildren(updateData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update the email in the database
                        Toast.makeText(updateProfile.this, "Email Updated Successfully!", Toast.LENGTH_SHORT).show();
//                            userReference.child(userId).child("email").setValue(newEmail);
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
    }

    // Method to save user data to Firebase Realtime Database
    private void saveUserData() {
        if (imageSelected) {
            try {
                genderSelected();
                openImagePicker();
                showToast();
            } catch (Exception e) {
                Toast.makeText(this, "Profile Update Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showToast() {
        Toast.makeText(this, "Profile Update Success!", Toast.LENGTH_SHORT).show();
    }

    private void genderSelected() {
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            String gender = selectedRadioButton.getText().toString();
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("gender", gender);
            userReference.child(userId).updateChildren(updateData);
        }
    }

    public void showUserData() {
        userReference.child(userId).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(String.class);
                showName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userReference.child(userId).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uname = snapshot.getValue(String.class);
                showUname.setText("@" + uname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
