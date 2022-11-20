package com.gmf.gamersfieldesports.Authentication;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity{

    //firebase
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    //fields
    private String userid = mCurrentUser.getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;
    private DocumentReference ginfoRef;
    private EditText username;
    private EditText usermail;
    private EditText userphone;
    private String Country;
    private String name;
    private String mail;
    private String phone;
    private Button proceed;
    private TextView userinfoerror;
    private ProgressBar userprog;
    private CountryCodePicker ccp;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        //reference
        username = findViewById(R.id.userInfoName);
        usermail = findViewById(R.id.userInfomail);
        userphone = findViewById(R.id.userInfonumber);
        proceed = findViewById(R.id.userInfoproceedbtn);
        userinfoerror = findViewById(R.id.userInfoError);
        userprog = findViewById(R.id.userinfoprog);
        ccp = (CountryCodePicker) findViewById(R.id.userInfoCountry);

        //Firebase
        docRef =  db.collection("users").document(userid);
        ginfoRef = db.collection("users").document(userid).collection("Gameinfo").document("usergameinfo");

        //country
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Toast.makeText(UserInfoActivity.this, "Updated " + ccp.getSelectedCountryName(), Toast.LENGTH_SHORT).show();
                Country = ccp.getSelectedCountryName();
            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceed.setEnabled(false);
                userprog.setVisibility(View.VISIBLE);

                //firebase Info Uploding
                name = username.getText().toString();
                mail = usermail.getText().toString();
                phone = userphone.getText().toString();
                Country = ccp.getSelectedCountryName();

                if (name.isEmpty() || mail.isEmpty() || phone.isEmpty()){
                    Toast.makeText(UserInfoActivity.this,"Please enter all the fields completely",Toast.LENGTH_SHORT).show();
                }else{

                    Map<String,Object> joinmap = new HashMap<>();
                    joinmap.put("name",name);
                    joinmap.put("Mail",mail);
                    joinmap.put("Phone",phone);
                    joinmap.put("Country",Country);
                    joinmap.put("Balance",0+"");
                    joinmap.put("Dbalance",0+"");
                    joinmap.put("MatchesPlayed",0+"");
                    joinmap.put("uid",userid);

                    Map<String,Object> ginfo = new HashMap<>();
                    ginfo.put("p_name","");
                    ginfo.put("p_id","");
                    ginfo.put("pl_name","");
                    ginfo.put("pl_id","");
                    ginfo.put("ff_name","");
                    ginfo.put("ff_id","");
                    ginfo.put("c_name","");
                    ginfo.put("c_id","");
                    ginfo.put("mm_name","");
                    ginfo.put("mm_id","");
                    ginfo.put("l_name","");
                    ginfo.put("s_name","");
                    ginfo.put("chess_name","");
                    ginfo.put("uid",userid);

                    docRef.set(joinmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                ginfoRef.set(ginfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Intent mainInt = new Intent(UserInfoActivity.this, MainActivity.class);
                                            startActivity(mainInt);
                                            userprog.setVisibility(View.INVISIBLE);
                                            proceed.setEnabled(true);
                                            finish();
                                        }
                                    }
                                });
                            }else {
                                userinfoerror.setText(task.getException().toString());
                                userprog.setVisibility(View.INVISIBLE);
                                proceed.setEnabled(true);
                            }
                        }
                    });

                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    if(ds.exists()){
                        Toast.makeText(UserInfoActivity.this, "Already have an Account", Toast.LENGTH_SHORT).show();
                        Intent mainInt = new Intent(UserInfoActivity.this,MainActivity.class);
                        startActivity(mainInt);
                        finish();
                    }else{
                        Toast.makeText(UserInfoActivity.this, "Verifying", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
