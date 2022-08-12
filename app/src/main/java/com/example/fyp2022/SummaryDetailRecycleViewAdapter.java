package com.example.fyp2022;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SummaryDetailRecycleViewAdapter extends RecyclerView.Adapter<SummaryDetailRecycleViewAdapter.MyViewHolder> {
    List<BudgetModal> budgetDetails;
    Context context;

    public SummaryDetailRecycleViewAdapter(List<BudgetModal> budgetDetails, Context context) {
        this.budgetDetails = budgetDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflater
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_summary_details,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // assigning value that goes with each line
        holder.tv_transactID.setText(String.valueOf(budgetDetails.get(position).getBudgetID()));
        holder.tv_budgetSpent.setText(String.valueOf(budgetDetails.get(position).getBudgetUsed()));
        holder.tv_transactDate.setText(budgetDetails.get(position).getBudgetDate());

    }

    @Override
    public int getItemCount() {
        return budgetDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        // references to the one_line_summary_detail layout
        TextView tv_transactID, tv_budgetSpent, tv_transactDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_transactID = itemView.findViewById(R.id.tv_transactID);
            tv_budgetSpent = itemView.findViewById(R.id.tv_budgetSpent);
            tv_transactDate = itemView.findViewById(R.id.tv_transactDate);
        }
    }
}
