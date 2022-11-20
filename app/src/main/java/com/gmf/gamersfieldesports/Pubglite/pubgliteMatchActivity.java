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

public class pubgliteMatchActivity extends AppCompatActivity {

    private TextView matchnum;
    private TextView datetime;
    private TextView f_prize;
    private TextView s_prize;
    private TextView t_prize;
    private TextView type;
    private TextView map;
    private TextView perkill;
    private int matchs_played;
    private int main_balance;
    private int match_fee;
    private int playersjoined;
    private int total;
    //buttons
    private Button mjoinbtn;
    private Button mroomidbtn;
    private Button mrulesbtn;
    private Button mEntriesbtn;
    //progress
    private ProgressBar mprogress;
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
        setContentView(R.layout.activity_pubglite_match);

        DocRefMatch = getIntent().getStringExtra("docRef");
        docId = getIntent().getStringExtra("docId");

        //document Referance
        joinedDocref = db.collection("PlayerDetails").document("Pubglite").collection(docId).document(Userid);
        playerDocref = db.collection("users").document(Userid);
        usergameInfo =  db.collection("users").document(Userid).collection("Gameinfo").document("usergameinfo");
        myMatchesref = db.collection("users").document(Userid).collection("Mymatches");

        //referance
        matchnum = findViewById(R.id.v_plmatchnum);
        datetime = findViewById(R.id.v_pldate);
        f_prize = findViewById(R.id.v_pl1stprize);
        s_prize = findViewById(R.id.v_pl2ndprize);
        t_prize = findViewById(R.id.v_pl3rdprize);
        type = findViewById(R.id.v_pltype);
        map = findViewById(R.id.v_plmap);
        perkill = findViewById(R.id.v_plkillprize);
        //buttons
        mjoinbtn = findViewById(R.id.pl_joinbtn);
        mjoinbtn.setEnabled(false);
        mroomidbtn = findViewById(R.id.pl_roomid);
        mrulesbtn = findViewById(R.id.pl_rules);
        mEntriesbtn = findViewById(R.id.pl_entries);
        //progress
        mprogress = findViewById(R.id.pubgliteProg);
        mprogress.setVisibility(View.VISIBLE);

        //docref
        matchDetailsref = db.document(DocRefMatch);
        matchDetailsref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot pdocs = task.getResult();
                    //string
                    mymatchNum = pdocs.get("matchnum").toString();
                    mymatchdate = pdocs.get("datetime").toString();
                    mymatchmap = pdocs.get("map").toString();
                    mymatchtype = pdocs.get("type").toString();

                    matchnum.setText(pdocs.get("matchnum").toString());
                    datetime.setText(pdocs.get("datetime").toString());
                    type.setText(pdocs.get("type").toString());
                    map.setText(pdocs.get("map").toString());
                    f_prize.setText(pdocs.get("prize1").toString());
                    s_prize.setText(pdocs.get("prize2").toString());
                    t_prize.setText(pdocs.get("prize3").toString());
                    perkill.setText(pdocs.get("killprize").toString());

                    //roomid and password
                    roomid = pdocs.getString("roomId");
                    roompass = pdocs.getString("roomPass");

                    //matchfee
                    String player_string = pdocs.get("joined").toString();
                    String total_string = pdocs.get("total").toString();
                    match_fee = Integer.parseInt(Objects.requireNonNull(pdocs.get("entryFee").toString()));
                    playersjoined = Integer.parseInt(player_string);
                    total = Integer.parseInt(total_string);

