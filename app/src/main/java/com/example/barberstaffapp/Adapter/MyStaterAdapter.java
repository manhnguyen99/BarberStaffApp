package com.example.barberstaffapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberstaffapp.Common.Common;
import com.example.barberstaffapp.Interface.IRecyclerItemSelectedListener;
import com.example.barberstaffapp.Model.City;
import com.example.barberstaffapp.R;
import com.example.barberstaffapp.SalonListActivity;

import java.util.List;

public class MyStaterAdapter extends RecyclerView.Adapter<MyStaterAdapter.MyViewHolder> {

    Context context;
    List<City> cityList;
    int lastPosition = -1;

    public MyStaterAdapter(Context context, List<City> cityList) {
        this.context = context;
        this.cityList = cityList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(context)
                .inflate(R.layout.layout_state, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull MyStaterAdapter.MyViewHolder holder, int position) {
        holder.txtStateName.setText(cityList.get(position).getName());
        setAnimation(holder.itemView,position);
        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                Common.state_name = cityList.get(position).getName();
                context.startActivity(new Intent(context, SalonListActivity.class));

            }
        });
    }
    private  void setAnimation(View itemView, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left);
            itemView.startAnimation(animation);
            lastPosition = position;
        }
    }



    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtStateName;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            txtStateName = itemView.findViewById(R.id.txtStateName);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(itemView, getAdapterPosition());
        }
    }
}
