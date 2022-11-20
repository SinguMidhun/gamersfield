package com.gmf.gamersfieldesports.Playerprofile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.gmf.gamersfieldesports.R;

public class RedeemHistoryActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mcurrentuser = mAuth.getCurrentUser();
    private String userId = mcurrentuser.getUid();
    private CollectionReference redeemhisCollref;
    private RecyclerView redeemhisRecyclerview;
    private RedeemHistoryadapter redeemHistoryadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_history);

        //collection
        redeemhisCollref = db.collection("users").document(userId).collection("RedeemHistory");
        //query
        Query query = redeemhisCollref.orderBy("transactionNo", Query.Direction.DESCENDING).limit(10);
        //options
        FirestoreRecyclerOptions<RedeemHistorymodel> options = new FirestoreRecyclerOptions.Builder<RedeemHistorymodel>()
                .setQuery(query,RedeemHistorymodel.class)
                .build();
        //adapter init
        redeemHistoryadapter = new RedeemHistoryadapter(options);

        redeemhisRecyclerview = findViewById(R.id.redeem_his_recycler);
        redeemhisRecyclerview.setHasFixedSize(true);
        redeemhisRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        redeemhisRecyclerview.setAdapter(redeemHistoryadapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        redeemHistoryadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        redeemHistoryadapter.stopListening();
    }
}