                    joinedCheck();
                    mjoinbtn.setEnabled(true);
                    mprogress.setVisibility(View.GONE);

                } else {
                    Toast.makeText(pubgliteMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    //progressBar
                    mprogress.setVisibility(View.GONE);
                    mprogress.setFocusable(true);
                }
            }
        });

        //join btn
        mjoinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogress.setVisibility(View.VISIBLE);
                if (gameusername.equals("") && gameuserId.equals("")){
                    Toast.makeText(pubgliteMatchActivity.this, "Please enter your game Information", Toast.LENGTH_SHORT).show();
                    Intent gameinfointent = new Intent(pubgliteMatchActivity.this, GameinfoActivity.class);
                    startActivity(gameinfointent);
                    finish();
                }else{
                    joinplayer();
                }
            }
        });
        //entries btn
        mEntriesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pubglitejwk = new Intent(pubgliteMatchActivity.this, pubgliteJoinedActivity.class);
                pubglitejwk.putExtra("docId",docId);
                startActivity(pubglitejwk);
            }
        });
        //roomid btn
        mroomidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mprogress.setVisibility(View.VISIBLE);
                mroomidbtn.setEnabled(false);

                final AlertDialog.Builder roomalert = new AlertDialog.Builder(pubgliteMatchActivity.this);
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
                                mprogress.setVisibility(View.GONE);
                                mroomidbtn.setEnabled(true);

                            }else{
                                //progressbar
                                mprogress.setVisibility(View.GONE);
                                mprogress.setFocusable(true);
                                Toast.makeText(pubgliteMatchActivity.this, "You haven't joined the match", Toast.LENGTH_SHORT).show();
                                mroomidbtn.setEnabled(true);
                            }
                        } else {
                            //progressbar
                            mprogress.setVisibility(View.GONE);
                            mprogress.setFocusable(true);
                            Toast.makeText(pubgliteMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            mroomidbtn.setEnabled(true);
                        }
                    }
                });
            }
        });
        mrulesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rulesIntent = new Intent(pubgliteMatchActivity.this, HelpdeskActivity.class);
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
                }else{
                    Toast.makeText(pubgliteMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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
                        mjoinbtn.setText("Joined");
                        mjoinbtn.setEnabled(false);

                    }else {
                        if (playersjoined>=total){
                            mjoinbtn.setText("Contest Full");
                            mjoinbtn.setEnabled(false);

                        }
                    }
                }else{

                    Toast.makeText(pubgliteMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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
                    main_balance = Integer.valueOf(balance);
                    matchs_played = Integer.valueOf(matchesPlayed);

                    if (main_balance >= match_fee) {
                        main_balance = main_balance - match_fee;

                        //entering name to playersDetails
                        Map<String, Object> joinMap = new HashMap<>();
                        joinMap.put("name", name);
                        joinMap.put("kills","0");
                        joinMap.put("position","");
                        joinMap.put("winning","0");
                        joinedDocref.set(joinMap);

                        //update balance and mymatches
                        Map<String, Object> Matchesjoin = new HashMap<>();
                        Matchesjoin.put("MatchesPlayed", matchs_played + 1 + "");
                        Matchesjoin.put("Balance", main_balance + "");
                        playerDocref.update(Matchesjoin);

                        //updating the playerjoined number
                        playersjoined = playersjoined + 1;
                        Map<String, Object> updatemap = new HashMap<>();
                        updatemap.put("joined", playersjoined + "");
                        matchDetailsref.update(updatemap);

                        //adding match details to mymatch of player
                        Map<String, Object> mymatches = new HashMap<>();
                        mymatches.put("MatchNumber", mymatchNum);
                        mymatches.put("Date", mymatchdate);
                        mymatches.put("Map", mymatchmap);
                        mymatches.put("Type", mymatchtype);
                        mymatches.put("BType", "PLite-BR");
                        myMatchesref.add(mymatches);

                        //button disable
                        mjoinbtn.setEnabled(false);
                        mjoinbtn.setText("Joined");

                        //progressbar
                        mprogress.setVisibility(View.GONE);
                        mprogress.setFocusable(true);
                    } else {
                        Toast.makeText(pubgliteMatchActivity.this, "Your balance is LOW, please TOPUP", Toast.LENGTH_SHORT).show();
                        mprogress.setVisibility(View.GONE);
                        mprogress.setFocusable(true);
                    }
                } else {
                    Toast.makeText(pubgliteMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    mprogress.setVisibility(View.GONE);
                    mprogress.setFocusable(true);
                }
            }
        });
    }

}