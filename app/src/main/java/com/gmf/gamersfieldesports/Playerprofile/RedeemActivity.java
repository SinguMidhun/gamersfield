package com.gmf.gamersfieldesports.Playerprofile;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.gmf.gamersfieldesports.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RedeemActivity extends AppCompatActivity {

    private CardView pcard1;
    private CardView pcard2;
    private CardView pcard3;
    private CardView fcard1;
    private CardView fcard2;
    private CardView fcard3;
    private CardView ffccard1;
    private CardView ffccard2;
    private CardView ffccard3;
    private CardView ffccard4;
    private CardView ffccard5;
    private CardView codm1;
    private CardView codm2;

    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mcurrentuser = mAuth.getCurrentUser();
    private String userId = mcurrentuser.getUid();
    private DocumentReference userRef;
    private CollectionReference transRef;
    private CollectionReference Redeemhistory;
    private CollectionReference redeemRef;
    //user balance variables
    private String balance;
    private String userNum;
    private int intbalance;
    private int trnum;
    private int redeemnum;
    private int redeemhistorynum;
    private CharSequence date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        //referance
        pcard1 = findViewById(R.id.pcard1);
        pcard2 = findViewById(R.id.pcard2);
        pcard3 = findViewById(R.id.pcard3);
        fcard1 = findViewById(R.id.fcard1);
        fcard2 = findViewById(R.id.fcard2);
        fcard3 = findViewById(R.id.fcard3);
        ffccard1 = findViewById(R.id.ffccard1);
        ffccard2 = findViewById(R.id.ffccard2);
        ffccard3 = findViewById(R.id.ffccard3);
        ffccard4 = findViewById(R.id.ffccard4);
        ffccard5 = findViewById(R.id.ffccard5);
        codm1 = findViewById(R.id.cod1);
        codm2 = findViewById(R.id.cod2);

        //userRef
        redeemRef = db.collection("Redeem");
        userRef = db.collection("users").document(userId);
        transRef = userRef.collection("Transactions");
        Redeemhistory = userRef.collection("RedeemHistory");
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    balance = documentSnapshot.get("Balance").toString();
                    userNum = documentSnapshot.get("Phone").toString();
                    intbalance = Integer.parseInt(balance);
                }else{
                    Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //date
        Date d = new Date();
        date  = DateFormat.format("MMMM d, yyyy ", d.getTime());

        //pubgcard1
        pcard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intbalance>=1500){
                    int newbalance = intbalance - 1500;
                    intbalance = newbalance;
                    //redeem num
                    Map<String,Object> redeemmap = new HashMap<>();
                    redeemmap.put("redeemItem","32UC");
                    redeemmap.put("transactionNo",redeemnum+1);
                    redeemmap.put("userPhone",userNum);
                    redeemmap.put("date",date);
                    //redeem history
                    Map<String,Object> redeemhismap = new HashMap<>();
                    redeemhismap.put("redeemItem","32UC");
                    redeemhismap.put("transactionNo",redeemhistorynum+1);
                    redeemhismap.put("Status","pending");
                    redeemhismap.put("date",date);
                    //trans num
                    Map<String,Object>transMap = new HashMap<>();
                    transMap.put("type","UC Redeemed");
                    transMap.put("winDiamonds","32 UC");
                    transMap.put("transactionNo",trnum+1);
                    //update balance
                    userRef.update("Balance",newbalance+"");
                    //add redeem trans
                    redeemRef.add(redeemmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                rednum();
                                Toast.makeText(RedeemActivity.this, "Processing ,please wait....", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //personal redeem history
                    redeemhistrans(redeemhismap);
                    //add trans
                    transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                transnum();
                                Toast.makeText(RedeemActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(RedeemActivity.this, "You don't have sufficient coins", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //pcard2
        pcard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intbalance >= 3000){
                    int newbalance = intbalance - 3000;
                    intbalance = newbalance;
                    //redeem num
                    Map<String,Object> redeemmap = new HashMap<>();
                    redeemmap.put("redeemItem","64UC");
                    redeemmap.put("transactionNo",redeemnum+1);
                    redeemmap.put("userPhone",userNum);
                    redeemmap.put("date",date);
                    //redeem history
                    Map<String,Object> redeemhismap = new HashMap<>();
                    redeemhismap.put("redeemItem","64UC");
                    redeemhismap.put("transactionNo",redeemhistorynum+1);
                    redeemhismap.put("Status","Pending");
                    redeemhismap.put("date",date);
                    //trans num
                    Map<String,Object>transMap = new HashMap<>();
                    transMap.put("type","UC Redeemed");
                    transMap.put("winDiamonds","64 UC");
                    transMap.put("transactionNo",trnum+1);
                    //update balance
                    userRef.update("Balance",newbalance+"");
                    //add redeem trans
                    redeemRef.add(redeemmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                rednum();
                                Toast.makeText(RedeemActivity.this, "Processing, please wait....", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //personal redeem history
                    redeemhistrans(redeemhismap);
                    //add trans
                    transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                transnum();
                                Toast.makeText(RedeemActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RedeemActivity.this, "You don't have sufficient coins", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //pcard3
        pcard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intbalance>=4500){
                    int newbalance = intbalance - 4500;
                    intbalance = newbalance;
                    //redeem num
                    Map<String,Object> redeemmap = new HashMap<>();
                    redeemmap.put("redeemItem","96UC");
                    redeemmap.put("transactionNo",redeemnum+1);
                    redeemmap.put("userPhone",userNum);
                    redeemmap.put("date",date);
                    //redeem history
                    Map<String,Object> redeemhismap = new HashMap<>();
                    redeemhismap.put("redeemItem","96UC");
                    redeemhismap.put("transactionNo",redeemhistorynum+1);
                    redeemhismap.put("Status","pending");
                    redeemhismap.put("date",date);
                    //trans num
                    Map<String,Object>transMap = new HashMap<>();
                    transMap.put("type","UC Redeemed");
                    transMap.put("winDiamonds","96 UC");
                    transMap.put("transactionNo",trnum+1);
                    //update balance
                    userRef.update("Balance",newbalance+"");
                    //add redeem trans
                    redeemRef.add(redeemmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                rednum();
                                Toast.makeText(RedeemActivity.this, "Processing, please wait....", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //personal redeem history
                    redeemhistrans(redeemhismap);
                    //add trans
                    transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                transnum();
                                Toast.makeText(RedeemActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RedeemActivity.this, "You don't have sufficient coins", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fcard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intbalance>=2000){
                    int newbalance = intbalance - 2000;
                    intbalance = newbalance;
                    //redeem num
                    Map<String,Object> redeemmap = new HashMap<>();
                    redeemmap.put("redeemItem","50 Diamonds");
                    redeemmap.put("userPhone",userNum);
                    redeemmap.put("transactionNo",redeemnum+1);
                    redeemmap.put("date",date);
                    //redeem history
                    Map<String,Object> redeemhismap = new HashMap<>();
                    redeemhismap.put("redeemItem","50 Diamonds");
                    redeemhismap.put("Status","pending");
                    redeemhismap.put("transactionNo",redeemhistorynum+1);
                    redeemhismap.put("date",date);
                    //trans num
                    Map<String,Object>transMap = new HashMap<>();
                    transMap.put("type","Diamonds Redeemed");
                    transMap.put("winDiamonds","50 Diamonds");
                    transMap.put("transactionNo",trnum+1);
                    //update balance
                    userRef.update("Balance",newbalance+"");
                    //add redeem trans
                    redeemRef.add(redeemmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                rednum();
                                Toast.makeText(RedeemActivity.this, "Processing, please wait....", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //personal redeem history
                    redeemhistrans(redeemhismap);
                    //add trans
                    transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                transnum();
                                Toast.makeText(RedeemActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RedeemActivity.this, "You don't have sufficient coins", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fcard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intbalance >= 4000){
                    int newbalance = intbalance - 4000;
                    intbalance = newbalance;
                    //redeem num
                    Map<String,Object> redeemmap = new HashMap<>();
                    redeemmap.put("redeemItem","100 Diamonds");
                    redeemmap.put("userPhone",userNum);
                    redeemmap.put("transactionNo",redeemnum+1);
                    redeemmap.put("date",date);
                    //redeem history
                    Map<String,Object> redeemhismap = new HashMap<>();
                    redeemhismap.put("redeemItem","100 Diamonds");
                    redeemhismap.put("Status","Pending");
                    redeemhismap.put("transactionNo",redeemhistorynum+1);
                    redeemhismap.put("date",date);
                    //trans num
                    Map<String,Object>transMap = new HashMap<>();
                    transMap.put("type","Diamonds Redeemed");
                    transMap.put("winDiamonds","100 Diamonds");
                    transMap.put("transactionNo",trnum+1);
                    //update balance
                    userRef.update("Balance",newbalance+"");
                    //add redeem trans
                    redeemRef.add(redeemmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                rednum();
                                Toast.makeText(RedeemActivity.this, "Processing, please wait....", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //personal redeem history
                    redeemhistrans(redeemhismap);
                    //add trans
                    transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                transnum();
                                Toast.makeText(RedeemActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RedeemActivity.this, "You don't have sufficient coins", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fcard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intbalance>=6000){
                    int newbalance = intbalance - 6000;
                    intbalance = newbalance;
                    //redeem num
                    Map<String,Object> redeemmap = new HashMap<>();
                    redeemmap.put("redeemItem","150 Diamonds");
                    redeemmap.put("userPhone",userNum);
                    redeemmap.put("transactionNo",redeemnum+1);
                    redeemmap.put("date",date);
                    //redeem history
                    Map<String,Object> redeemhismap = new HashMap<>();
                    redeemhismap.put("redeemItem","150 Diamonds");
                    redeemhismap.put("Status","Pending");
                    redeemhismap.put("transactionNo",redeemhistorynum+1);
                    redeemhismap.put("date",date);
                    //trans num
                    Map<String,Object>transMap = new HashMap<>();
                    transMap.put("type","Diamonds Redeemed");
                    transMap.put("winDiamonds","150 Diamonds");
                    transMap.put("transactionNo",trnum+1);
                    //update balance
                    userRef.update("Balance",newbalance+"");
                    //add redeem trans
                    redeemRef.add(redeemmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                rednum();
                                Toast.makeText(RedeemActivity.this, "Processing, please wait....", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //personal redeem history
                    redeemhistrans(redeemhismap);
                    //add trans
                    transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                transnum();
                                Toast.makeText(RedeemActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RedeemActivity.this, "You don't have sufficient coins", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ffccard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intbalance>=18000){
                    int newbalance = intbalance - 18000;
                    intbalance = newbalance;
                    //redeem num
                    Map<String,Object> redeemmap = new HashMap<>();
                    redeemmap.put("redeemItem","Alok Character");
                    redeemmap.put("userPhone",userNum);
                    redeemmap.put("transactionNo",redeemnum+1);
                    redeemmap.put("date",date);
                    //redeem history num
                    Map<String,Object> redeemhismap = new HashMap<>();
                    redeemhismap.put("redeemItem","Alok character");
                    redeemhismap.put("Status","Pending");
                    redeemhismap.put("transactionNo",redeemhistorynum+1);
                    redeemhismap.put("date",date);

                    //trans num
                    Map<String,Object>transMap = new HashMap<>();
                    transMap.put("type","Character Redeem");
                    transMap.put("winDiamonds","Alok character");
                    transMap.put("transactionNo",trnum+1);
                    //update balance
                    userRef.update("Balance",newbalance+"");
                    //add redeem trans
                    redeemRef.add(redeemmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                rednum();
                                redeemhistrans(redeemhismap);
                                Toast.makeText(RedeemActivity.this, "Processing, please wait....", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //add trans
                    transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                transnum();
                                Toast.makeText(RedeemActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RedeemActivity.this, "You don't have sufficient coins", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ffccard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intbalance>=18000){
                    int newbalance = intbalance - 18000;
                    intbalance = newbalance;
                    //redeem num
                    Map<String,Object> redeemmap = new HashMap<>();
                    redeemmap.put("redeemItem","Jota character");
                    redeemmap.put("userPhone",userNum);
                    redeemmap.put("transactionNo",redeemnum+1);
                    redeemmap.put("date",date);
                    //redeem history
                    Map<String,Object> redeemhismap = new HashMap<>();
                    redeemhismap.put("redeemItem","Jota character");
                    redeemhismap.put("Status","Pending");
                    redeemhismap.put("transactionNo",redeemhistorynum+1);
                    redeemhismap.put("date",date);
                    //trans num
                    Map<String,Object>transMap = new HashMap<>();
                    transMap.put("type","Character Redeem");
                    transMap.put("winDiamonds","Jota Character");
                    transMap.put("transactionNo",trnum+1);
                    //update balance
                    userRef.update("Balance",newbalance+"");
                    //add redeem trans
                    redeemRef.add(redeemmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                rednum();
                                Toast.makeText(RedeemActivity.this, "Processing, please wait....", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //add personal redeem his
                    redeemhistrans(redeemhismap);
                    //add trans
                    transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                transnum();
                                Toast.makeText(RedeemActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RedeemActivity.this, "You don't have sufficient coins", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ffccard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intbalance>=18000){
                    int newbalance = intbalance - 18000;
                    intbalance = newbalance;
                    //redeem num
                    Map<String,Object> redeemmap = new HashMap<>();
                    redeemmap.put("redeemItem","K character");
                    redeemmap.put("userPhone",userNum);
                    redeemmap.put("transactionNo",redeemnum+1);
                    redeemmap.put("date",date);
                    //redeem history
                    Map<String,Object> redeemhismap = new HashMap<>();
                    redeemhismap.put("redeemItem","K character");
                    redeemhismap.put("Status","Pending");
                    redeemhismap.put("transactionNo",redeemhistorynum+1);
                    redeemhismap.put("date",date);
                    //trans num
                    Map<String,Object>transMap = new HashMap<>();
                    transMap.put("type","Character Redeem");
                    transMap.put("winDiamonds","K Character");
                    transMap.put("transactionNo",trnum+1);
                    //update balance
                    userRef.update("Balance",newbalance+"");
                    //add redeem trans
                    redeemRef.add(redeemmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                rednum();
                                Toast.makeText(RedeemActivity.this, "Processing, please wait....", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //personal redeem history
                    redeemhistrans(redeemhismap);
                    //add trans
                    transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                transnum();
                                Toast.makeText(RedeemActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RedeemActivity.this, "You don't have sufficient coins", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ffccard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intbalance>=18000){
                    int newbalance = intbalance - 18000;
                    intbalance = newbalance;
                    //redeem num
                    Map<String,Object> redeemmap = new HashMap<>();
                    redeemmap.put("redeemItem","Hayato Character");
                    redeemmap.put("userPhone",userNum);
                    redeemmap.put("transactionNo",redeemnum+1);
                    redeemmap.put("date",date);
                    //redeem history
                    Map<String,Object> redeemhismap = new HashMap<>();
                    redeemhismap.put("redeemItem","Hayato Character");
                    redeemhismap.put("Status","Pending");
                    redeemhismap.put("transactionNo",redeemhistorynum+1);
                    redeemhismap.put("date",date);
                    //trans num
                    Map<String,Object>transMap = new HashMap<>();
                    transMap.put("type","Character Redeemed");
                    transMap.put("winDiamonds","Hayato Character");
                    transMap.put("transactionNo",trnum+1);
                    //update balance
                    userRef.update("Balance",newbalance+"");
                    //add redeem trans
                    redeemRef.add(redeemmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                rednum();
                                Toast.makeText(RedeemActivity.this, "Processing, please wait....", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //personal redeem history
                    redeemhistrans(redeemhismap);
                    //add trans
                    transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                transnum();
                                Toast.makeText(RedeemActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RedeemActivity.this, "You don't have sufficient coins", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ffccard5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intbalance>=18000){
                    int newbalance = intbalance - 18000;
                    intbalance = newbalance;
                    //redeem num
                    Map<String,Object> redeemmap = new HashMap<>();
                    redeemmap.put("redeemItem","Ronaldo Character");
                    redeemmap.put("userPhone",userNum);
                    redeemmap.put("transactionNo",redeemnum+1);
                    redeemmap.put("date",date);
                    //redeem history
                    Map<String,Object> redeemhismap = new HashMap<>();
                    redeemhismap.put("redeemItem","Ronaldo Character");
                    redeemhismap.put("Status","Pending");
                    redeemhismap.put("transactionNo",redeemhistorynum+1);
                    redeemhismap.put("date",date);
                    //trans num
                    Map<String,Object>transMap = new HashMap<>();
                    transMap.put("type","Character Redeemed");
                    transMap.put("winDiamonds","Ronaldo Character");
                    transMap.put("transactionNo",trnum+1);
                    //update balance
                    userRef.update("Balance",newbalance+"");
                    //add redeem trans
                    redeemRef.add(redeemmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                rednum();
                                Toast.makeText(RedeemActivity.this, "Processing, please wait....", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //personal redeem history
                    redeemhistrans(redeemhismap);
                    //add trans
                    transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                transnum();
                                Toast.makeText(RedeemActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RedeemActivity.this, "You don't have sufficient coins", Toast.LENGTH_SHORT).show();
                }
            }
        });

        codm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intbalance>=5000){
                    int newbalance = intbalance - 5000;
                    intbalance = newbalance;
                    //redeem num
                    Map<String,Object> redeemmap = new HashMap<>();
                    redeemmap.put("redeemItem","80 CP");
                    redeemmap.put("userPhone",userNum);
                    redeemmap.put("transactionNo",redeemnum+1);
                    redeemmap.put("date",date);
                    //redeem history
                    Map<String,Object> redeemhismap = new HashMap<>();
                    redeemhismap.put("redeemItem","80 CP");
                    redeemhismap.put("Status","Pending");
                    redeemhismap.put("transactionNo",redeemhistorynum+1);
                    redeemhismap.put("date",date);
                    //trans num
                    Map<String,Object>transMap = new HashMap<>();
                    transMap.put("type","CP Redeemed");
                    transMap.put("winDiamonds","80 CP");
                    transMap.put("transactionNo",trnum+1);
                    //update balance
                    userRef.update("Balance",newbalance+"");
                    //add redeem trans
                    redeemRef.add(redeemmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                rednum();
                                Toast.makeText(RedeemActivity.this, "Processing, please wait....", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //personal redeem history
                    redeemhistrans(redeemhismap);
                    //add trans
                    transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                transnum();
                                Toast.makeText(RedeemActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RedeemActivity.this, "You don't have sufficient coins", Toast.LENGTH_SHORT).show();
                }
            }
        });

        codm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intbalance>=10000){
                    int newbalance = intbalance - 10000;
                    intbalance = newbalance;
                    //redeem num
                    Map<String,Object> redeemmap = new HashMap<>();
                    redeemmap.put("redeemItem","160 CP");
                    redeemmap.put("userPhone",userNum);
                    redeemmap.put("transactionNo",redeemnum+1);
                    redeemmap.put("date",date);
                    //redeem history
                    Map<String,Object> redeemhismap = new HashMap<>();
                    redeemhismap.put("redeemItem","160 CP");
                    redeemhismap.put("Status","Pending");
                    redeemhismap.put("transactionNo",redeemhistorynum+1);
                    redeemhismap.put("date",date);
                    //trans num
                    Map<String,Object>transMap = new HashMap<>();
                    transMap.put("type","CP Redeemed");
                    transMap.put("winDiamonds","160 CP");
                    transMap.put("transactionNo",trnum+1);
                    //update balance
                    userRef.update("Balance",newbalance+"");
                    //add redeem trans
                    redeemRef.add(redeemmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                rednum();
                                Toast.makeText(RedeemActivity.this, "Processing, please wait....", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //personal redeem history
                    redeemhistrans(redeemhismap);
                    //add trans
                    transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                transnum();
                                Toast.makeText(RedeemActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RedeemActivity.this, "You don't have sufficient coins", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void redeemhistrans(Map<String, Object> redeemhismap) {
        Redeemhistory.add(redeemhismap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    redeemhistory();
                }else{
                    Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        transnum();
        rednum();
        redeemhistory();
    }

    private void redeemhistory(){
        Redeemhistory.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    redeemhistorynum = querySnapshot.size();
                }else{
                    Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void rednum() {
        redeemRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    redeemnum = querySnapshot.size();
                } else {
                    Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.redeem_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.redeem_history:
                Intent redeemHistory = new Intent(RedeemActivity.this,RedeemHistoryActivity.class);
                startActivity(redeemHistory);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void transnum() {
        transRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    trnum = querySnapshot.size();
                } else {
                    Toast.makeText(RedeemActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}