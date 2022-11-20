package com.gmf.gamersfieldesports.Userdetails;

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

public class MyMatchesActivity extends AppCompatActivity {

    //adapter
    private mm_adapter mm_adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentuser = mAuth.getCurrentUser();
    private String userId = mCurrentuser.getUid().toString();
    private CollectionReference mymatchCollref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_matches);
        //my matches ref
        mymatchCollref = db.collection("users").document(userId).collection("Mymatches");
        //query
        Query mm_query = mymatchCollref.orderBy("MatchNumber", Query.Direction.DESCENDING);
        //options
        FirestoreRecyclerOptions<mm_model> options = new FirestoreRecyclerOptions.Builder<mm_model>()
                .setQuery(mm_query,mm_model.class)
                .build();
        mm_adapter = new mm_adapter(options);

        RecyclerView mm_recyclerview = findViewById(R.id.myMatchesRec);
        mm_recyclerview.setHasFixedSize(true);
        mm_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mm_recyclerview.setAdapter(mm_adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mm_adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mm_adapter.stopListening();
    }
}