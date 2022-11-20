package com.gmf.gamersfieldesports.Minimilitia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.gmf.gamersfieldesports.Pubg.k_w_Adapter;
import com.gmf.gamersfieldesports.Pubg.k_w_model;
import com.gmf.gamersfieldesports.R;

public class miniK_WActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference playerDetailRef;
    private String docId;
    private k_w_Adapter k_w_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_k__w);

        docId = getIntent().getStringExtra("docId");
        playerDetailRef = db.collection("PlayerDetails").document("Minimilitia").collection(docId);

        Query winnerquery = playerDetailRef.orderBy("winning", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<k_w_model> options = new FirestoreRecyclerOptions.Builder<k_w_model>()
                .setQuery(winnerquery, k_w_model.class)
                .build();
        k_w_Adapter = new k_w_Adapter(options);

        RecyclerView recyclerView = findViewById(R.id.mini_winner);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(k_w_Adapter);


    }

    @Override
    protected void onStop() {
        super.onStop();
        k_w_Adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        k_w_Adapter.startListening();
    }

}