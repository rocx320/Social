package com.example.social.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.social.R;
import com.example.social.extra.CharacterRemovalUtil;
import com.example.social.utils.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MsgFragment#} factory method to
 * create an instance of this fragment.
 */
public class MsgFragment extends Fragment {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String email;

    private TextView user1, user2, user3, user4;
    private ImageView online_user1, online_user2, online_user3, online_user4;
    private ImageView offline_user1, offline_user2, offline_user3, offline_user4;

    private List<String> retrievedEmails = new ArrayList<>();

    private FirebaseHelper firebaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_msg, container, false);

        if (user != null) {
            email = user.getEmail();
            if (email != null)
                ref = FirebaseDatabase.getInstance().getReference().child("users").child(CharacterRemovalUtil.removeCharacters(email));
        }

        user1 = rootView.findViewById(R.id.showUsername_chatMenu1);
        user2 = rootView.findViewById(R.id.showUsername_chatMenu2);
        user3 = rootView.findViewById(R.id.showUsername_chatMenu3);
        user4 = rootView.findViewById(R.id.showUsername_chatMenu4);

        online_user1 = rootView.findViewById(R.id.user1_online);
        online_user2 = rootView.findViewById(R.id.user2_online);
        online_user3 = rootView.findViewById(R.id.user3_online);
        online_user4 = rootView.findViewById(R.id.user4_online);

        offline_user1 = rootView.findViewById(R.id.user1_offline);
        offline_user2 = rootView.findViewById(R.id.user2_offline);
        offline_user3 = rootView.findViewById(R.id.user3_offline);
        offline_user4 = rootView.findViewById(R.id.user4_offline);

        firebaseHelper = new FirebaseHelper();
        getActiveUsers();

        return rootView;
    }

    private void getActiveUsers() {
        firebaseHelper.getAllUserNodesWithActiveStatus(new FirebaseHelper.OnUserNodesRetrievedListener() {
            @Override
            public void onUserNodesRetrieved(List<FirebaseHelper.UserNode> userNodes) {
                // Process the retrieved user nodes
                for (FirebaseHelper.UserNode userNode : userNodes) {
                    String email = userNode.getEmail();
                    boolean isActive = userNode.isActive();
                    // Do something with the email and isActive

                    if(isActive){
                        String username = userNode.getEmail(); // Replace with actual username retrieval logic
                        displayUserDetails(username);
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });
    }

    private void displayUserDetails(String username) {
        user1.setText(email);
        online_user1.setVisibility(View.VISIBLE);
        offline_user1.setVisibility(View.GONE);
    }

}
