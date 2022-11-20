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

public class k_w_Adapter extends FirestoreRecyclerAdapter<k_w_model,k_w_Adapter.k_w_holder> {

    public k_w_Adapter(@NonNull FirestoreRecyclerOptions<k_w_model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull k_w_holder holder, int position, @NonNull k_w_model model) {
        holder.name.setText(model.getName());
        holder.winning.setText(model.getWinning());
        holder.position.setText(model.getPosition());
        holder.kills.setText(model.getKills());
    }

    @NonNull
    @Override
    public k_w_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_k_w_layout,parent,false);
        return new k_w_holder(v);
    }

    //view holder
    class k_w_holder extends RecyclerView.ViewHolder{

        TextView name;
        TextView position;
        TextView kills;
        TextView winning;

        public k_w_holder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.v_kwname);
            position = itemView.findViewById(R.id.v_kwpostion);
            kills = itemView.findViewById(R.id.v_kwkills);
            winning = itemView.findViewById(R.id.v_kwwinning);

        }
    }
}
