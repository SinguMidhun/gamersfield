package com.gmf.gamersfieldesports.Playerprofile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gmf.gamersfieldesports.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.startapp.sdk.adsbase.StartAppAd;

public class TransactionsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mcurrentuser = mAuth.getCurrentUser();
    private String userId = mcurrentuser.getUid();
    private CollectionReference transCollref;
    private RecyclerView transRecyclerview;
    private transactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);


        //transRef
        transCollref = db.collection("users").document(userId).collection("Transactions");
        //query
        Query query = transCollref.orderBy("transactionNo", Query.Direction.DESCENDING).limit(10);
        //options
        FirestoreRecyclerOptions<transactionModel>options = new FirestoreRecyclerOptions.Builder<transactionModel>()
                .setQuery(query,transactionModel.class)
                .build();
        //adapter
        transactionAdapter = new transactionAdapter(options);
        //recyclerview
        transRecyclerview = findViewById(R.id.transactionsDaily);
        transRecyclerview.setHasFixedSize(true);
        transRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        transRecyclerview.setAdapter(transactionAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        transactionAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        transactionAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StartAppAd.onBackPressed(this);
    }
}