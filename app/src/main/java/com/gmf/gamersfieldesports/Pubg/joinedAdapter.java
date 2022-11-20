package com.gmf.gamersfieldesports.Pubg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gmf.gamersfieldesports.R;

public class joinedAdapter extends FirestoreRecyclerAdapter<joinedModel, joinedAdapter.joinedViewholder> {

    public joinedAdapter(@NonNull FirestoreRecyclerOptions<joinedModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull joinedViewholder holder, int position, @NonNull joinedModel model) {
        holder.joinedplayername.setText(model.getName());
    }

    @NonNull
    @Override
    public joinedViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.joined_players,parent,false);
        return new joinedViewholder(v);
    }

    class joinedViewholder extends RecyclerView.ViewHolder{

        TextView joinedplayername;

        public joinedViewholder(@NonNull View itemView) {
            super(itemView);
            joinedplayername = itemView.findViewById(R.id.joinedplayerName);
        }
    }

}
