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

public class spinWheelActivity extends AppCompatActivity{

    private ImageView spinwheel;
    private Button spinbtn;
    private Button gspinClaimbtn;
    private String[] values;
    private String res;
    private int diamonds;
    private int trnum;
    //firebaase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mcurrentuser = mAuth.getCurrentUser();
    private String userId = mcurrentuser.getUid();
    private CollectionReference transRef;
    private DocumentReference userRef;

    //timer
    private CountDownTimer mtimer;
    private int spin_count;
    private long spinTimeleftinsec;
    private long spinmEndtime;


    //rewarded Ads
    StartAppAd rewardedVideo = new StartAppAd(this);
    private boolean Adloaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_wheel);

        //referance
        spinwheel = findViewById(R.id.spinimg);
        spinbtn = findViewById(R.id.spinbtn);
        transRef = db.collection("users").document(userId).collection("Transactions");
        userRef = db.collection("users").document(userId);
        //spin claim btn
        gspinClaimbtn = findViewById(R.id.gspinclaimbtn);
        gspinClaimbtn.setEnabled(false);

        StartAppSDK.init(this, getString(R.string.startAppId), true);

        //rewards providing


        //spin wheel
        values = new String[]{"100", "200", "300", "400", "500", "600", "700", "800", "900", "1000"};
        Collections.reverse(Arrays.asList(values));
        //spin btn
        spinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disable button
                spinbtn.setEnabled(false);
                spinbtn.setVisibility(View.INVISIBLE);
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
                        if (spinTimeleftinsec == 0){
                            gspinClaimbtn.setVisibility(View.VISIBLE);
                            gspinClaimbtn.setEnabled(true);
                            spinbtn.setVisibility(View.INVISIBLE);
                            spinbtn.setEnabled(false);
                            calculatepoints(degree);
                        }else{
                            spinbtn.setVisibility(View.INVISIBLE);
                            spinbtn.setEnabled(false);
                            Toast.makeText(spinWheelActivity.this, "Please wait for"+spinTimeleftinsec+"seconds", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                });
                spinwheel.startAnimation(rotateAnimation);
            }

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
                Toast.makeText(spinWheelActivity.this, res, Toast.LENGTH_SHORT).show();
            }
        });
        //spinclaim btn
        gspinClaimbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Adloaded) {
                    rewardedVideo.showAd();
                    checkListner();
                } else {
                    loadAd();
                    Toast.makeText(spinWheelActivity.this, "Please wait while we load the Video", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkListner() {
        rewardedVideo.setVideoListener(new VideoListener() {
            @Override
            public void onVideoCompleted() {
                //update
                diamonds = Integer.parseInt(res);
                Map<String,Object> transMap = new HashMap<>();
                transMap.put("type","Golden Spin Wheel");
                transMap.put("winDiamonds",diamonds+" Diamonds");
                transMap.put("transactionNo",trnum+1);
                //updating Dbalance
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            int db = Integer.parseInt(documentSnapshot.get("Dbalance").toString());
                            int newdb = db + diamonds;
                            userRef.update("Dbalance",newdb+"");
                        }else{
                            Toast.makeText(spinWheelActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //add tras history
                transRef.add(transMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            timer();
                        }else{
                            Toast.makeText(spinWheelActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //invisible claim btn
                gspinClaimbtn.setVisibility(View.INVISIBLE);
                gspinClaimbtn.setEnabled(false);
                //make spin button visible
                spinbtn.setEnabled(false);
                spinbtn.setVisibility(View.INVISIBLE);


            }
        });
    }

    public void loadAd() {
        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                Adloaded = true;
                Toast.makeText(spinWheelActivity.this, "Video is loaded. Please watch", Toast.LENGTH_SHORT).show();
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
        //trans number
        transRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    trnum = task.getResult().size();
                }else{
                    Toast.makeText(spinWheelActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //sharde prefs
        SharedPreferences gspinPreferences = getSharedPreferences(getString(R.string.sg_remaining),MODE_PRIVATE);
        spinTimeleftinsec = gspinPreferences.getLong("Timeleft",spin_count);
        spinmEndtime = gspinPreferences.getLong("mEndtime",0);
        spinTimeleftinsec = spinmEndtime - System.currentTimeMillis()/1000;
        if (spinTimeleftinsec <= 0){
            spinTimeleftinsec = 0;
        }

        Toast.makeText(this, spinTimeleftinsec+"", Toast.LENGTH_SHORT).show();

        //timer check
        if (spinTimeleftinsec != 0){
            spinbtn.setVisibility(View.INVISIBLE);
            spinbtn.setEnabled(false);
            Toast.makeText(this, "Please wait for"+spinTimeleftinsec+"seconds", Toast.LENGTH_SHORT).show();
        }

        //rewarded Ad loading
        loadAd();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //sharedpref
        SharedPreferences gspinPreferences = getSharedPreferences(getString(R.string.sg_remaining),MODE_PRIVATE);
        SharedPreferences.Editor editor = gspinPreferences.edit();
        editor.putLong("mEndtime",spinmEndtime);
        editor.putLong("Timeleft",spinTimeleftinsec);
        editor.apply();

        Toast.makeText(this, spinmEndtime+"  "+spinTimeleftinsec+"", Toast.LENGTH_LONG).show();
    }

    private void timer() {

        spin_count = 1800000;
        mtimer = new CountDownTimer(spin_count,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                spinTimeleftinsec = millisUntilFinished/1000;
                spinmEndtime = spinTimeleftinsec + System.currentTimeMillis()/1000;
            }
            @Override
            public void onFinish() { }
        }.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StartAppAd.onBackPressed(this);
    }

}

