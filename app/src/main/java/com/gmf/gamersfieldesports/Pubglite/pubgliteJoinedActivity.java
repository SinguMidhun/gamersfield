package com.gmf.gamersfieldesports.Pubglite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.gmf.gamersfieldesports.Pubg.joinedAdapter;
import com.gmf.gamersfieldesports.Pubg.joinedModel;
import com.gmf.gamersfieldesports.R;

public class pubgliteJoinedActivity extends AppCompatActivity {

    //adapter
    private joinedAdapter joinedAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference joinedCollref;
    //intent String
    private String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubglite_joined);

        //intent strings
        docId = getIntent().getStringExtra("docId");

        //collection referance
        joinedCollref = db.collection("PlayerDetails").document("Pubglite").collection(docId);
        Query joinedquery = joinedCollref;
        //options
        FirestoreRecyclerOptions<joinedModel> options = new FirestoreRecyclerOptions.Builder<joinedModel>()
                .setQuery(joinedquery,joinedModel.class)
                .build();
        //adapter
        joinedAdapter = new joinedAdapter(options);
        //recyclerview
        RecyclerView recyclerView = findViewById(R.id.pubglite_joined);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(joinedAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        joinedAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        joinedAdapter.stopListening();

    }
}