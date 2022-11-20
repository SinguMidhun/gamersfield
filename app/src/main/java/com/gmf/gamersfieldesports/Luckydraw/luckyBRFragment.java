package com.gmf.gamersfieldesports.Luckydraw;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.gmf.gamersfieldesports.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class luckyBRFragment extends Fragment {

    private CharSequence date;
    private TextView contestDate;
    private Button joinluckycontest;
    private ProgressBar luckyprog;

    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser mcurrentuser = FirebaseAuth.getInstance().getCurrentUser();
    private String userId = mcurrentuser.getUid();
    private DocumentReference luckyDateRef;
    private DocumentReference userDoc;

    //extras
    private String username;

    //rewarded Ads
    StartAppAd rewardedVideo = new StartAppAd(getContext());
    private boolean Adloaded = false;

    public luckyBRFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_lucky_b_r, container, false);
        Context context = v.getContext().getApplicationContext();
        //date
        Date d = new Date();
        date  = DateFormat.format("MMMM d, yyyy ", d.getTime());
        //referance
        contestDate = v.findViewById(R.id.luckydate);
        contestDate.setText(date);
        joinluckycontest = v.findViewById(R.id.joinluckydraw);
        luckyprog = v.findViewById(R.id.luckyprog);
        //firebase
        userDoc = db.collection("users").document(userId);
        luckyDateRef = db.collection("PlayerDetails").document("Luckydraw").collection(date.toString()).document(userId);

        StartAppSDK.init(context, getString(R.string.startAppId), true);

        //join btn
        joinluckycontest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartAppAd.showAd(getContext());
                checkListner();
            }
        });

        return v;
    }

    private void checkListner() {
        Map<String,Object> luckymap = new HashMap<>();
        luckymap.put("Winner","0");
        luckymap.put("name",username);
        luckyDateRef.set(luckymap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Joined the Contest", Toast.LENGTH_SHORT).show();
                    joinluckycontest.setEnabled(false);
                    joinluckycontest.setText("Already Joined");
                }else{
                    Toast.makeText(getContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loadAd() {
        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                Adloaded = true;
                Toast.makeText(getContext(), "Video is loaded. Please watch", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                // Can't show rewarded video
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        luckyprog.setVisibility(View.VISIBLE);
        userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    username = documentSnapshot.get("name").toString();
                    luckyprog.setVisibility(View.GONE);
                }
            }
        });
        luckyDateRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        joinluckycontest.setEnabled(false);
                        joinluckycontest.setText("Already Joined");
                    }
                }else{
                    Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                Adloaded = true;
                Toast.makeText(getContext(), "Video is loaded. Please watch", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                // Can't show rewarded video
            }
        });
    }

}