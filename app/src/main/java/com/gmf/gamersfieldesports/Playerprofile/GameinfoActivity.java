package com.gmf.gamersfieldesports.Playerprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gmf.gamersfieldesports.R;

import java.util.HashMap;
import java.util.Map;

public class GameinfoActivity extends AppCompatActivity {

    private EditText gameinfo_pubgname;
    private EditText gameinfo_pubgid;
    private EditText gameinfo_plname;
    private EditText gameinfo_plid;
    private EditText gameinfo_ffname;
    private EditText gameinfo_ffid;
    private EditText gameinfo_cname;
    private EditText gameinfo_cid;
    private EditText gameinfo_mmname;
    private EditText gameinfo_mmid;
    private EditText gameinfo_lname;
    private EditText gameinfo_sname;
    private EditText gameinfo_chessname;
    //green Tic
    private ImageView p_gTic;
    private ImageView pl_gTic;
    private ImageView f_gTic;
    private ImageView c_gTic;
    private ImageView mm_gTic;
    private ImageView s_gTic;
    private ImageView l_gTic;
    private ImageView chess_gTic;
    //button
    private Button g_pubg;
    private Button g_plite;
    private Button g_freefire;
    private Button g_cod;
    private Button g_mini;
    private Button g_ludo;
    private Button g_snake;
    private Button g_chess;
    //string
    private String p_nameStr;
    private String p_idStr;
    private String pl_nameStr;
    private String pl_idStr;
    private String ff_nameStr;
    private String ff_idStr;
    private String c_nameStr;
    private String c_idStr;
    private String mm_nameStr;
    private String mm_idStr;
    private String l_nameStr;
    private String s_nameStr;
    private String chess_nameStr;

