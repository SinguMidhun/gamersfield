package com.gmf.gamersfieldesports.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gmf.gamersfieldesports.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText loginPhone;
    private Button mloginBtn;
    private Button msignupBtn;
    private TextView ErrorTxt;

    //numbers
    private String loginNumber;
    private String finalPhoneNumber;
    private CountryCodePicker ccp;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        //country
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        //buttons
        loginPhone = findViewById(R.id.loginPhone);
        mloginBtn = findViewById(R.id.loginBtn);
        msignupBtn = findViewById(R.id.loginSignupBtn);
        ErrorTxt = findViewById(R.id.loginErrorTxt);
        //progress
        mProgress = findViewById(R.id.loginProgress);

        msignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(LoginActivity.this, signUpActivity.class);
                startActivity(signupIntent);
            }
        });
        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mloginBtn.setEnabled(false);

                loginNumber = loginPhone.getText().toString();
                ccp.registerCarrierNumberEditText(loginPhone);
                finalPhoneNumber = ccp.getFullNumberWithPlus();

                mProgress.setVisibility(View.VISIBLE);

                if (loginNumber.length() != 10) {
                    Toast.makeText(LoginActivity.this, "Please enter a valid 10 digit phone number", Toast.LENGTH_SHORT).show();
                    mProgress.setVisibility(View.INVISIBLE);
                    mloginBtn.setEnabled(true);
                } else {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            finalPhoneNumber,
                            60,
                            TimeUnit.SECONDS,
                            LoginActivity.this,
                            mCallbacks
                    );

                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                final String codeSend = s;
                new android.os.Handler().postDelayed(new Runnable() {
                                                         @Override
                                                         public void run() {
                                                             Intent otpIntent = new Intent(LoginActivity.this, otpVerifyActivity.class);
                                                             otpIntent.putExtra("AuthValue",codeSend);
                                                             startActivity(otpIntent);
                                                             mProgress.setVisibility(View.INVISIBLE);
                                                         }
                                                     },
                        3000);
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent mainIntent = new Intent(LoginActivity.this, UserInfoActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }else{
                    ErrorTxt.setVisibility(View.VISIBLE);
                    ErrorTxt.setText("Verification Failed,please try Again");
                    Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    mloginBtn.setEnabled(true);
                }
                mProgress.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mCurrentUser!=null){
            Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(MainIntent);
            finish();
        }
    }

}