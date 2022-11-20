package com.gmf.gamersfieldesports.Snakerivals;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gmf.gamersfieldesports.Pubg.Model;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.gmf.gamersfieldesports.Pubg.pubgAdapter;
import com.gmf.gamersfieldesports.R;

public class snakeBRFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference snakeMatchref = db.collection("LeagueMatches/Matches/Snakerivals");
    private pubgAdapter pubgAdapter;


    public snakeBRFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_snake_b_r, container, false);

        Query query = snakeMatchref.whereEqualTo("ended","0")
                .orderBy("matchnum", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Model> options = new FirestoreRecyclerOptions.Builder<Model>()
                .setQuery(query, Model.class)
                .build();

        pubgAdapter = new pubgAdapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.snakeBR);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(pubgAdapter);

        pubgAdapter.setOnclickListener(new pubgAdapter.OnitemClickListener() {
            @Override
            public void onItemclick(DocumentSnapshot documentSnapshot, int position) {
                String documentid = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Intent snakematchintent = new Intent(getContext(), snakeMatchActivity.class);
                snakematchintent.putExtra("docRef",path);
                snakematchintent.putExtra("docId",documentid);
                startActivity(snakematchintent);
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