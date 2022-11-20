package com.gmf.gamersfieldesports.Pubg;

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

public class pubgTDMMatchActivity extends AppCompatActivity {

    private TextView tmatchnum;
    private TextView tdatetime;
    private TextView tPrizepool;
    private TextView ttype;
    private TextView tmap;
    private TextView tperkill;
    private TextView tpubgPlaystore;
    private int tmatchs_played;
    private int tmain_balance;
    private int tmatch_fee;
    private int tplayersjoined;
    private int ttotal;
    private WebView tTimer;
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
    private DocumentReference usergameInfo;
    private DocumentReference matchDetailsref;
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
    private String mytTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubg_t_d_m_match);

        //Extra Strings docRef
        DocRefMatch = getIntent().getStringExtra("docRef");
        docId = getIntent().getStringExtra("docId");

        //document Referance
        joinedDocref = db.collection("PlayerDetails").document("Pubg").collection(docId).document(Userid);
        playerDocref = db.collection("users").document(Userid);
        usergameInfo =  db.collection("users").document(Userid).collection("Gameinfo").document("usergameinfo");
        myMatchesref = db.collection("users").document(Userid).collection("Mymatches");

        tmatchnum =  findViewById(R.id.v_ptmatchnum);
        tdatetime = findViewById(R.id.v_ptdate);
        tPrizepool = findViewById(R.id.v_ptprizepool);
        ttype = findViewById(R.id.v_pttype);
        tmap = findViewById(R.id.v_ptmap);
        tperkill = findViewById(R.id.v_ptkillprize);
        tpubgPlaystore = findViewById(R.id.pubgtplaystore);
        tTimer = findViewById(R.id.ptTimer);
        //buttons
        mtjoinbtn = findViewById(R.id.pt_joinbtn);
        mtjoinbtn.setEnabled(false);
        mtroomidbtn = findViewById(R.id.pt_roombtn);
        mtrulesbtn = findViewById(R.id.pt_rules);
        mtEntriesbtn = findViewById(R.id.pt_entries);
        //progress
        mtprogress = findViewById(R.id.tdmpubgProg);
        mtprogress.setVisibility(View.VISIBLE);
        //timer fields
        mStartLayout = findViewById(R.id.ptstartlayout);

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
                    mytTimer = pdocs.get("cTimer").toString();

                    tmatchnum.setText(pdocs.get("matchnum").toString());
                    tdatetime.setText(pdocs.get("datetime").toString());
                    ttype.setText(pdocs.get("type").toString());
                    tmap.setText(pdocs.get("map").toString());
                    tPrizepool.setText(pdocs.getString("prizepool").toString());
                    tperkill.setText(pdocs.get("killprize").toString());

                    //timer
                    tTimer.loadData(mytTimer,"text/html","UTF-8");
                    tTimer.requestFocus();
                    tTimer.setBackgroundColor(Color.parseColor("#CECDCB"));
                    tTimer.getSettings().setJavaScriptEnabled(true);
                    tTimer.getSettings().setGeolocationEnabled(true);

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
                    Toast.makeText(pubgTDMMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //join button
        mtjoinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtprogress.setVisibility(View.VISIBLE);
                if (gameusername.equals("") && gameuserId.equals("")){
                    Toast.makeText(pubgTDMMatchActivity.this, "Please enter your game Information", Toast.LENGTH_SHORT).show();
                    Intent gameinfointent = new Intent(pubgTDMMatchActivity.this, GameinfoActivity.class);
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
                Intent pubgjwk = new Intent(pubgTDMMatchActivity.this, pubgJoinedActivity.class);
                pubgjwk.putExtra("docId",docId);
                startActivity(pubgjwk);
            }
        });
        //roomid and password btn
        mtroomidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtprogress.setVisibility(View.VISIBLE);
                mtroomidbtn.setEnabled(false);

                final AlertDialog.Builder roomalert = new AlertDialog.Builder(pubgTDMMatchActivity.this);
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
                                Toast.makeText(pubgTDMMatchActivity.this, "You haven't joined the match", Toast.LENGTH_SHORT).show();
                                mtroomidbtn.setEnabled(true);
                            }
                        } else {
                            //progressbar
                            mtprogress.setVisibility(View.GONE);
                            mtprogress.setFocusable(true);
                            Toast.makeText(pubgTDMMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            mtroomidbtn.setEnabled(true);
                        }
                    }
                });
            }
        });
        //playstore
        tpubgPlaystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent playstoreIntent = new Intent(Intent.ACTION_VIEW);
                playstoreIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.tencent.ig&hl=en_IN&gl=US"));
                startActivity(playstoreIntent);

                 */
            }
        });
        //rules btn
        mtrulesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rulesIntent = new Intent(pubgTDMMatchActivity.this, HelpdeskActivity.class);
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
                    gameusername = documentSnapshot.get("p_name").toString().trim();
                    gameuserId = documentSnapshot.get("p_id").toString().trim();
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
                    Toast.makeText(pubgTDMMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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
                        mymatches.put("BType","PUBG-TDM");
                        myMatchesref.add(mymatches);

                        //button disable
                        mtjoinbtn.setEnabled(false);
                        mtjoinbtn.setText("Joined");

                        //progressbar
                        mtprogress.setVisibility(View.GONE);
                        mtprogress.setFocusable(true);
                    }else{
                        Toast.makeText(pubgTDMMatchActivity.this, "Your balance is LOW, please watch videos to get coins", Toast.LENGTH_SHORT).show();
                        Intent walletintent = new Intent(pubgTDMMatchActivity.this, WalletActivity.class);
                        startActivity(walletintent);
                        mtprogress.setVisibility(View.GONE);
                        mtprogress.setFocusable(true);
                    }
                }else{
                    Toast.makeText(pubgTDMMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    mtprogress.setVisibility(View.GONE);
                    mtprogress.setFocusable(true);
                }
            }
        });
    }
}