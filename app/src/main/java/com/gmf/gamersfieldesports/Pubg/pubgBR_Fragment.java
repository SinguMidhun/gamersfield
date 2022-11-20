package com.gmf.gamersfieldesports.Pubg;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gmf.gamersfieldesports.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class pubgBR_Fragment extends Fragment{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference pubgMatchref = db.collection("LeagueMatches/Matches/Pubg");
    private pubgAdapter pubgAdapter;

    public pubgBR_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pubg_b_r_, container, false);

        Query query = pubgMatchref.whereEqualTo("Btype","BR").whereEqualTo("ended","0")
                .orderBy("matchnum", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Model> options = new FirestoreRecyclerOptions.Builder<Model>()
                .setQuery(query, Model.class)
                .build();

        pubgAdapter = new pubgAdapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.pubgBR);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(pubgAdapter);

        pubgAdapter.setOnclickListener(new pubgAdapter.OnitemClickListener() {
            @Override
            public void onItemclick(DocumentSnapshot documentSnapshot, int position) {
                String documentid = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Intent pubgmatchintent = new Intent(getContext(),pubgMatchActivity.class);
                pubgmatchintent.putExtra("docRef",path);
                pubgmatchintent.putExtra("docId",documentid);
                startActivity(pubgmatchintent);
            }
        });

        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        pubgAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        pubgAdapter.stopListening();
    }
}