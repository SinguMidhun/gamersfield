package com.gmf.gamersfieldesports.Userdetails;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anupkumarpanwar.scratchview.ScratchView;
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
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class silverScratchActivity extends AppCompatActivity{

    private ScratchView scratchView;
    private Button claim_button;
    private TextView dWon;
    private TextView infoTxt;
    private int diamonds;

    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mcurrentuser = mAuth.getCurrentUser();
    private String userId = mcurrentuser.getUid();
    private CollectionReference transRef;
    private DocumentReference userRef;
    private int trnum;
    private String type;

    //timer
    private CountDownTimer mstimer;
    private int ts_count;
    private long sTimeleftinsec;
    private long msEndtime;
    private boolean rewarded=false;

    //rewarded Ads
    StartAppAd rewardedVideo = new StartAppAd(this);
    private boolean Adloaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silver_scratch);

        //referance
        scratchView = findViewById(R.id.sscratch_view);
        dWon = findViewById(R.id.sd_won);
        //claim button
        claim_button = findViewById(R.id.sscratch_claim);
        claim_button.setVisibility(View.INVISIBLE);
        claim_button.setEnabled(false);
        //timer_counter text
        infoTxt = findViewById(R.id.sinfoTxt);
        //Firebase
        transRef = db.collection("users").document(userId).collection("Transactions");
        userRef = db.collection("users").document(userId);

        StartAppSDK.init(this, getString(R.string.startAppId), true);

        //intent String
        type = getIntent().getStringExtra("type");
        //initial diamonds generation
        if (type.equals("golden")) {
            Random rr = new Random();
            diamonds = rr.nextInt(900) + 100;
            dWon.setText(String.valueOf(diamonds));
            infoTxt.setText(getString(R.string.g_scartch_30min));
        }
        if (type.equals("silver")) {
            Random rr = new Random();
            diamonds = rr.nextInt(90) + 10;
            dWon.setText(String.valueOf(diamonds));
            infoTxt.setText(getString(R.string.s_scartch_10min));

        }

        scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                scratchView.reveal();
                if (sTimeleftinsec == 0){
                    claim_button.setVisibility(View.VISIBLE);
                    claim_button.setEnabled(true);
                }else{
                    Toast.makeText(silverScratchActivity.this, "Please wait for"+sTimeleftinsec+"Seconds", Toast.LENGTH_SHORT).show();
                    claim_button.setEnabled(false);
                    scratchView.mask();
                }

            }
            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                if (percent == 0.5) { }
            }
        });

        claim_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scratchView.mask();
                if (Adloaded) {
                    rewardedVideo.showAd();
                    checkListner();
                } else {
                    loadAd();
                    Toast.makeText(silverScratchActivity.this, "Please wait while we load the Video", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadAd() {

        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                Adloaded = true;
                Toast.makeText(silverScratchActivity.this, "Video is loaded. Please watch", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                // Can't show rewarded video
            }
        });

    }

    private void checkListner() {

        rewardedVideo.setVideoListener(new VideoListener() {
            @Override
            public void onVideoCompleted() {
                //transaction details
                Map<String, Object> tranMap = new HashMap<>();
                tranMap.put("winDiamonds", diamonds+" Diamonds");
                tranMap.put("type", "Silver Scratch Card");
                tranMap.put("transactionNo", trnum + 1);

                //updating Dbalance
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            int db = Integer.parseInt(documentSnapshot.get("Dbalance").toString());
                            int newdb = db + diamonds;
                            userRef.update("Dbalance", newdb + "");
                        }

                    }
                });
                //adding transaction and generating new diamonds
                transRef.add(tranMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            rewarded = true;
                            if (type.equals("silver")) {
                                Random rr = new Random();
                                diamonds = rr.nextInt(100);
                                dWon.setText(String.valueOf(diamonds));
                            }
                            if (type.equals("golden")) {
                                Random rr = new Random();
                                diamonds = rr.nextInt(900) + 100;
                                dWon.setText(String.valueOf(diamonds));
                            }

                        } else {
                            Toast.makeText(silverScratchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        claim_button.setEnabled(false);
        claim_button.setVisibility(View.INVISIBLE);
        if (rewarded){
            timer();
            Toast.makeText(silverScratchActivity.this, "Rewarded added", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(silverScratchActivity.this, "Rewarded not added", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        //transaction number
        transRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    trnum = task.getResult().size();
                } else {
                    Toast.makeText(silverScratchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //sharde prefs
        SharedPreferences silverSharedPreferences = getSharedPreferences(getString(R.string.ts_remaining),MODE_PRIVATE);
        sTimeleftinsec = silverSharedPreferences.getLong("sTimeleft",ts_count);
        msEndtime = silverSharedPreferences.getLong("msEndtime",0);
        sTimeleftinsec = msEndtime - System.currentTimeMillis()/1000;
        if (sTimeleftinsec <= 0){
            sTimeleftinsec = 0;
        }

        loadAd();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //sharedpref
        SharedPreferences silverSharedPreferences = getSharedPreferences(getString(R.string.ts_remaining),MODE_PRIVATE);
        SharedPreferences.Editor editor = silverSharedPreferences.edit();
        editor.putLong("msEndtime",msEndtime);
        editor.putLong("sTimeleft",sTimeleftinsec);
        editor.apply();
    }



    private void timer() {

        if (type.equals("silver")){
            ts_count = 600000;
        }if (type.equals("golden")){
            ts_count = 1800000;
        }
        mstimer = new CountDownTimer(ts_count,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sTimeleftinsec = millisUntilFinished/1000;
                msEndtime = sTimeleftinsec + System.currentTimeMillis()/1000;
            }

            @Override
            public void onFinish() {

            }
        }.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StartAppAd.onBackPressed(this);
    }

}