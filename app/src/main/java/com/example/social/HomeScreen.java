package com.example.social;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.social.extra.CharacterRemovalUtil;
import com.example.social.extra.Quotes;
import com.example.social.surprise.AddPlayers;
import com.example.social.utils.AndroidUtils;
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
    TextView nameUser;
    FloatingActionButton bored;
    Button logout;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference userRef ;

    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        logout = findViewById(R.id.logout);
        nameUser = findViewById(R.id.username);
        quote = findViewById(R.id.quote);
        greetingTextView = findViewById(R.id.greetingTextView);
        bored = findViewById(R.id.bored_click);

        bored.setOnClickListener(v -> showAlertDialog());
        greetingText();

        showUserData();

        logout.setOnClickListener(v -> showLogoutConfirmationDialog());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            email = user.getEmail();
            if (email != null)
                userRef = FirebaseDatabase.getInstance().getReference().child("users").child(CharacterRemovalUtil.removeCharacters(email));
            userRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userName = snapshot.getValue(String.class);

                        // Display the name in the TextView
                        nameUser.setText(userName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

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

    @Override
    public void onBackPressed() {
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(CharacterRemovalUtil.removeCharacters(email));
        userRef.child("active").setValue(false);
        super.onBackPressed();
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Perform logout operation
                    // For example, clear user session, navigate to login screen, etc.
                    startActivity(new Intent(HomeScreen.this, LoginActivity.class));
                    userRef = FirebaseDatabase.getInstance().getReference().child("users").child(CharacterRemovalUtil.removeCharacters(email));
                    userRef.child("active").setValue(false);
                    AndroidUtils.showToast(getApplicationContext(),"User Logged Out Successfully!");
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Dismiss the dialog
                    dialog.dismiss();
                })
                .show();
    }

    public void showUserData() {
        Toast.makeText(this, "Making sure it works now!", Toast.LENGTH_SHORT).show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("userEmail");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                String msg = "Current Children in this node : " + Long.toString(count);
                Toast.makeText(HomeScreen.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Greetings Text
    private void greetingText() {
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
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you bored? Let's do something fun!")
                .setPositiveButton("Yes", (dialog, which) -> startActivity(new Intent(HomeScreen.this, AddPlayers.class)))

                .setNegativeButton("No", (dialog, which) -> startActivity(new Intent(HomeScreen.this, LaunchFragment.class)));
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