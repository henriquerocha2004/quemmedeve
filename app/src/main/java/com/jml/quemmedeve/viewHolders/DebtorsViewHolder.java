package com.jml.quemmedeve.viewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jml.quemmedeve.R;

public class DebtorsViewHolder extends RecyclerView.ViewHolder {

    final public TextView nome;
    final public TextView debtValue;
    final public CardView card;

    public DebtorsViewHolder(@NonNull View itemView) {
        super(itemView);

        nome =  itemView.findViewById(R.id.txtNameDebtor);
        debtValue = itemView.findViewById(R.id.txtTotalDebts);
        card = itemView.findViewById(R.id.card);
    }
}
