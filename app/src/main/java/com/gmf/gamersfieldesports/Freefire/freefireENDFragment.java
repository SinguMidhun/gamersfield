package com.gmf.gamersfieldesports.Freefire;

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

public class

freefireENDFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference freefireENDref = db.collection("LeagueMatches/Matches/Freefire");
    private pubgAdapter pubgAdapter;


    public freefireENDFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_freefire_e_n_d, container, false);

        Query query = freefireENDref.whereEqualTo("ended","1")
                .orderBy("matchnum", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Model> options = new FirestoreRecyclerOptions.Builder<Model>()
                .setQuery(query, Model.class)
                .build();

        pubgAdapter = new pubgAdapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.freefireEND);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(pubgAdapter);

        pubgAdapter.setOnclickListener(new pubgAdapter.OnitemClickListener() {
            @Override
            public void onItemclick(DocumentSnapshot documentSnapshot, int position) {
                String documentid = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Intent freefireENDintent = new Intent(getContext(), freefireENDMatchActivity.class);
                freefireENDintent.putExtra("docRef",path);
                freefireENDintent.putExtra("docId",documentid);
                startActivity(freefireENDintent);
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