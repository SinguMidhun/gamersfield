package com.gmf.gamersfieldesports.Freefire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmf.gamersfieldesports.Userdetails.WalletActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gmf.gamersfieldesports.Playerprofile.GameinfoActivity;
import com.gmf.gamersfieldesports.Playerprofile.HelpdeskActivity;
import com.gmf.gamersfieldesports.R;

import java.util.HashMap;
import java.util.Map;

public class freefireTDMMatchActivity extends AppCompatActivity {

    private TextView tmatchnum;
    private TextView tdatetime;
    private TextView tPrizepool;
    private TextView ttype;
    private TextView tmap;
    private TextView tperkill;
    private TextView freefiretplaystore;
    private int tmatchs_played;
    private int tmain_balance;
    private int tmatch_fee;
    private int tplayersjoined;
    private int ttotal;
    private WebView fftTimer;
    //buttons
    private Button mtjoinbtn;
    private Button mtroomidbtn;
    private Button mtrulesbtn;
    private Button mtEntriesbtn;
    //progress
    private ProgressBar mtprogress;
    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String Userid = mAuth.getCurrentUser().getUid();
    private DocumentReference playerDocref;
    private DocumentReference joinedDocref;
    private DocumentReference matchDetailsref;
    private DocumentReference usergameInfo;
    private CollectionReference myMatchesref;

