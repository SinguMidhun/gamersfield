package com.gmf.gamersfieldesports.Userdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.gmf.gamersfieldesports.Luckydraw.LuckydrawActivity;
import com.gmf.gamersfieldesports.Playerprofile.RedeemActivity;
import com.gmf.gamersfieldesports.Playerprofile.dExchangeActivity;
import com.gmf.gamersfieldesports.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WalletActivity extends AppCompatActivity{

    private RelativeLayout mc_layout;
    private RelativeLayout mw_layout;
    //card view
    private CardView watchVideoAds;
    private CardView luckydraw;
    private CardView goldenScratchCard;
    private CardView silverScratchCard;
    private CardView goldenSpin;
    private CardView silverSpin;
    //textview
    private TextView gcoins;
    private TextView gdiamonds;
    //strings
    private String diamonds;
    private String coins;
    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mcurrentuser = mAuth.getCurrentUser();
    private String userId = mcurrentuser.getUid();
    private DocumentReference userDoc;
    private CollectionReference transRef;
    //progress bar
    private ProgressBar wprogbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        mc_layout = findViewById(R.id.w_coinslayout);
        mw_layout = findViewById(R.id.w_diamondslayout);
        //card view
        watchVideoAds = findViewById(R.id.watchAds);
        luckydraw = findViewById(R.id.luckyDraw);
        goldenScratchCard = findViewById(R.id.goldenScratchCard);
        silverScratchCard = findViewById(R.id.silverScratchcard);
        goldenSpin = findViewById(R.id.goldenSpin);
        silverSpin = findViewById(R.id.silverSpin);
        //textview
        gcoins = findViewById(R.id.w_coins);
        gdiamonds = findViewById(R.id.w_daimond);
        //progressbar
        wprogbar = findViewById(R.id.walletprog);
        //firebase
        userDoc = db.collection("users").document(userId);
        transRef = db.collection("users").document(userId).collection("Transactions");
        //golden scarth card
        goldenScratchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scratchcard = new Intent(WalletActivity.this,scratchCardActivity.class);
                scratchcard.putExtra("type","golden");
                startActivity(scratchcard);
            }
        });
        //silver scartch card
        silverScratchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scratchcard = new Intent(WalletActivity.this,silverScratchActivity.class);
                scratchcard.putExtra("type","silver");
                startActivity(scratchcard);
            }
        });
        //goldenspin
        goldenSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gspinintent =  new Intent(WalletActivity.this,spinWheelActivity.class);
                gspinintent.putExtra("type","golden");
                startActivity(gspinintent);
            }
        });
        //silverspin
        silverSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sspinintent =  new Intent(WalletActivity.this,silverSpinActivity.class);
                sspinintent.putExtra("type","silver");
                startActivity(sspinintent);
            }
        });
        //watchvideo
        watchVideoAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent watchvideointent = new Intent(WalletActivity.this,WatchvideoActivity.class);
                startActivity(watchvideointent);
            }
        });
        //luckydraw
        luckydraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent luckyintent = new Intent(WalletActivity.this, LuckydrawActivity.class);
                startActivity(luckyintent);
            }
        });
        //mc coins
        mc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redeemwalletintent = new Intent(WalletActivity.this, RedeemActivity.class);
                startActivity(redeemwalletintent);
            }
        });
        //diamonds exchange
        mw_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dExchangeIntent = new Intent(WalletActivity.this, dExchangeActivity.class);
                startActivity(dExchangeIntent);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        wprogbar.setVisibility(View.VISIBLE);
        userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    coins = documentSnapshot.get("Balance").toString();
                    diamonds = documentSnapshot.get("Dbalance").toString();
                    //set the values to the views
                    gcoins.setText(coins);
                    gdiamonds.setText(diamonds);
                    wprogbar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(WalletActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    wprogbar.setVisibility(View.GONE);
                }
            }
        });
    }

}