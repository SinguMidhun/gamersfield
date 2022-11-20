package com.gmf.gamersfieldesports.COD;

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


public class codBRFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference codMatchref = db.collection("LeagueMatches/Matches/Cod");
    private pubgAdapter pubgAdapter;

    public codBRFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cod_b_r, container, false);

        Query query = codMatchref.whereEqualTo("Btype","BR").whereEqualTo("ended","0")
                .orderBy("matchnum", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Model> options = new FirestoreRecyclerOptions.Builder<Model>()
                .setQuery(query, Model.class)
                .build();

        pubgAdapter = new pubgAdapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.codBR);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(pubgAdapter);

        pubgAdapter.setOnclickListener(new pubgAdapter.OnitemClickListener() {
            @Override
            public void onItemclick(DocumentSnapshot documentSnapshot, int position) {
                String documentid = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Intent codmatchintent = new Intent(getContext(), codMatchActivity.class);
                codmatchintent.putExtra("docRef",path);
                codmatchintent.putExtra("docId",documentid);
                startActivity(codmatchintent);
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