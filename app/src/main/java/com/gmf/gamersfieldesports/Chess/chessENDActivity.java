package com.gmf.gamersfieldesports.Chess;

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

public class chessENDActivity extends AppCompatActivity {

    //textview
    private TextView ematchnum;
    private TextView edatetime;
    private TextView etype;
    private TextView eprizepool;
    //buttons
    private Button mwatchbtn;
    private Button mwinnersbtn;
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
        setContentView(R.layout.activity_chess_e_n_d);

        //Extra Strings docRef
        DocRefMatch = getIntent().getStringExtra("docRef");
        docId = getIntent().getStringExtra("docId");

        //referance
        ematchnum = findViewById(R.id.v_chematchnum);
        edatetime = findViewById(R.id.v_chedate);
        etype = findViewById(R.id.v_chetype);
        eprizepool = findViewById(R.id.v_cheprizepool);
        mwatchbtn = findViewById(R.id.che_watchbtn);
        mwinnersbtn = findViewById(R.id.che_winners);

        //progress
        mprogress = findViewById(R.id.endchessProg);
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
                    eprizepool.setText(pdocs.get("prizepool").toString());
                }else{
                    Toast.makeText(chessENDActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mwinnersbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent winner = new Intent(chessENDActivity.this, chessK_WActivity.class);
                winner.putExtra("docId",docId);
                startActivity(winner);
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