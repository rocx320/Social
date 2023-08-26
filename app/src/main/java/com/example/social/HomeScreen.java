package com.example.social;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeScreen extends AppCompatActivity {

    TextView quote;
    TextView greetingTextView;


    FloatingActionButton bored;

    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        quote = findViewById(R.id.quote);
        greetingTextView = findViewById(R.id.greetingTextView);
        bored = findViewById(R.id.bored_click);

        final TextView username = findViewById(R.id.username);


        ref = FirebaseDatabase.getInstance().getReference();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String userId = null;
        if (currentUser != null) {
            userId = currentUser.getUid();
            // Now you have the user's ID (userId)

            ref = ref.child("users").child(userId).child("username");
        } else {
            // User is not authenticated
        }

        if (userId != null) {
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.getValue(String.class);

                        if (username != null) {
                            // Update the TextView with the username
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView usernameTextView = findViewById(R.id.username);
                                    usernameTextView.setText(username);
                                }
                            });
                        }
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                    Log.e("FirebaseError", "Error fetching username: " + databaseError.getMessage());
                }
            });
        }


        // Greetings Text

        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (currentHour >= 0 && currentHour < 12) {
            greeting = "Good Morning!";
        } else if (currentHour >= 12 && currentHour < 18) {
            greeting = "Good Afternoon!";
        } else {
            greeting = "Good Evening!";
        }

        // Set the greeting in the TextView
        greetingTextView.setText(greeting);

        bored.setOnClickListener(v -> showAlertDialog());


        /*
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Profile Activity");

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);
        actionBar.setTitle("Home");

        ProfileFragment fragment = new ProfileFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.navigation_activity, fragment, "");
        fragmentTransaction.commit();

         */
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you bored? Let's do something fun!")
                .setPositiveButton("Yes", (dialog, id) -> Toast.makeText(HomeScreen.this, "Will add Feature in Future Thank You", Toast.LENGTH_SHORT).show());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /*
    private final BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            int itemId = menuItem.getItemId();

            if (itemId == R.id.navigation_profile) {
                actionBar.setTitle("Profile");
                ProfileFragment fragment = new ProfileFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.navigation_profile, fragment, "");
                fragmentTransaction.commit();
                return true;
            } else if (itemId == R.id.navigation_activity) {
                actionBar.setTitle("Home");
                ActivityFragment fragment1 = new ActivityFragment();
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.navigation_activity, fragment1);
                fragmentTransaction1.commit();
                return true;
            } else if (itemId == R.id.navigation_create) {
                actionBar.setTitle("Post");
                NewPostFragment fragment2 = new NewPostFragment();
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.navigation_create, fragment2, "");
                fragmentTransaction2.commit();
                return true;
            } else if (itemId == R.id.navigation_msg) {
                actionBar.setTitle("Chats");
                MsgFragment listFragment = new MsgFragment();
                FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction3.replace(R.id.navigation_msg, listFragment, "");
                fragmentTransaction3.commit();
                return true;
            } else if (itemId == R.id.navigation_notify) {
                actionBar.setTitle("Alerts");
                AlertFragment fragment4 = new AlertFragment();
                FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction4.replace(R.id.navigation_notify, fragment4, "");
                fragmentTransaction4.commit();
                return true;
            }

            return false;

        }
    };
    
     */

        @Override
        protected void onResume() {
            super.onResume();
            quote.setText(Quotes.getRandomQuote());
        }

    }