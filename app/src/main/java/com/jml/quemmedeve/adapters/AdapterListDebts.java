package com.jml.quemmedeve.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jml.quemmedeve.R;
import com.jml.quemmedeve.bean.DebtsBean;
import com.jml.quemmedeve.ultility.NumberUtility;
import com.jml.quemmedeve.viewHolders.ListDebtsViewHolder;
import java.util.List;


public class AdapterListDebts extends RecyclerView.Adapter {

    private List<DebtsBean> list;
    private Context context;
    private View.OnClickListener mClickListener;


    public AdapterListDebts(List<DebtsBean> list, Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.fields_list_view_cliente, viewGroup, false);
        ListDebtsViewHolder holder = new ListDebtsViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               mClickListener.onClick(v);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ListDebtsViewHolder holder = (ListDebtsViewHolder) viewHolder;
        DebtsBean debt = list.get(i);
        holder.desc_debt.setText(debt.getDebt_desc());
        holder.debt_value.setText(NumberUtility.converterBr(debt.getValue()));
        holder.split_value.setText(NumberUtility.converterBr(debt.getValue_split()));
        holder.debt_split.setText(debt.getDebt_split());
        holder.txtStatus.setText((debt.getStatus_debt() == 0 ? "Pendente" : "Pago"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public int getIdDebt(int position){
        return list.get(position).getId();
    }

    public void setClickListener(View.OnClickListener callback){
        mClickListener = callback;
    }
}
