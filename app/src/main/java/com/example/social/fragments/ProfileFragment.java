package com.example.social.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.social.R;
import com.example.social.extra.CharacterRemovalUtil;
import com.example.social.extra.DBHelperSqllite;
import com.example.social.profile.updateProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    Button update;

    TextView name, username, email, gender, dob;
    ImageView profilePic;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    String userId;

    private DBHelperSqllite dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        update = rootView.findViewById(R.id.Home_updateProfileBtn);

        name = rootView.findViewById(R.id.Home_nameView);
        username = rootView.findViewById(R.id.Home_usernameView);
        email = rootView.findViewById(R.id.Home_emailView);
        dob = rootView.findViewById(R.id.Home_dobView);
        gender = rootView.findViewById(R.id.Home_genderView);
        profilePic = rootView.findViewById(R.id.Home_profilepicture);

        //Fetch data from database

        dbHelper = new DBHelperSqllite(rootView.getContext(), "profile_pics.db", null, 1);
        // Fetch and display profile picture
        displayProfilePicture();

        if (user != null) {
            userId = user.getEmail();
            userId = CharacterRemovalUtil.removeCharacters(userId);
        }

        displayUserData();
        displayProfilePicture();

        update.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), updateProfile.class)));

        return rootView;
    }

    private void displayUserData() {
        // Fetch Name

        ref.child(userId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data = snapshot.getValue(String.class);
                    name.setText(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Fetch Username

        ref.child(userId).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data = snapshot.getValue(String.class);
                    username.setText("@" + data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Fetch email

        ref.child(userId).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data = snapshot.getValue(String.class);
                    email.setText(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Fetch Date of Birth

        ref.child(userId).child("DOB").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data = snapshot.getValue(String.class);
                    dob.setText(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Fetch gender

        ref.child(userId).child("gender").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data = snapshot.getValue(String.class);
                    gender.setText(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        // Fetch Profile Pic
//        ref.child(userId).child("profileImageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String data = snapshot.getValue(String.class);
//                    Picasso.get().load(data).into(profilePic);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
    }


    private void displayProfilePicture() {

        byte[] imageBytes = dbHelper.getProfilePicture(userId); // Replace with actual user ID

        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            profilePic.setImageBitmap(bitmap);

        }
    }
}