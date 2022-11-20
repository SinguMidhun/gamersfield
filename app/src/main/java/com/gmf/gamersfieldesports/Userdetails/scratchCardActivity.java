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

public class scratchCardActivity extends AppCompatActivity{

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
    private CountDownTimer mtimer;
    private int t_count;
    private long Timeleftinsec;
    private long mEndtime;
    private boolean rewarded = false;

    //rewarded Ads
    StartAppAd rewardedVideo = new StartAppAd(this);
    private boolean Adloaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_card);

        //referance
        scratchView = findViewById(R.id.scratch_view);
        dWon = findViewById(R.id.d_won);
        //claim button
        claim_button = findViewById(R.id.scratch_claim);
        claim_button.setVisibility(View.INVISIBLE);
        claim_button.setEnabled(false);
        //timer_counter text
        infoTxt = findViewById(R.id.infoTxt);
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
                if (Timeleftinsec == 0){
                    claim_button.setVisibility(View.VISIBLE);
                    claim_button.setEnabled(true);
                }else{
                    Toast.makeText(scratchCardActivity.this, "Please wait for"+Timeleftinsec+"Seconds", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(scratchCardActivity.this, "Please wait while we load the Video", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadAd() {

        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                Adloaded = true;
                Toast.makeText(scratchCardActivity.this, "Video is loaded. Please watch", Toast.LENGTH_SHORT).show();
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
                tranMap.put("type", "Golden Scratch Card");
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
                            Toast.makeText(scratchCardActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                claim_button.setEnabled(false);
                claim_button.setVisibility(View.INVISIBLE);
                if (rewarded){
                    timer();
                    Toast.makeText(scratchCardActivity.this, "Rewarded added", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(scratchCardActivity.this, "Rewarded not added", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                    Toast.makeText(scratchCardActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //sharde prefs
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.t_remaining),MODE_PRIVATE);
        Timeleftinsec = sharedPreferences.getLong("Timeleft",t_count);
        mEndtime = sharedPreferences.getLong("mEndtime",0);
        Timeleftinsec = mEndtime - System.currentTimeMillis()/1000;
        if (Timeleftinsec <= 0){
            Timeleftinsec = 0;
        }

        loadAd();
    }


    @Override
    protected void onStop() {
        super.onStop();
        //sharedpref
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.t_remaining),MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("mEndtime",mEndtime);
        editor.putLong("Timeleft",Timeleftinsec);
        editor.apply();
    }

    private void timer() {

        if (type.equals("silver")){
            t_count = 600000;
        }if (type.equals("golden")){
            t_count = 1800000;
        }
        mtimer = new CountDownTimer(t_count,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Timeleftinsec = millisUntilFinished/1000;
                mEndtime = Timeleftinsec + System.currentTimeMillis()/1000;
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