    //mymatch Values
    private String mymatchNum;
    private String mymatchdate;
    private String mymatchmap;
    private String mymatchtype;
    //extra intent strings
    private String DocRefMatch;
    private String docId;
    //roomid password
    private String roomid;
    private String roompass;
    //user game info
    private String gameusername;
    private String gameuserId;
    //timer field
    private RelativeLayout mStartLayout;
    private String roomCredits;
    private String myTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freefire_t_d_m_match);

        //Extra Strings docRef
        DocRefMatch = getIntent().getStringExtra("docRef");
        docId = getIntent().getStringExtra("docId");

        //document Referance
        joinedDocref = db.collection("PlayerDetails").document("Freefire").collection(docId).document(Userid);
        playerDocref = db.collection("users").document(Userid);
        usergameInfo =  db.collection("users").document(Userid).collection("Gameinfo").document("usergameinfo");
        myMatchesref = db.collection("users").document(Userid).collection("Mymatches");

        tmatchnum =  findViewById(R.id.v_ftmatchnum);
        tdatetime = findViewById(R.id.v_ftdate);
        tPrizepool = findViewById(R.id.v_ftprizepool);
        ttype = findViewById(R.id.v_fttype);
        tmap = findViewById(R.id.v_ftmap);
        freefiretplaystore = findViewById(R.id.freefiretplaystore);
        tperkill = findViewById(R.id.v_ftkillprize);
        fftTimer = findViewById(R.id.fftTimer);
        //buttons
        mtjoinbtn = findViewById(R.id.ft_joinbtn);
        mtjoinbtn.setEnabled(false);
        mtroomidbtn = findViewById(R.id.ft_roomidbtn);
        mtrulesbtn = findViewById(R.id.ft_rulesbtn);
        mtEntriesbtn = findViewById(R.id.ft_entriesbtn);
        //progress
        mtprogress = findViewById(R.id.tdmfreefireProg);
        mtprogress.setVisibility(View.VISIBLE);
        //timer fields
        mStartLayout = findViewById(R.id.ftstartlayout);

        //docref
        matchDetailsref = db.document(DocRefMatch);
        matchDetailsref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    //progressBar
                    mtprogress.setVisibility(View.GONE);
                    mtprogress.setFocusable(true);

                    DocumentSnapshot pdocs = task.getResult();
                    //string
                    mymatchNum = pdocs.get("matchnum").toString();
                    mymatchdate = pdocs.get("datetime").toString();
                    mymatchmap = pdocs.get("map").toString();
                    mymatchtype = pdocs.get("type").toString();
                    myTimer = pdocs.get("cTimer").toString();

                    //timer
                    fftTimer.loadData(myTimer,"text/html","UTF-8");
                    fftTimer.requestFocus();
                    fftTimer.setBackgroundColor(Color.parseColor("#CECDCB"));
                    fftTimer.getSettings().setJavaScriptEnabled(true);
                    fftTimer.getSettings().setGeolocationEnabled(true);

                    tmatchnum.setText(pdocs.get("matchnum").toString());
                    tdatetime.setText(pdocs.get("datetime").toString());
                    ttype.setText(pdocs.get("type").toString());
                    tmap.setText(pdocs.get("map").toString());
                    tPrizepool.setText(pdocs.getString("prizepool").toString());
                    tperkill.setText(pdocs.get("killprize").toString());

                    roomCredits = pdocs.get("roomCredits").toString();
                    if (roomCredits.equals("1")){
                        mStartLayout.setVisibility(View.VISIBLE);
                    }else{
                        mStartLayout.setVisibility(View.INVISIBLE);
                    }

                    //roomid and password
                    roomid = pdocs.getString("roomId");
                    roompass = pdocs.getString("roomPass");

                    //matchfee
                    String player_string = pdocs.get("joined").toString();
                    String total_string = pdocs.get("total").toString();
                    tmatch_fee = Integer.parseInt(pdocs.get("entryFee").toString());
                    tplayersjoined = Integer.parseInt(player_string);
                    ttotal = Integer.parseInt(total_string);

                    //entry fee on button
                    if (tmatch_fee==0){
                        String buttonString ="Join for Free";
                        mtjoinbtn.setText(buttonString);
                    }else{
                        String buttonString ="Join with "+tmatch_fee+"-coins";
                        mtjoinbtn.setText(buttonString);
                    }

                    joinedCheck();

                    mtjoinbtn.setEnabled(true);

                }else{
                    Toast.makeText(freefireTDMMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        //join button
        mtjoinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtprogress.setVisibility(View.VISIBLE);
                if (gameusername.equals("") && gameuserId.equals("")){
                    Toast.makeText(freefireTDMMatchActivity.this, "Please enter your game Information", Toast.LENGTH_SHORT).show();
                    Intent gameinfointent = new Intent(freefireTDMMatchActivity.this, GameinfoActivity.class);
                    startActivity(gameinfointent);
                    finish();
                }else{
                    joinplayer();
                }
            }
        });

        //entries btn
        mtEntriesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent freefirejwk = new Intent(freefireTDMMatchActivity.this, freefireJoinedActivity.class);
                freefirejwk.putExtra("docId",docId);
                startActivity(freefirejwk);
            }
        });
        //roomid and password btn
        mtroomidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtprogress.setVisibility(View.VISIBLE);
                mtroomidbtn.setEnabled(false);

                final AlertDialog.Builder roomalert = new AlertDialog.Builder(freefireTDMMatchActivity.this);
                View alertview = getLayoutInflater().inflate(R.layout._roomid_password,null);

                TextView v_roomid = alertview.findViewById(R.id.v_roomid);
                TextView v_roompass = alertview.findViewById(R.id.v_roompass);
                roomalert.setView(alertview);

                final AlertDialog alert = roomalert.create();

                joinedDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {

                                v_roomid.setText(roomid);
                                v_roompass.setText(roompass);
                                alert.show();
                                mtprogress.setVisibility(View.GONE);
                                mtroomidbtn.setEnabled(true);

                            }else{
                                //progressbar
                                mtprogress.setVisibility(View.GONE);
                                mtprogress.setFocusable(true);
                                Toast.makeText(freefireTDMMatchActivity.this, "You haven't joined the match", Toast.LENGTH_SHORT).show();
                                mtroomidbtn.setEnabled(true);
                            }
                        } else {
                            //progressbar
                            mtprogress.setVisibility(View.GONE);
                            mtprogress.setFocusable(true);
                            Toast.makeText(freefireTDMMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            mtroomidbtn.setEnabled(true);
                        }
                    }
                });
            }
        });
        //freefireIntent
        freefiretplaystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent playstoreIntent = new Intent(Intent.ACTION_VIEW);
                playstoreIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.dts.freefireth"));
                startActivity(playstoreIntent);

                 */
            }
        });

        //rules
        mtrulesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rulesIntent = new Intent(freefireTDMMatchActivity.this, HelpdeskActivity.class);
                startActivity(rulesIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        usergameInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    gameusername = documentSnapshot.get("ff_name").toString().trim();
                    gameuserId = documentSnapshot.get("ff_id").toString().trim();
                }else{
                    Toast.makeText(freefireTDMMatchActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void joinedCheck(){
        joinedDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        mtjoinbtn.setText("Joined");
                        mtjoinbtn.setEnabled(false);
                    }else{
                        if (tplayersjoined >= ttotal) {
                            mtjoinbtn.setText("Contest full");
                            mtjoinbtn.setEnabled(false);
                        }
                    }
                } else {
                    Toast.makeText(freefireTDMMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void joinplayer() {
        playerDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    DocumentSnapshot documentSnapshot = task.getResult();
                    DocumentSnapshot snapshot = task.getResult();
                    String name = snapshot.get("name").toString();
                    String matchesPlayed = snapshot.get("MatchesPlayed").toString();
                    String balance = snapshot.get("Balance").toString();
                    tmain_balance = Integer.valueOf(balance);
                    tmatchs_played = Integer.valueOf(matchesPlayed);

                    if (tmain_balance >= tmatch_fee) {
                        tmain_balance = tmain_balance - tmatch_fee;

                        //entering name to playersDetails
                        Map<String, Object> joinMap = new HashMap<>();
                        joinMap.put("name", name);
                        joinMap.put("kills","0");
                        joinMap.put("position","");
                        joinMap.put("IGN",gameusername);
                        joinMap.put("gameid",gameuserId);
                        joinMap.put("winning","0");
                        joinedDocref.set(joinMap);

                        //update balance and mymatches
                        Map<String, Object> Matchesjoin = new HashMap<>();
                        Matchesjoin.put("MatchesPlayed", tmatchs_played + 1 + "");
                        Matchesjoin.put("Balance", tmain_balance + "");
                        playerDocref.update(Matchesjoin);

                        //updating the playerjoined number
                        tplayersjoined = tplayersjoined + 1;
                        Map<String, Object> updatemap = new HashMap<>();
                        updatemap.put("joined", tplayersjoined + "");
                        matchDetailsref.update(updatemap);

                        //adding match details to mymatch of player
                        Map<String,Object>mymatches = new HashMap<>();
                        mymatches.put("MatchNumber",mymatchNum);
                        mymatches.put("Date",mymatchdate);
                        mymatches.put("Map",mymatchmap);
                        mymatches.put("Type",mymatchtype);
                        mymatches.put("BType","Freefire-TDM");
                        myMatchesref.add(mymatches);

                        //button disable
                        mtjoinbtn.setEnabled(false);
                        mtjoinbtn.setText("Joined");

                        //progressbar
                        mtprogress.setVisibility(View.GONE);
                        mtprogress.setFocusable(true);
                    }else{
                        Toast.makeText(freefireTDMMatchActivity.this, "Your balance is LOW, please watch videos to get coins", Toast.LENGTH_SHORT).show();
                        Intent walletintent = new Intent(freefireTDMMatchActivity.this, WalletActivity.class);
                        startActivity(walletintent);
                        mtprogress.setVisibility(View.GONE);
                        mtprogress.setFocusable(true);
                    }
                }else{
                    Toast.makeText(freefireTDMMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    mtprogress.setVisibility(View.GONE);
                    mtprogress.setFocusable(true);
                }
            }
        });
    }
}