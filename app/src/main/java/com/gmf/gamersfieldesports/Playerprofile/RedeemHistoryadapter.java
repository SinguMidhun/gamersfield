package com.gmf.gamersfieldesports.Playerprofile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gmf.gamersfieldesports.R;

public class RedeemHistoryadapter extends FirestoreRecyclerAdapter<RedeemHistorymodel,RedeemHistoryadapter.redeemhistoryviewholder> {

    public RedeemHistoryadapter(@NonNull FirestoreRecyclerOptions<RedeemHistorymodel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull redeemhistoryviewholder holder, int position, @NonNull RedeemHistorymodel model) {
        holder.redeemItemtext.setText(model.getRedeemItem());
        holder.datetext.setText(model.getDate());
        holder.Statustext.setText(model.getStatus());
        String transno = model.getTransactionNo() + "";
        holder.transactionNotext.setText(transno);

    }

    @NonNull
    @Override
    public redeemhistoryviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.redeem_history_single,parent,false);
        return new redeemhistoryviewholder(v);
    }

    class redeemhistoryviewholder extends RecyclerView.ViewHolder{

        TextView Statustext;
        TextView datetext;
        TextView redeemItemtext;
        TextView transactionNotext;

        public redeemhistoryviewholder(@NonNull View itemView) {
            super(itemView);
            Statustext = itemView.findViewById(R.id.redeem_his_status);
            datetext = itemView.findViewById(R.id.redeem_his_date);
            redeemItemtext = itemView.findViewById(R.id.redeem_his_amount);
            transactionNotext = itemView.findViewById(R.id.redeem_his_trans);
        }
    }

}
