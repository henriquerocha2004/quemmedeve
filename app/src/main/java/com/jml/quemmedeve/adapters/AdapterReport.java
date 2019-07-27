package com.jml.quemmedeve.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jml.quemmedeve.R;
import com.jml.quemmedeve.bean.DebtsBean;
import com.jml.quemmedeve.ultility.DateUltility;
import com.jml.quemmedeve.viewHolders.ReportViewHolder;

import java.util.List;

public class AdapterReport extends RecyclerView.Adapter {

    private List<DebtsBean> list;
    private Context context;
    private View.OnClickListener mClickListener;

    public AdapterReport(List<DebtsBean> list, Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_component, viewGroup, false);
        ReportViewHolder holder = new ReportViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onClick(v);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ReportViewHolder holder = (ReportViewHolder) viewHolder;
            DebtsBean debt = list.get(i);
            holder.lbNomeCliente.setText(debt.getDebtorName());
            holder.lbValor.setText(debt.getValorTotal());
            holder.lbDescricao.setText(debt.getDebt_desc());
            holder.lbData.setText(DateUltility.formataBR(debt.getDebt_desc()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setmClickListener(View.OnClickListener callback){
        mClickListener = callback;
    }

}
