package com.gmf.gamersfieldesports.Userdetails;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
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
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class silverSpinActivity extends AppCompatActivity {


    private ImageView spinwheel;
    private Button spinbtn;
    private Button sspinClaimbtn;
    private String[] values;
    private int diamonds;
    private int trnum;
    private String res;
    //firebaase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mcurrentuser = mAuth.getCurrentUser();
    private String userId = mcurrentuser.getUid();
    private CollectionReference transRef;
    private DocumentReference userRef;

    //timer
    private CountDownTimer mtimer;
    private int sspin_count;
    private long sspinTimeleftinsec;
    private long sspinmEndtime;
    private boolean rewarded = false;

    //rewarded Ads
    StartAppAd rewardedVideo = new StartAppAd(this);
    private boolean Adloaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silver_spin);

        //referance
        spinwheel = findViewById(R.id.sspinimg);
        spinbtn = findViewById(R.id.sspinbtn);
        transRef = db.collection("users").document(userId).collection("Transactions");
        userRef = db.collection("users").document(userId);
        //spin claim btn
        sspinClaimbtn = findViewById(R.id.sspinclaimbtn);
        sspinClaimbtn.setEnabled(false);

        StartAppSDK.init(this, getString(R.string.startAppId), true);

        values = new String[]{"10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
        Collections.reverse(Arrays.asList(values));

        spinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disable button
                spinbtn.setEnabled(false);
                Random random = new Random();
                int degree = random.nextInt(360);

                RotateAnimation rotateAnimation = new RotateAnimation(0, 1800 + degree,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(5000);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setInterpolator(new DecelerateInterpolator());
                rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (sspinTimeleftinsec == 0) {
                            sspinClaimbtn.setVisibility(View.VISIBLE);
                            sspinClaimbtn.setEnabled(true);
                            spinbtn.setVisibility(View.INVISIBLE);
                            spinbtn.setEnabled(false);
                            calculatepoints(degree);
                        } else {
                            spinbtn.setVisibility(View.INVISIBLE);
                            spinbtn.setEnabled(false);
                            Toast.makeText(silverSpinActivity.this, "Please wait for" + sspinTimeleftinsec + "seconds", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                });

                spinwheel.startAnimation(rotateAnimation);
            }

            //calculate the degree
            private void calculatepoints(int degree) {
                int initdegree = 0;
                int finaldegree = 36;
                res = null;
                int i = 0;
                do {
                    if (degree >= initdegree && degree <= finaldegree) {
                        res = values[i];
                    }
                    initdegree += 36;
                    finaldegree += 36;
                    i++;
                } while (res == null);
                Toast.makeText(silverSpinActivity.this, res, Toast.LENGTH_SHORT).show();
            }
        });
        //spinclaim btn
        sspinClaimbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Adloaded) {
                    rewardedVideo.showAd();
                    checkListner();
                } else {
                    loadAd();
                    Toast.makeText(silverSpinActivity.this, "Please wait while we load the Video", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadAd() {

        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                Adloaded = true;
                Toast.makeText(silverSpinActivity.this, "Video is loaded. Please watch", Toast.LENGTH_SHORT).show();
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
                //update
                diamonds = Integer.parseInt(res);
                Map<String, Object> transMap = new HashMap<>();
                transMap.put("type", "Silver Spin Wheel");
                transMap.put("winDiamonds", diamonds + " Diamonds");
                transMap.put("transactionNo", trnum + 1);
                //updating Dbalance
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            int db = Integer.parseInt(documentSnapshot.get("Dbalance").toString());
                            int newdb = db + diamonds;
                            userRef.update("Dbalance", newdb + "");
                        } else {
                            Toast.makeText(silverSpinActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //add tras history
                transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            rewarded = true;
                            timer();
                        } else {
                            Toast.makeText(silverSpinActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //invisible claim btn
                sspinClaimbtn.setVisibility(View.INVISIBLE);
                sspinClaimbtn.setEnabled(false);
                //make spin button visible
                spinbtn.setEnabled(false);
                spinbtn.setVisibility(View.INVISIBLE);

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //trans number
        transRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    trnum = task.getResult().size();
                } else {
                    Toast.makeText(silverSpinActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //shared preferance
        //sharde prefs
        SharedPreferences silverspinPreferences = getSharedPreferences(getString(R.string.ss_remaining), MODE_PRIVATE);
        sspinTimeleftinsec = silverspinPreferences.getLong("Timeleft", sspin_count);
        sspinmEndtime = silverspinPreferences.getLong("mEndtime", 0);
        sspinTimeleftinsec = sspinmEndtime - System.currentTimeMillis() / 1000;
        if (sspinTimeleftinsec <= 0) {
            sspinTimeleftinsec = 0;
            //rewarded = false;
        }
        //timer check
        if (sspinTimeleftinsec != 0) {
            spinbtn.setVisibility(View.INVISIBLE);
            spinbtn.setEnabled(false);
            //rewarded = true;
            Toast.makeText(this, "Please wait for" + sspinTimeleftinsec + "seconds", Toast.LENGTH_SHORT).show();
        }

        //rewarded Ad loading
        loadAd();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //sharedpref
        SharedPreferences silverspinPreferences = getSharedPreferences(getString(R.string.ss_remaining), MODE_PRIVATE);
        SharedPreferences.Editor editor = silverspinPreferences.edit();
        editor.putLong("mEndtime", sspinmEndtime);
        editor.putLong("Timeleft", sspinTimeleftinsec);
        editor.apply();
    }

    private void timer() {

        sspin_count = 600000;
        mtimer = new CountDownTimer(sspin_count, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sspinTimeleftinsec = millisUntilFinished / 1000;
                sspinmEndtime = sspinTimeleftinsec + System.currentTimeMillis() / 1000;
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