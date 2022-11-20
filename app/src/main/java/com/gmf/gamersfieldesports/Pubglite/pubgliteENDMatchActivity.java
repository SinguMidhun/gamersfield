package com.gmf.gamersfieldesports.Pubglite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gmf.gamersfieldesports.R;

public class pubgliteENDMatchActivity extends AppCompatActivity {

    //textview
    private TextView ematchnum;
    private TextView edatetime;
    private TextView ef_prize;
    private TextView es_prize;
    private TextView et_prize;
    private TextView etype;
    private TextView emap;
    private TextView eperkill;
    //buttons
    private Button mwatchbtn;
    private Button mwinnersbtn;
    private Button mkillerbtn;
    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference matchDetailsref;
    //progress
    private ProgressBar mprogress;
    //extra intent strings
    private String DocRefMatch;
    private String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubglite_e_n_d_match);

        //Extra Strings docRef
        DocRefMatch = getIntent().getStringExtra("docRef");
        docId = getIntent().getStringExtra("docId");

        //referance
        ematchnum = findViewById(R.id.v_plematchnum);
        edatetime = findViewById(R.id.v_pledate);
        ef_prize = findViewById(R.id.v_ple1stprize);
        es_prize = findViewById(R.id.v_ple2ndprize);
        et_prize = findViewById(R.id.v_ple3rdprize);
        etype = findViewById(R.id.v_pletype);
        emap = findViewById(R.id.v_plemap);
        eperkill = findViewById(R.id.v_plekillprize);
        mwatchbtn = findViewById(R.id.ple_watchbtn);
        mwinnersbtn = findViewById(R.id.ple_winners);
        mkillerbtn = findViewById(R.id.ple_killers);

        //progress
        mprogress = findViewById(R.id.endpubgliteProg);
        mprogress.setVisibility(View.VISIBLE);

        //docref
        matchDetailsref = db.document(DocRefMatch);
        matchDetailsref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    //progressBar
                    mprogress.setVisibility(View.GONE);
                    mprogress.setFocusable(true);

                    DocumentSnapshot pdocs = task.getResult();

                    ematchnum.setText(pdocs.get("matchnum").toString());
                    edatetime.setText(pdocs.get("datetime").toString());
                    etype.setText(pdocs.get("type").toString());
                    emap.setText(pdocs.get("map").toString());
                    ef_prize.setText(pdocs.get("prize1").toString());
                    es_prize.setText(pdocs.get("prize2").toString());
                    et_prize.setText(pdocs.get("prize3").toString());
                    eperkill.setText(pdocs.get("killprize").toString());
                }else{
                    Toast.makeText(pubgliteENDMatchActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mwinnersbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent freefirewinner = new Intent(pubgliteENDMatchActivity.this, pubgliteK_WActivity.class);
                freefirewinner.putExtra("docId",docId);
                startActivity(freefirewinner);
            }
        });
        mkillerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mkillerbtn = new Intent(pubgliteENDMatchActivity.this, pubglitekillerActivity.class);
                mkillerbtn.putExtra("docId",docId);
                startActivity(mkillerbtn);
            }
        });
        mwatchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW);
                youtubeIntent.setData(Uri.parse("https://youtube.com/channel/UCHq0-Iy522b8SYIP1JWn4bQ"));
                startActivity(youtubeIntent);
            }
        });
    }
}