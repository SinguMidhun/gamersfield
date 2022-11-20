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
import com.google.firebase.firestore.DocumentSnapshot;

public class pubgAdapter extends FirestoreRecyclerAdapter<Model, pubgAdapter.pubgviewholder> {

    private OnitemClickListener listener;

    public pubgAdapter(@NonNull FirestoreRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull pubgviewholder holder, int position, @NonNull Model model) {
        holder.matchnum.setText(model.getMatchnum());
        holder.datetime.setText(model.getDatetime());
        holder.type.setText(model.getType());
        holder.prizepool.setText(model.getPrizepool());
        holder.map.setText(model.getMap());
        holder.total.setText(model.getTotal());
        holder.joined.setText(model.getJoined());
        holder.tName.setText(model.gettName());
        holder.tNotify.setText(model.gettNotify());
    }

    @NonNull
    @Override
    public pubgviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_match_layout, parent, false);
        return new pubgviewholder(v);
    }

    class pubgviewholder extends RecyclerView.ViewHolder {

        //variables
        TextView matchnum;
        TextView datetime;
        TextView prizepool;
        TextView type;
        TextView tName;
        TextView tNotify;
        TextView map;
        TextView joined;
        TextView total;

        public pubgviewholder(@NonNull View itemView) {
            super(itemView);
            matchnum = itemView.findViewById(R.id.v_matchnum);
            datetime = itemView.findViewById(R.id.v_timedate);
            type = itemView.findViewById(R.id.v_type);
            map = itemView.findViewById(R.id.v_map);
            joined = itemView.findViewById(R.id.p_joined);
            total = itemView.findViewById(R.id.p_total);
            prizepool = itemView.findViewById(R.id.v_prizepool);
            tName = itemView.findViewById(R.id.tName);
            tNotify = itemView.findViewById(R.id.matchnotify);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemclick(getSnapshots().getSnapshot(position), position);
                    } else {
                        return;
                    }
                }
            });
        }
    }

    public interface OnitemClickListener {
        void onItemclick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnclickListener(OnitemClickListener listener) {
        this.listener = listener;
    }
}
