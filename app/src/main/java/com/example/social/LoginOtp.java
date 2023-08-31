package com.example.social;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.social.utils.AndroidUtils;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOtp extends AppCompatActivity {

    private String phone;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    Long timeoutSeconds = 30L;
    private EditText phoneNumber, otpText;
    private ProgressBar progressBar;
    private TextView resendOtp;
    private Button sendOtp, verifyOtp;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        phoneNumber = findViewById(R.id.phoneNumberEditText);
        otpText = findViewById(R.id.login_otp);
        sendOtp = findViewById(R.id.login_sendOtp);
        verifyOtp = findViewById(R.id.login_otpVerifyBtn);
        resendOtp = findViewById(R.id.login_otpResend);
        progressBar = findViewById(R.id.progressBar);

        phoneNumber.addTextChangedListener(new PhoneNumberTextWatcher());
//        phone = getIntent().getStringExtra("phone");
//
//        if (phone != null) {
//            // Use the received text as needed
//            phoneNumber.setText(phone);
//
//        }

        sendOtp.setOnClickListener(v -> sendotp(phone, false));

        verifyOtp.setOnClickListener(v -> {
            String enteredOtp = otpText.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp);
            signIn(credential);
            setInProgress(true);
        });

        resendOtp.setOnClickListener(v -> sendotp(phone, true));
    }

    private void setInProgress(@NonNull Boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            verifyOtp.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            verifyOtp.setVisibility(View.VISIBLE);
        }

    }

    private void sendotp(String phone, boolean isResend) {
        startResendTimer();
        setInProgress(true);

        PhoneAuthOptions.Builder builder =

                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)
                        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                                setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtils.showToast(getApplicationContext(), "OTP Verification Failed!");
                                setInProgress(false);

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                AndroidUtils.showToast(getApplicationContext(), "OTP sent successfully!");
                                setInProgress(false);


                            }
                        });

        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    private void startResendTimer() {
        resendOtp.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                resendOtp.setText("Resend OTP in "+ timeoutSeconds +" seconds");

                if (timeoutSeconds <= 0) {
                    timeoutSeconds = 30L;
                    timer.cancel();

                    runOnUiThread(() -> resendOtp.setEnabled(true));

                }
            }
        }, 0, 1000);


    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {

        // Login and Go to Next Activity
        setInProgress(true);
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(LoginOtp.this, HomeScreen.class));

            } else {
                AndroidUtils.showToast(getApplicationContext(), "OTP verification failed!");

            }
        });


    }

    private class PhoneNumberTextWatcher implements TextWatcher {
        private boolean isFormatting;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (isFormatting) {
                return;
            }

            isFormatting = true;

            // Remove all non-digit characters
            String phonenumber = editable.toString().replaceAll("\\D", "");

            // Add "+91" if the phone number is not empty and is exactly 10 digits
            if (phonenumber.length() == 10) {
                phonenumber = "+91" + phonenumber;
            }

//            // Update the EditText's text
//            phoneNumber.setText(phonenumber);
//            phoneNumber.setSelection(phonenumber.length()); // Move cursor to the end

            phone = phonenumber;

            isFormatting = false;
        }
    }
}
