package com.gmf.gamersfieldesports.Minimilitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

public class miniMatchActivity extends AppCompatActivity {


    private TextView matchnum;
    private TextView datetime;
    private TextView f_prize;
    private TextView s_prize;
    private TextView t_prize;
    private TextView type;
    private TextView map;
    private TextView perkill;
    private TextView miniplaystore;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_match);

        DocRefMatch = getIntent().getStringExtra("docRef");
        docId = getIntent().getStringExtra("docId");

        //document Referance
        joinedDocref = db.collection("PlayerDetails").document("Minimilitia").collection(docId).document(Userid);
        playerDocref = db.collection("users").document(Userid);
        usergameInfo =  db.collection("users").document(Userid).collection("Gameinfo").document("usergameinfo");
        myMatchesref = db.collection("users").document(Userid).collection("Mymatches");

        //referance
        matchnum = findViewById(R.id.v_mmatchnum);
        datetime = findViewById(R.id.v_mdate);
        f_prize = findViewById(R.id.v_m1stprize);
        s_prize = findViewById(R.id.v_m2ndprize);
        t_prize = findViewById(R.id.v_m3rdprize);
        type = findViewById(R.id.v_mtype);
        map = findViewById(R.id.v_mmap);
        perkill = findViewById(R.id.v_mkillprize);
        //buttons
        mjoinbtn = findViewById(R.id.m_joinbtn);
        mjoinbtn.setEnabled(false);
        mroomidbtn = findViewById(R.id.m_roomid);
        mrulesbtn = findViewById(R.id.m_rules);
        mEntriesbtn = findViewById(R.id.m_entries);
        miniplaystore = findViewById(R.id.miniplaystore);
        //progress
        mprogress = findViewById(R.id.miniProg);
        mprogress.setVisibility(View.VISIBLE);
        //timer fields
        mStartLayout = findViewById(R.id.mstartlayout);

        //docref
        matchDetailsref = db.document(DocRefMatch);
        matchDetailsref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    //progressBar
                    mprogress.setVisibility(View.GONE);
                    mprogress.setFocusable(true);

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
                    match_fee = Integer.parseInt(pdocs.get("entryFee").toString());
                    playersjoined = Integer.parseInt(player_string);
                    total = Integer.parseInt(total_string);

                    joinedCheck();

                    //progressBar
                    mprogress.setVisibility(View.GONE);
                    mprogress.setFocusable(true);

                    mjoinbtn.setEnabled(true);

                } else {
                    Toast.makeText(miniMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(miniMatchActivity.this, "Please enter your game Information", Toast.LENGTH_SHORT).show();
                    Intent gameinfointent = new Intent(miniMatchActivity.this, GameinfoActivity.class);
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
                Intent minijwk = new Intent(miniMatchActivity.this, miniJoinedActivity.class);
                minijwk.putExtra("docId",docId);
                startActivity(minijwk);
            }
        });
        //roomid btn
        mroomidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mprogress.setVisibility(View.VISIBLE);
                mroomidbtn.setEnabled(false);

                final AlertDialog.Builder roomalert = new AlertDialog.Builder(miniMatchActivity.this);
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
                                Toast.makeText(miniMatchActivity.this, "You haven't joined the match", Toast.LENGTH_SHORT).show();
                                mroomidbtn.setEnabled(true);
                            }
                        } else {
                            //progressbar
                            mprogress.setVisibility(View.GONE);
                            mprogress.setFocusable(true);
                            Toast.makeText(miniMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            mroomidbtn.setEnabled(true);
                        }
                    }
                });
            }
        });
        //mini playstore intent
        miniplaystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent playstore = new Intent(Intent.ACTION_VIEW);
                playstore.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.appsomniacs.da2"));
                startActivity(playstore);
                 */
            }
        });

        mrulesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rulesIntent = new Intent(miniMatchActivity.this, HelpdeskActivity.class);
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
                    gameusername = documentSnapshot.get("mm_name").toString().trim();
                    gameuserId = documentSnapshot.get("mm_id").toString().trim();
                }else{
                    Toast.makeText(miniMatchActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
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
                        mjoinbtn.setText("Joined");
                        mjoinbtn.setEnabled(false);
                    }else{
                        if (playersjoined >= total) {
                            mjoinbtn.setText("Contest full");
                            mjoinbtn.setEnabled(false);
                        }
                    }
                } else {
                    Toast.makeText(miniMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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
                        mymatches.put("BType", "Mini Militia");
                        myMatchesref.add(mymatches);

                        //button disable
                        mjoinbtn.setEnabled(false);
                        mjoinbtn.setText("Joined");

                        //progressbar
                        mprogress.setVisibility(View.GONE);
                        mprogress.setFocusable(true);
                    } else {
                        Toast.makeText(miniMatchActivity.this, "Your balance is LOW, please TOPUP", Toast.LENGTH_SHORT).show();
                        mprogress.setVisibility(View.GONE);
                        mprogress.setFocusable(true);
                    }
                } else {
                    Toast.makeText(miniMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    mprogress.setVisibility(View.GONE);
                    mprogress.setFocusable(true);
                }
            }
        });

    }
}