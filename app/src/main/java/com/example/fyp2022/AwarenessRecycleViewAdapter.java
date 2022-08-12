package com.example.fyp2022;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AwarenessRecycleViewAdapter extends RecyclerView.Adapter<AwarenessRecycleViewAdapter.MyViewHolder> {
    List<AwarenessModal> awarenessList;
    Context context;

    public AwarenessRecycleViewAdapter(List<AwarenessModal> awarenessList, Context context) {
        this.awarenessList = awarenessList;
        this.context = context;
    }

    @NonNull
    @Override
    public AwarenessRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflater
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_awareness_details,parent,false);
        AwarenessRecycleViewAdapter.MyViewHolder holder = new AwarenessRecycleViewAdapter.MyViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull AwarenessRecycleViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // assigning value that goes with each line
        holder.tv_awareTitle.setText(String.valueOf(awarenessList.get(position).getAwareTitle()));
        holder.tv_awareDesc.setText(String.valueOf(awarenessList.get(position).getAwareDesc()));
        holder.tv_awareTag.setText(awarenessList.get(position).getAwareTag());
        Glide.with(this.context).load(awarenessList.get(position).getImageUrl()).into(holder.iv_imageUrl);

        String tag = awarenessList.get(position).getAwareTag();
        if (tag.equalsIgnoreCase("News")) {
            // if tag is news
            holder.tv_awareTag.setBackgroundColor(context.getColor(R.color.grey_200));
            holder.tv_awareTag.setTextColor(context.getColor(R.color.black));

        } else {
            // tag = tips
            holder.tv_awareTag.setBackgroundColor(context.getColor(R.color.green_300));
            holder.tv_awareTag.setTextColor(context.getColor(R.color.black));
        }

        holder.iv_imageUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String awareLink = awarenessList.get(position).getAwareLink();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(awareLink));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return awarenessList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_awareTitle, tv_awareDesc, tv_awareTag;
        ImageView iv_imageUrl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_awareTitle = itemView.findViewById(R.id.tv_awareTitle);
            tv_awareDesc = itemView.findViewById(R.id.tv_awareDesc);
            tv_awareTag = itemView.findViewById(R.id.tv_awareTag);
            iv_imageUrl = itemView.findViewById(R.id.iv_imageUrl);

            // adding ellipsis if more than 3 lines
            ViewTreeObserver vto = this.tv_awareDesc.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver obs = tv_awareDesc.getViewTreeObserver();
                    obs.removeOnGlobalLayoutListener(this);
                    if(tv_awareDesc.getLineCount() > 3) {
                        int lineEndIndex = tv_awareDesc.getLayout().getLineEnd(2);
                        String text = tv_awareDesc.getText().subSequence(0,lineEndIndex-3) + "...";
                        tv_awareDesc.setText(text);
                    }
                }
            });
        }
    }
}
