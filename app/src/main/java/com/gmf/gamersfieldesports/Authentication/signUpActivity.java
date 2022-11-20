package com.gmf.gamersfieldesports.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.gmf.gamersfieldesports.R;

import java.util.concurrent.TimeUnit;

public class signUpActivity extends AppCompatActivity {


    private EditText mUserPhone;
    private TextView msignupError;
    private CountryCodePicker ccp;

    //progressbar
    private ProgressBar signupProg;

    //strings
    private String signNumber;
    private String finalsignNumber;

    //buttons
    private Button msignupBtn;
    private Button msignupLogin;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mUserPhone = findViewById(R.id.signupPhone);
        msignupError = findViewById(R.id.signupError);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        //buttons
        msignupBtn = findViewById(R.id.msignupBtn);
        msignupLogin = findViewById(R.id.signupLoginBtn);

        //progress
        signupProg = findViewById(R.id.signupProg);

        msignupLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActintent = new Intent(signUpActivity.this, LoginActivity.class);
                startActivity(loginActintent);
                finish();
            }
        });

        msignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signNumber = mUserPhone.getText().toString();
                ccp.registerCarrierNumberEditText(mUserPhone);
                finalsignNumber = ccp.getFullNumberWithPlus();

                msignupBtn.setEnabled(false);

                //progressBar
                signupProg.setVisibility(View.VISIBLE);

                if(signNumber.length()!=10){
                    msignupError.setVisibility(View.VISIBLE);
                    msignupError.setText("Please enter a valid number");
                    msignupBtn.setEnabled(true);
                    signupProg.setVisibility(View.INVISIBLE);
                }
                else{
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            finalsignNumber,
                            60,
                            TimeUnit.SECONDS,
                            signUpActivity.this,
                            mCallbacks
                    );
                }

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                msignupError.setVisibility(View.VISIBLE);
                msignupError.setText(e.getMessage());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                final String codeSend = s;
                new android.os.Handler()
                        .postDelayed(new Runnable() {

                                         @Override
                                         public void run() {
                                             Intent otpIntent = new Intent(signUpActivity.this, otpVerifyActivity.class);
                                             otpIntent.putExtra("AuthValue",codeSend);
                                             startActivity(otpIntent);
                                             finish();

                                         }
                                     },
                                3000);
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent userinfointent = new Intent(signUpActivity.this, UserInfoActivity.class);
                    startActivity(userinfointent);
                    finish();
                    signupProg.setVisibility(View.INVISIBLE);
                    msignupBtn.setEnabled(true);
                }else{
                    msignupError.setVisibility(View.VISIBLE);
                    msignupError.setText("Verification Failed,please try Again");
                    Toast.makeText(signUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    msignupBtn.setEnabled(true);
                    signupProg.setVisibility(View.INVISIBLE);
                }
                signupProg.setVisibility(View.INVISIBLE);
                msignupBtn.setEnabled(true);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mCurrentUser!=null){
            Intent MainIntent = new Intent(signUpActivity.this, MainActivity.class);
            startActivity(MainIntent);
            finish();
        }
    }
}