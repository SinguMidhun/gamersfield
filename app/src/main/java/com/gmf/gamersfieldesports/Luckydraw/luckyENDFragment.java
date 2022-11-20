package com.gmf.gamersfieldesports.Luckydraw;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class luckyENDFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference luckywinnerref;
    private RecyclerView luckyRecycler;
    private luckyEndAdapter luckyEndAdapter;



    public luckyENDFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lucky_e_n_d, container, false);


        //referance
        luckywinnerref = db.collection("PlayerDetails").document("Luckydraw").collection("winner");
        //query
        Query query = luckywinnerref.orderBy("contestNo", Query.Direction.DESCENDING);
        //firestore options
        FirestoreRecyclerOptions<luckyEndModel> options = new FirestoreRecyclerOptions.Builder<luckyEndModel>()
                .setQuery(query,luckyEndModel.class)
                .build();
        //adapter
        luckyEndAdapter = new luckyEndAdapter(options);
        //recycler view
        luckyRecycler = v.findViewById(R.id.luckyendrecycler);
        luckyRecycler.setHasFixedSize(true);
        luckyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        luckyRecycler.setAdapter(luckyEndAdapter);

        return v;
    }

    @Override
    public void onStart() {
        luckyEndAdapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        luckyEndAdapter.stopListening();
        super.onStop();
    }
}