    //firebase
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mcurrentuser = mAuth.getCurrentUser();
    private String userId = mcurrentuser.getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference ginfo_ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameinfo);

        //referance
        gameinfo_pubgname = findViewById(R.id.ginfo_pusername);
        gameinfo_pubgid = findViewById(R.id.ginfo_puserId);
        gameinfo_plname = findViewById(R.id.ginfo_plusername);
        gameinfo_plid = findViewById(R.id.ginfo_pluserId);
        gameinfo_ffname = findViewById(R.id.ginfo_fusername);
        gameinfo_ffid = findViewById(R.id.ginfo_fuserId);
        gameinfo_cname = findViewById(R.id.ginfo_cusername);
        gameinfo_cid = findViewById(R.id.ginfo_cuserId);
        gameinfo_mmname = findViewById(R.id.ginfo_mmusername);
        gameinfo_mmid = findViewById(R.id.ginfo_mmuserId);
        gameinfo_lname = findViewById(R.id.ginfo_lname);
        gameinfo_sname = findViewById(R.id.ginfo_sname);
        gameinfo_chessname = findViewById(R.id.ginfo_chessname);
        g_pubg = findViewById(R.id.ginfo_psave);
        g_plite = findViewById(R.id.ginfo_plsave);
        g_freefire = findViewById(R.id.ginfo_fsave);
        g_cod = findViewById(R.id.ginfo_csave);
        g_mini = findViewById(R.id.ginfo_mmsave);
        g_ludo = findViewById(R.id.ginfo_lsave);
        g_snake = findViewById(R.id.ginfo_ssave);
        g_chess = findViewById(R.id.ginfo_chesssave);
        //image Tic
        p_gTic = findViewById(R.id.p_gtic);
        pl_gTic = findViewById(R.id.pl_gTic);
        f_gTic = findViewById(R.id.f_gTic);
        c_gTic = findViewById(R.id.c_gTic);
        mm_gTic = findViewById(R.id.mm_gTic);
        l_gTic = findViewById(R.id.l_gTic);
        s_gTic = findViewById(R.id.s_gTic);
        chess_gTic = findViewById(R.id.chess_gTic);
        //referance
        ginfo_ref = db.collection("users").document(userId).collection("Gameinfo").document("usergameinfo");
        ginfo_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    p_nameStr = documentSnapshot.get("p_name").toString().trim();
                    p_idStr = documentSnapshot.get("p_id").toString().trim();
                    pl_nameStr = documentSnapshot.get("pl_name").toString().trim();
                    pl_idStr = documentSnapshot.get("pl_id").toString().trim();
                    ff_nameStr = documentSnapshot.get("ff_name").toString().trim();
                    ff_idStr = documentSnapshot.get("ff_id").toString().trim();
                    c_idStr = documentSnapshot.get("c_id").toString().trim();
                    c_nameStr = documentSnapshot.get("c_name").toString().trim();
                    mm_nameStr = documentSnapshot.get("mm_name").toString();
                    mm_idStr = documentSnapshot.get("mm_id").toString();
                    l_nameStr = documentSnapshot.get("l_name").toString();
                    s_nameStr = documentSnapshot.get("s_name").toString();
                    chess_nameStr = documentSnapshot.get("chess_name").toString();

                    if (!p_nameStr.isEmpty() && !p_idStr.isEmpty()){
                        g_pubg.setEnabled(false);
                        g_pubg.setText("SAVED");
                        gameinfo_pubgname.setText(p_nameStr);
                        gameinfo_pubgid.setText(p_idStr);
                        gameinfo_pubgname.setEnabled(false);
                        gameinfo_pubgid.setEnabled(false);
                        p_gTic.setVisibility(View.VISIBLE);
                    }if (!pl_nameStr.isEmpty() && !pl_idStr.isEmpty()){
                        g_plite.setEnabled(false);
                        g_plite.setText("SAVED");
                        gameinfo_plname.setText(pl_nameStr);
                        gameinfo_plid.setText(pl_idStr);
                        gameinfo_plname.setEnabled(false);
                        gameinfo_plid.setEnabled(false);
                        pl_gTic.setVisibility(View.VISIBLE);
                    }if (!ff_nameStr.isEmpty() && !ff_idStr.isEmpty()){
                        g_freefire.setEnabled(false);
                        g_freefire.setText("SAVED");
                        gameinfo_ffname.setText(ff_nameStr);
                        gameinfo_ffid.setText(ff_idStr);
                        gameinfo_ffname.setEnabled(false);
                        gameinfo_ffid.setEnabled(false);
                        f_gTic.setVisibility(View.VISIBLE);
                    }if (!c_nameStr.isEmpty() && !c_idStr.isEmpty()){
                        g_cod.setEnabled(false);
                        g_cod.setText("SAVED");
                        gameinfo_cid.setText(c_idStr);
                        gameinfo_cname.setText(c_nameStr);
                        gameinfo_cid.setEnabled(false);
                        gameinfo_cname.setEnabled(false);
                        c_gTic.setVisibility(View.VISIBLE);
                    }if (!mm_nameStr.isEmpty() && !mm_idStr.isEmpty()){
                        g_mini.setEnabled(false);
                        g_mini.setText("SAVED");
                        gameinfo_mmid.setText(mm_idStr);
                        gameinfo_mmname.setText(mm_nameStr);
                        gameinfo_mmid.setEnabled(false);
                        gameinfo_mmname.setEnabled(false);
                        mm_gTic.setVisibility(View.VISIBLE);
                    }if (!l_nameStr.isEmpty()){
                        g_ludo.setEnabled(false);
                        g_ludo.setText("SAVED");
                        gameinfo_lname.setText(l_nameStr);
                        gameinfo_lname.setEnabled(false);
                        l_gTic.setVisibility(View.VISIBLE);
                    }if (!s_nameStr.isEmpty()){
                        g_snake.setEnabled(false);
                        g_snake.setText("SAVED");
                        gameinfo_sname.setText(s_nameStr);
                        gameinfo_sname.setEnabled(false);
                        s_gTic.setVisibility(View.VISIBLE);
                    }if (!chess_nameStr.isEmpty()){
                        g_chess.setEnabled(false);
                        g_chess.setText("SAVED");
                        gameinfo_chessname.setText(chess_nameStr);
                        gameinfo_chessname.setEnabled(false);
                        chess_gTic.setVisibility(View.VISIBLE);
                    }
                }else {
                    Toast.makeText(GameinfoActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        g_pubg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p_n = gameinfo_pubgname.getText().toString().trim();
                String p_i = gameinfo_pubgid.getText().toString().trim();
                if (p_i.isEmpty() || p_n.isEmpty()){
                    Toast.makeText(GameinfoActivity.this, "Enter Both Fields", Toast.LENGTH_SHORT).show();
                }else{
                    Map<String , Object> infoMap = new HashMap<>();
                    infoMap.put("p_name",p_n);
                    infoMap.put("p_id",p_i);
                    ginfo_ref.update(infoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(GameinfoActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                g_pubg.setEnabled(false);
                                g_pubg.setText("SAVED");
                                p_gTic.setVisibility(View.VISIBLE);
                            }else{
                                Toast.makeText(GameinfoActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        g_plite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pl_n = gameinfo_plname.getText().toString().trim();
                String pl_i = gameinfo_plid.getText().toString().trim();
                if (pl_n.isEmpty()||pl_i.isEmpty()){
                    Toast.makeText(GameinfoActivity.this, "Enter Both Field", Toast.LENGTH_SHORT).show();
                }else {
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("pl_name", pl_n);
                    infoMap.put("pl_id", pl_i);
                    ginfo_ref.update(infoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(GameinfoActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                g_plite.setEnabled(false);
                                g_plite.setText("SAVED");
                                pl_gTic.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(GameinfoActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        g_freefire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String f_n = gameinfo_ffname.getText().toString().trim();
                String f_i = gameinfo_ffid.getText().toString().trim();
                if (f_i.isEmpty()||f_n.isEmpty()){
                    Toast.makeText(GameinfoActivity.this, "Enter Both Fields", Toast.LENGTH_SHORT).show();
                }else {
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("ff_name", f_n);
                    infoMap.put("ff_id", f_i);
                    ginfo_ref.update(infoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(GameinfoActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                g_freefire.setEnabled(false);
                                g_freefire.setText("SAVED");
                                f_gTic.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(GameinfoActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        g_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c_n = gameinfo_cname.getText().toString().trim();
                String c_i = gameinfo_cid.getText().toString().trim();
                if (c_i.isEmpty()||c_n.isEmpty()){
                    Toast.makeText(GameinfoActivity.this, "Enter Both Fields", Toast.LENGTH_SHORT).show();
                }else {
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("c_name", c_n);
                    infoMap.put("c_id", c_i);
                    ginfo_ref.update(infoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(GameinfoActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                g_cod.setEnabled(false);
                                g_cod.setText("SAVED");
                                c_gTic.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(GameinfoActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        g_mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mm_n = gameinfo_mmname.getText().toString().trim();
                String mm_i = gameinfo_mmid.getText().toString().trim();
                if (mm_i.isEmpty()||mm_n.isEmpty()){
                    Toast.makeText(GameinfoActivity.this, "Enter Both Fields", Toast.LENGTH_SHORT).show();
                }else{
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("mm_name", mm_n);
                    infoMap.put("mm_id", mm_i);
                    ginfo_ref.update(infoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(GameinfoActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                g_mini.setEnabled(false);
                                g_mini.setText("SAVED");
                                mm_gTic.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(GameinfoActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        g_ludo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String l_n = gameinfo_lname.getText().toString().trim();
                if (l_n.isEmpty()){
                    Toast.makeText(GameinfoActivity.this, "Enter the field", Toast.LENGTH_SHORT).show();
                }else{
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("l_name", l_n);
                    ginfo_ref.update(infoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(GameinfoActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                g_ludo.setEnabled(false);
                                g_ludo.setText("SAVED");
                                l_gTic.setVisibility(View.VISIBLE);
                            }else{
                                Toast.makeText(GameinfoActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        g_snake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_n = gameinfo_sname.getText().toString().trim();
                if (s_n.isEmpty()){
                    Toast.makeText(GameinfoActivity.this, "Enter the field", Toast.LENGTH_SHORT).show();
                }else{
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("s_name", s_n);
                    ginfo_ref.update(infoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(GameinfoActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                g_snake.setEnabled(false);
                                g_snake.setText("SAVED");
                                s_gTic.setVisibility(View.VISIBLE);
                            }else{
                                Toast.makeText(GameinfoActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        g_chess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chess_n = gameinfo_chessname.getText().toString().trim();
                if (chess_n.isEmpty()){
                    Toast.makeText(GameinfoActivity.this, "Enter the field", Toast.LENGTH_SHORT).show();
                }else{
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("chess_name", chess_n);
                    ginfo_ref.update(infoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(GameinfoActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                g_chess.setEnabled(false);
                                g_chess.setText("SAVED");
                                chess_gTic.setVisibility(View.VISIBLE);
                            }else{
                                Toast.makeText(GameinfoActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}