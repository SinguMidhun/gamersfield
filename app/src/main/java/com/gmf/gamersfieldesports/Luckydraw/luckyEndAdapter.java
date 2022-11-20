package com.gmf.gamersfieldesports.Luckydraw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gmf.gamersfieldesports.R;

public class luckyEndAdapter extends FirestoreRecyclerAdapter<luckyEndModel,luckyEndAdapter.luckuEndHolder> {


    public luckyEndAdapter(@NonNull FirestoreRecyclerOptions<luckyEndModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull luckuEndHolder holder, int position, @NonNull luckyEndModel model) {
        holder.date.setText(model.getDate());
        holder.name.setText(model.getName());
    }

    @NonNull
    @Override
    public luckuEndHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_lucky_winner,parent,false);
        return new luckuEndHolder(v);
    }

    class luckuEndHolder extends RecyclerView.ViewHolder{

        private TextView date;
        private TextView name;

        public luckuEndHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.luckyenddate);
            name = itemView.findViewById(R.id.luckyendwinner);

        }
    }

}
