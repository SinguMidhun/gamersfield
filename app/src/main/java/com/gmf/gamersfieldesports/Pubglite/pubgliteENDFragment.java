package com.gmf.gamersfieldesports.Pubglite;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gmf.gamersfieldesports.Pubg.pubgAdapter;
import com.gmf.gamersfieldesports.R;


public class pubgliteENDFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference pubgliteENDref = db.collection("LeagueMatches/Matches/Pubglite");
    private pubgAdapter pubgAdapter;


    public pubgliteENDFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pubglite_e_n_d, container, false);

        /*
        Query query = pubgliteENDref.whereEqualTo("ended","1")
                .orderBy("matchnum", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Model> options = new FirestoreRecyclerOptions.Builder<Model>()
                .setQuery(query, Model.class)
                .build();

        pubgAdapter = new pubgAdapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.pubgliteEND);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(pubgAdapter);

        pubgAdapter.setOnclickListener(new pubgAdapter.OnitemClickListener() {
            @Override
            public void onItemclick(DocumentSnapshot documentSnapshot, int position) {
                String documentid = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Intent pubgliteENDintent = new Intent(getContext(), pubgliteENDMatchActivity.class);
                pubgliteENDintent.putExtra("docRef",path);
                pubgliteENDintent.putExtra("docId",documentid);
                startActivity(pubgliteENDintent);
            }
        });
        */
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        //pubgAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        //pubgAdapter.stopListening();
    }
}