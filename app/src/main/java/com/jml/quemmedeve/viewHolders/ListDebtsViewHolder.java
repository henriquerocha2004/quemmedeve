package com.jml.quemmedeve.viewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jml.quemmedeve.R;

public class ListDebtsViewHolder extends RecyclerView.ViewHolder {

    final public TextView desc_debt;
    final public TextView debt_value;
    final public TextView debt_split;
    final public TextView split_value;
    final public TextView txtStatus;


    public ListDebtsViewHolder(@NonNull View itemView) {
        super(itemView);
        desc_debt = itemView.findViewById(R.id.desc_debt);
        debt_value = itemView.findViewById(R.id.debt_value);
        debt_split = itemView.findViewById(R.id.debt_split);
        split_value = itemView.findViewById(R.id.split_value);
        txtStatus = itemView.findViewById(R.id.txtStatus);
    }
}
