package com.example.social.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.social.R;
import com.example.social.profile.updateProfile;


public class ProfileFragment extends Fragment {

    Button update;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        update = rootView.findViewById(R.id.Home_updateProfileBtn);

        update.setOnClickListener(v -> startActivity(new Intent(getActivity(), updateProfile.class)));
        return rootView;
    }
}