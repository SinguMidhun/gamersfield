package com.gmf.gamersfieldesports.Userdetails;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.startapp.sdk.adsbase.AutoInterstitialPreferences;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.util.HashMap;
import java.util.Map;

public class WatchvideoActivity extends AppCompatActivity {

    private Button watchvideobtn;
    private String coins;
    private int trnum;
    private ProgressBar watchvideoprog;

    //rewarded Ads
    StartAppAd rewardedVideo = new StartAppAd(this);
    private boolean Adloaded = false;

    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mcurrentuser = mAuth.getCurrentUser();
    private String userId = mcurrentuser.getUid();
    private DocumentReference userDoc;
    private CollectionReference transRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchvideo);

        //referance
        watchvideobtn = findViewById(R.id.watchvideobtn);
        watchvideoprog = findViewById(R.id.watchvideoprog);

        //firebase
        userDoc = db.collection("users").document(userId);
        transRef = db.collection("users").document(userId).collection("Transactions");

        StartAppSDK.init(this, getString(R.string.startAppId), true);

        StartAppAd.setAutoInterstitialPreferences(
                new AutoInterstitialPreferences()
                        .setSecondsBetweenAds(60)
        );

        rewardedVideo.setVideoListener(new VideoListener() {
            @Override
            public void onVideoCompleted() {
                String newCoin = Integer.parseInt(coins) + 2 + "";
                userDoc.update("Balance", newCoin).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> transMap = new HashMap<>();
                            transMap.put("transactionNo", trnum + 1);
                            transMap.put("type", "Watch Video");
                            transMap.put("winDiamonds", "2 Coins");
                            transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(WatchvideoActivity.this, "2 Coins Added Successfully", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(WatchvideoActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(WatchvideoActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        watchvideobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Adloaded) {
                    rewardedVideo.showAd();
                } else {
                    loadAd();
                    Toast.makeText(WatchvideoActivity.this, "Please wait while we load the Video", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void loadAd() {
        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                Adloaded = true;
                Toast.makeText(WatchvideoActivity.this, "Video is loaded. Please watch", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                // Can't show rewarded video
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        watchvideoprog.setVisibility(View.VISIBLE);
        userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    coins = documentSnapshot.get("Balance").toString();
                    watchvideoprog.setVisibility(View.GONE);
                } else {
                    Toast.makeText(WatchvideoActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    watchvideoprog.setVisibility(View.GONE);
                }
            }
        });
        transRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    trnum = task.getResult().size();
                } else {
                    Toast.makeText(WatchvideoActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    watchvideoprog.setVisibility(View.GONE);
                }
            }
        });

        //rewarded video
        loadAd();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StartAppAd.onBackPressed(this);
    }
}