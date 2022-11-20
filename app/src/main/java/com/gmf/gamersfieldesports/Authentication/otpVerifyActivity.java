package com.gmf.gamersfieldesports.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gmf.gamersfieldesports.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otpVerifyActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String AuthValue;
    private EditText otpTxt;
    private Button OtpVerify;

    private ProgressBar otpProg;
    private TextView otpErrorTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        AuthValue = getIntent().getStringExtra("AuthValue");
        otpTxt = findViewById(R.id.otptxt);
        OtpVerify = findViewById(R.id.otpbtn);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        //progress
        otpProg = findViewById(R.id.otpProgress);
        otpErrorTxt = findViewById(R.id.otpErrorTxt);

        OtpVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OtpVerify.setEnabled(false);
                otpProg.setVisibility(View.VISIBLE);

                String otp = otpTxt.getText().toString();
                if(otp.isEmpty()){
                    otpErrorTxt.setVisibility(View.VISIBLE);
                    otpErrorTxt.setText("please enter your OTP");
                    otpProg.setVisibility(View.INVISIBLE);
                    OtpVerify.setEnabled(true);
                }
                else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(AuthValue,otp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent userintent = new Intent(otpVerifyActivity.this, UserInfoActivity.class);
                    startActivity(userintent);
                    finish();
                }else{
                    otpErrorTxt.setVisibility(View.VISIBLE);
                    otpErrorTxt.setText("OTP veification failed");
                }
                otpProg.setVisibility(View.INVISIBLE);
                OtpVerify.setEnabled(true);

            }
        });

    }
}