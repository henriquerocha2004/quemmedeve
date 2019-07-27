package com.jml.quemmedeve.viewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.jml.quemmedeve.R;

public class ReportViewHolder  extends RecyclerView.ViewHolder {

    final public TextView lbData;
    final public TextView lbDescricao;
    final public TextView lbNomeCliente;
    final public TextView lbValor;

    public ReportViewHolder(@NonNull View itemView){
        super(itemView);
        lbData = itemView.findViewById(R.id.lbData);
        lbDescricao = itemView.findViewById(R.id.lbDescricao);
        lbNomeCliente = itemView.findViewById(R.id.lbNomeCliente);
        lbValor = itemView.findViewById(R.id.lbValor);
    }

}
