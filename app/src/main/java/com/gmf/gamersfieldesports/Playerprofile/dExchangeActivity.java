package com.gmf.gamersfieldesports.Playerprofile;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
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
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.HashMap;
import java.util.Map;

public class dExchangeActivity extends AppCompatActivity {

    private CardView card1;
    private CardView card2;
    private CardView card3;
    private CardView card4;

    private String dbalance;
    private String balance;
    private int trnum;
    private int ndiamond;
    private int ncoins;
    private ProgressBar exgprogress;
    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser muser = FirebaseAuth.getInstance().getCurrentUser();
    private DocumentReference userRef;
    private CollectionReference transRef;
    private String userId = muser.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_exchange);



        //referance
        card1 = findViewById(R.id.exg1card);
        card2 = findViewById(R.id.exg2card);
        card3 = findViewById(R.id.exg3card);
        card4 = findViewById(R.id.exg4card);
        exgprogress = findViewById(R.id.dexgprog);
        //firebase
        userRef = db.collection("users").document(userId);
        transRef = userRef.collection("Transactions");
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    dbalance = documentSnapshot.get("Dbalance").toString();
                    balance = documentSnapshot.get("Balance").toString();
                    //converting balances into integers
                    ndiamond = Integer.parseInt(dbalance);
                    ncoins = Integer.parseInt(balance);
                } else {
                    Toast.makeText(dExchangeActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //card1 10,000=20
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ndiamond >= 10000) {
                    exgprogress.setVisibility(View.VISIBLE);
                    int newDiamonds = ndiamond - 10000;
                    int newbalance = ncoins + 20;
                    ndiamond = newDiamonds;
                    ncoins = newbalance;
                    Map<String, Object> dexgmap = new HashMap<>();
                    dexgmap.put("transactionNo", trnum + 1);
                    dexgmap.put("type", "Diamong Exchange");
                    dexgmap.put("winDiamonds", "20 Coins");
                    //update balances
                    userRef.update("Dbalance", newDiamonds + "");
                    userRef.update("Balance", newbalance + "");
                    transRef.add(dexgmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(dExchangeActivity.this, "Successfully Exchanged", Toast.LENGTH_SHORT).show();
                                tranumUpdate();
                                exgprogress.setVisibility(View.GONE);

                            }
                        }
                    });
                } else {
                    Toast.makeText(dExchangeActivity.this, "You don't have sufficient diamonds to exchange", Toast.LENGTH_SHORT).show();

                }
            }
        });
        //card2 15000=30coins
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ndiamond>=15000){
                    exgprogress.setVisibility(View.VISIBLE);
                    int newDiamonds = ndiamond - 15000;
                    int newbalance = ncoins + 30;
                    ndiamond = newDiamonds;
                    ncoins = newbalance;
                    Map<String, Object> dexgmap2 = new HashMap<>();
                    dexgmap2.put("transactionNo", trnum + 1);
                    dexgmap2.put("type", "Diamong Exchange");
                    dexgmap2.put("winDiamonds", "30 Coins");
                    //update balances
                    userRef.update("Dbalance", newDiamonds + "");
                    userRef.update("Balance", newbalance + "");
                    transRef.add(dexgmap2).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(dExchangeActivity.this, "Successfully Exchanged", Toast.LENGTH_SHORT).show();
                                tranumUpdate();
                                exgprogress.setVisibility(View.GONE);
                            }
                        }
                    });
                }else{
                    Toast.makeText(dExchangeActivity.this, "You don't have sufficient diamonds to exchange", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //card3 20000=40coins
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ndiamond>=20000){
                    exgprogress.setVisibility(View.VISIBLE);
                    int newDiamonds = ndiamond - 20000;
                    int newbalance = ncoins + 40;
                    ndiamond = newDiamonds;
                    ncoins = newbalance;
                    Map<String, Object> dexgmap3 = new HashMap<>();
                    dexgmap3.put("transactionNo", trnum + 1);
                    dexgmap3.put("type", "Diamong Exchange");
                    dexgmap3.put("winDiamonds", "40 Coins");
                    //update balances
                    userRef.update("Dbalance", newDiamonds + "");
                    userRef.update("Balance", newbalance + "");
                    transRef.add(dexgmap3).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(dExchangeActivity.this, "Successfully Exchanged", Toast.LENGTH_SHORT).show();
                                tranumUpdate();
                                exgprogress.setVisibility(View.GONE);
                            }
                        }
                    });
                }else {
                    Toast.makeText(dExchangeActivity.this, "You don't have sufficient diamonds to exchange", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //card4 25000=50coins
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ndiamond>=25000){
                    exgprogress.setVisibility(View.VISIBLE);
                    int newDiamonds = ndiamond - 25000;
                    int newbalance = ncoins + 50;
                    ndiamond = newDiamonds;
                    ncoins = newbalance;
                    Map<String, Object> dexgmap4 = new HashMap<>();
                    dexgmap4.put("transactionNo", trnum + 1);
                    dexgmap4.put("type", "Diamong Exchange");
                    dexgmap4.put("winDiamonds", "50 Coins");
                    //update balances
                    userRef.update("Dbalance", newDiamonds + "");
                    userRef.update("Balance", newbalance + "");
                    transRef.add(dexgmap4).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(dExchangeActivity.this, "Successfully Exchanged", Toast.LENGTH_SHORT).show();
                                tranumUpdate();
                                exgprogress.setVisibility(View.GONE);
                            }
                        }
                    });
                }else{
                    Toast.makeText(dExchangeActivity.this, "You don't have sufficient diamonds to exchange", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void tranumUpdate() {
        transRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    trnum = querySnapshot.size();
                } else {
                    Toast.makeText(dExchangeActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        tranumUpdate();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StartAppAd.onBackPressed(this);
    }
}