package com.example.social.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    private DatabaseReference usersReference;

    public FirebaseHelper() {
        // Initialize the Firebase Database reference
        usersReference = FirebaseDatabase.getInstance().getReference().child("users");
    }

    public void getAllUserNodesWithActiveStatus(final OnUserNodesRetrievedListener listener) {
        final List<UserNode> userNodes = new ArrayList<>();

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String email = userSnapshot.child("username").getValue(String.class);
                    Boolean isActive = userSnapshot.child("active").getValue(Boolean.class);
                    if (email != null && isActive != null) {
                        userNodes.add(new UserNode(email, isActive));
                    }
                }

                if (listener != null) {
                    listener.onUserNodesRetrieved(userNodes);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                if (listener != null) {
                    listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public interface OnUserNodesRetrievedListener {
        void onUserNodesRetrieved(List<UserNode> userNodes);
        void onError(String errorMessage);
    }

    public static class UserNode {
        private String email;
        private boolean isActive;

        public UserNode(String email, boolean isActive) {
            this.email = email;
            this.isActive = isActive;
        }

        public String getEmail() {
            return email;
        }

        public boolean isActive() {
            return isActive;
        }
    }

    public void getAllUserChildren(final OnUsersRetrievedListener listener) {
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> userKeys = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    userKeys.add(userSnapshot.getKey());
                }

                if (listener != null) {
                    listener.onUsersRetrieved(userKeys);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                if (listener != null) {
                    listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public interface OnUsersRetrievedListener {
        void onUsersRetrieved(List<String> userKeys);
        void onError(String errorMessage);
    }

    public void getActiveUserDetails(final OnActiveUserDetailsRetrievedListener listener) {
        final List<String> activeUserEmails = new ArrayList<>();
        final List<String> activeUserUsernames = new ArrayList<>();

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Boolean isActive = userSnapshot.child("active").getValue(Boolean.class);
                    if (isActive != null && isActive) {
                        String email = userSnapshot.child("email").getValue(String.class);
                        String username = userSnapshot.child("username").getValue(String.class);
                        if (email != null && username != null) {
                            activeUserEmails.add(email);
                            activeUserUsernames.add(username);
                        }
                    }
                }

                if (listener != null) {
                    listener.onActiveUserDetailsRetrieved(activeUserEmails, activeUserUsernames);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                if (listener != null) {
                    listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public interface OnActiveUserDetailsRetrievedListener {
        void onActiveUserDetailsRetrieved(List<String> activeUserEmails, List<String> activeUserUsernames);
        void onError(String errorMessage);
    }

    public interface OnActiveUserEmailsRetrievedListener {
        void onActiveUserEmailsRetrieved(List<String> activeUserEmails);

        void onError(String errorMessage);
    }

    public void getEmailsFormatted(final OnFormattedEmailsRetrievedListener listener) {
        final List<String> emails = new ArrayList<>();

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String email = userSnapshot.child("email").getValue(String.class);
                    if (email != null) {
                        emails.add(StringFormatter.formatUserString(email));
                    }
                }

                if (listener != null) {
                    listener.onFormattedEmailsRetrieved(emails);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                if (listener != null) {
                    listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public interface OnFormattedEmailsRetrievedListener {
        void onFormattedEmailsRetrieved(List<String> formattedEmails);

        void onError(String errorMessage);
    }
}
