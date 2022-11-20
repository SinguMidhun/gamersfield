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

public class transactionAdapter extends FirestoreRecyclerAdapter<transactionModel,transactionAdapter.transHolder> {

    public transactionAdapter(@NonNull FirestoreRecyclerOptions<transactionModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull transHolder holder, int position, @NonNull transactionModel model) {
        holder.transtype.setText(model.getType());
        holder.transAmt.setText(model.getWinDiamonds());
        String transnumber = String.valueOf(model.getTransactionNo());
        holder.transNo.setText(transnumber);
    }

    @NonNull
    @Override
    public transHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_transaction_layout,parent,false);
        return new transHolder(v);
    }

    //viewholder
    public class transHolder extends RecyclerView.ViewHolder{

        TextView transAmt;
        TextView transNo;
        TextView transtype;

        public transHolder(@NonNull View itemView) {
            super(itemView);

            transAmt = itemView.findViewById(R.id.trans_amount);
            transNo = itemView.findViewById(R.id.trans_num);
            transtype = itemView.findViewById(R.id.trans_type);

        }
    }

}
