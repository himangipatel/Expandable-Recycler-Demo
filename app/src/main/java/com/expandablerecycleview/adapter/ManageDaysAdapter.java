package com.expandablerecycleview.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expandablerecycleview.R;
import com.expandablerecycleview.model.Days;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Himangi on 26/7/17
 */

public class ManageDaysAdapter extends RecyclerView.Adapter<ManageDaysAdapter.DaysViewHolder> {

    private Context context;
    private List<Days> mDays = new ArrayList<>();

    public ManageDaysAdapter(Context context) {
        this.context = context;
        addWeekDays();
    }

    @Override
    public DaysViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new DaysViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_days,parent,false));
    }

    @Override
    public void onBindViewHolder(DaysViewHolder holder, final int position) {
        final Days days = mDays.get(position);

        holder.tvDay.setText(days.getDay());

        if (days.isSelected()){
            holder.tvDay.setTextColor(context.getResources().getColor(android.R.color.white));
            holder.tvDay.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_selected_bg));
        }else {
            holder.tvDay.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_bg));
            holder.tvDay.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (days.isSelected()){
                    days.setSelected(false);
                }else {
                    days.setSelected(true);
                }

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    private void addWeekDays(){
        mDays.add(new Days("Monday",false));
        mDays.add(new Days("Tuesday",false));
        mDays.add(new Days("Wednesday",false));
        mDays.add(new Days("Thursday",false));
        mDays.add(new Days("Friday",false));
        mDays.add(new Days("Saturday",false));
        mDays.add(new Days("Sunday",false));
    }

    class DaysViewHolder extends RecyclerView.ViewHolder {

        TextView tvDay;

        public DaysViewHolder(View itemView) {
            super(itemView);
            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
        }
    }
}
