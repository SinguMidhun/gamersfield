package com.gmf.gamersfieldesports.Userdetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gmf.gamersfieldesports.R;

public class mm_adapter extends FirestoreRecyclerAdapter<mm_model,mm_adapter.mm_viewholder> {

    public mm_adapter(@NonNull FirestoreRecyclerOptions<mm_model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull mm_viewholder holder, int position, @NonNull mm_model model) {
        holder.mm_matchnum.setText(model.getMatchNumber());
        holder.mm_date.setText(model.getDate());
        holder.mm_type.setText(model.getType());
        holder.mm_map.setText(model.getMap());
        holder.mm_btype.setText(model.getBType());
    }

    @NonNull
    @Override
    public mm_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_my_match,parent,false);
        return new mm_viewholder(v);
    }

    //viewholder
    class mm_viewholder extends RecyclerView.ViewHolder{

        TextView mm_matchnum;
        TextView mm_date;
        TextView mm_type;
        TextView mm_map;
        TextView mm_btype;

        public mm_viewholder(@NonNull View itemView) {
            super(itemView);

            mm_matchnum = itemView.findViewById(R.id.v_mmmatchnum);
            mm_date = itemView.findViewById(R.id.v_mmdate);
            mm_type = itemView.findViewById(R.id.v_mmtype);
            mm_map = itemView.findViewById(R.id.v_mmmap);
            mm_btype = itemView.findViewById(R.id.v_mmbtype);

        }
    }
}
