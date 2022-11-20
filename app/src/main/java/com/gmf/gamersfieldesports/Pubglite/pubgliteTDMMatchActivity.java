package com.gmf.gamersfieldesports.Pubglite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Objects;

public class pubgliteTDMMatchActivity extends AppCompatActivity {

    private TextView tmatchnum;
    private TextView tdatetime;
    private TextView tPrizepool;
    private TextView ttype;
    private TextView tmap;
    private TextView tperkill;
    private int tmatchs_played;
    private int tmain_balance;
    private int tmatch_fee;
    private int tplayersjoined;
    private int ttotal;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubglite_t_d_m_match);

        //Extra Strings docRef
        DocRefMatch = getIntent().getStringExtra("docRef");
        docId = getIntent().getStringExtra("docId");
        //document Referance
        joinedDocref = db.collection("PlayerDetails").document("Pubglite").collection(docId).document(Userid);
        playerDocref = db.collection("users").document(Userid);
        usergameInfo =  db.collection("users").document(Userid).collection("Gameinfo").document("usergameinfo");
        myMatchesref = db.collection("users").document(Userid).collection("Mymatches");

        tmatchnum = findViewById(R.id.v_pltmatchnum);
        tdatetime = findViewById(R.id.v_pltdate);
        tPrizepool = findViewById(R.id.v_pltprizepool);
        ttype = findViewById(R.id.v_plttype);
        tmap = findViewById(R.id.v_pltmap);
        tperkill = findViewById(R.id.v_pltkillprize);
        //buttons
        mtjoinbtn = findViewById(R.id.plt_joinbtn);
        mtjoinbtn.setEnabled(false);
        mtroomidbtn = findViewById(R.id.plt_roombtn);
        mtrulesbtn = findViewById(R.id.plt_rules);
        mtEntriesbtn = findViewById(R.id.plt_entries);
        //progress
        mtprogress = findViewById(R.id.tdmpubgliteProg);
        mtprogress.setVisibility(View.VISIBLE);

        //docref
        matchDetailsref = db.document(DocRefMatch);
        matchDetailsref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    //progressBar
                    mtprogress.setVisibility(View.GONE);
                    mtprogress.setFocusable(true);

                    DocumentSnapshot pdocs = task.getResult();
                    //string
                    mymatchNum = pdocs.get("matchnum").toString();
                    mymatchdate = pdocs.get("datetime").toString();
                    mymatchmap = pdocs.get("map").toString();
                    mymatchtype = pdocs.get("type").toString();

                    tmatchnum.setText(pdocs.get("matchnum").toString());
                    tdatetime.setText(pdocs.get("datetime").toString());
                    ttype.setText(pdocs.get("type").toString());
                    tmap.setText(pdocs.get("map").toString());
                    tPrizepool.setText(pdocs.getString("prizepool").toString());
                    tperkill.setText(pdocs.get("killprize").toString());

                    //roomid and password
                    roomid = pdocs.getString("roomId");
                    roompass = pdocs.getString("roomPass");

                    String player_string = pdocs.get("joined").toString();
                    String total_string = pdocs.get("total").toString();
                    tmatch_fee = Integer.parseInt(Objects.requireNonNull(pdocs.get("entryFee").toString()));
                    tplayersjoined = Integer.parseInt(player_string);
                    ttotal = Integer.parseInt(total_string);


                    joinedCheck();

                    mtjoinbtn.setEnabled(true);

                } else {
                    Toast.makeText(pubgliteTDMMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //join button
        mtjoinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtprogress.setVisibility(View.VISIBLE);
                if (gameusername.equals("") && gameuserId.equals("")){
                    Toast.makeText(pubgliteTDMMatchActivity.this, "Please enter your game Information", Toast.LENGTH_SHORT).show();
                    Intent gameinfointent = new Intent(pubgliteTDMMatchActivity.this, GameinfoActivity.class);
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
                Intent pubglitejwk = new Intent(pubgliteTDMMatchActivity.this, pubgliteJoinedActivity.class);
                pubglitejwk.putExtra("docId", docId);
                startActivity(pubglitejwk);
            }
        });

        //roomid and password btn
        mtroomidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtprogress.setVisibility(View.VISIBLE);
                mtroomidbtn.setEnabled(false);

                final AlertDialog.Builder roomalert = new AlertDialog.Builder(pubgliteTDMMatchActivity.this);
                View alertview = getLayoutInflater().inflate(R.layout._roomid_password, null);

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

                            } else {
                                //progressbar
                                mtprogress.setVisibility(View.GONE);
                                mtprogress.setFocusable(true);
                                Toast.makeText(pubgliteTDMMatchActivity.this, "You haven't joined the match", Toast.LENGTH_SHORT).show();
                                mtroomidbtn.setEnabled(true);
                            }
                        } else {
                            //progressbar
                            mtprogress.setVisibility(View.GONE);
                            mtprogress.setFocusable(true);
                            Toast.makeText(pubgliteTDMMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            mtroomidbtn.setEnabled(true);
                        }
                    }
                });
            }
        });

        //rules
        mtrulesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rulesIntent = new Intent(pubgliteTDMMatchActivity.this, HelpdeskActivity.class);
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
                    gameusername = documentSnapshot.get("pl_name").toString().trim();
                    gameuserId = documentSnapshot.get("pl_id").toString().trim();
                }
            }
        });
    }

    private void joinedCheck(){
        joinedDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        mtjoinbtn.setText("Joined");
                        mtjoinbtn.setEnabled(false);
                    }else{
                        if (tplayersjoined>=ttotal){
                            mtjoinbtn.setText("Contest Full");
                            mtjoinbtn.setEnabled(false);
                        }
                    }
                }else {
                    Toast.makeText(pubgliteTDMMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void joinplayer() {

        playerDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot documentSnapshot = task.getResult();
                    DocumentSnapshot snapshot = task.getResult();
                    String name = snapshot.get("name").toString();
                    String matchesPlayed = snapshot.get("MatchesPlayed").toString();
                    String balance = snapshot.get("Balance").toString();
                    tmain_balance = Integer.parseInt(balance);
                    tmatchs_played = Integer.parseInt(matchesPlayed);

                    if (tmain_balance >= tmatch_fee) {
                        tmain_balance = tmain_balance - tmatch_fee;

                        //entering name to playersDetails
                        Map<String, Object> joinMap = new HashMap<>();
                        joinMap.put("name", name);
                        joinMap.put("kills", "0");
                        joinMap.put("position", "");
                        joinMap.put("winning", "0");
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
                        Map<String, Object> mymatches = new HashMap<>();
                        mymatches.put("MatchNumber", mymatchNum);
                        mymatches.put("Date", mymatchdate);
                        mymatches.put("Map", mymatchmap);
                        mymatches.put("Type", mymatchtype);
                        mymatches.put("BType", "PLite-TDM");
                        myMatchesref.add(mymatches);

                        //button disable
                        mtjoinbtn.setEnabled(false);
                        mtjoinbtn.setText("Joined");

                        //progressbar
                        mtprogress.setVisibility(View.GONE);
                        mtprogress.setFocusable(true);
                    } else {
                        Toast.makeText(pubgliteTDMMatchActivity.this, "Your balance is low", Toast.LENGTH_SHORT).show();
                        mtprogress.setVisibility(View.GONE);
                        mtprogress.setFocusable(true);
                    }
                } else {
                    Toast.makeText(pubgliteTDMMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    mtprogress.setVisibility(View.GONE);
                    mtprogress.setFocusable(true);
                }
            }
        });

    }
}