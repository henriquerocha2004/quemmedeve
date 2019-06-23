package com.jml.quemmedeve.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jml.quemmedeve.R;
import com.jml.quemmedeve.bean.DebtorsBean;
import com.jml.quemmedeve.viewHolders.ListDebtorsViewHolder;
import java.util.List;

public class AdapterListDebtors extends RecyclerView.Adapter {

    private List<DebtorsBean> list;
    private Context context;
    private View.OnClickListener mClickListener;

    public AdapterListDebtors(List<DebtorsBean> list, Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_debtors_fields, viewGroup, false);
        ListDebtorsViewHolder holder = new ListDebtorsViewHolder(view);
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

        ListDebtorsViewHolder holder = (ListDebtorsViewHolder) viewHolder;
        DebtorsBean debtor = list.get(i);
        holder.nome.setText(debtor.getName());
        holder.debtValue.setText(debtor.getValueDebt());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setClickListener(View.OnClickListener callback){
        mClickListener = callback;
    }

    public int getIdDebtor(int position){
        return list.get(position).getId();
    }

    public String getTotalValor(int position){
        return list.get(position).getValueDebt();
    }

    public String getNameClient(int position){ return list.get(position).getName();}

}
