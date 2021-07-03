package com.example.barberstaffapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberstaffapp.Common.Common;
import com.example.barberstaffapp.Interface.IRecyclerItemSelectedListener;
import com.example.barberstaffapp.Model.TimeSlot;
import com.example.barberstaffapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {
    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;


    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        cardViewList = new ArrayList<>();
    }
    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot,parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.txtTimeSlot.setText(new StringBuilder(Common.convertTimeSlotToString(position)).toString());
        if (timeSlotList.size() == 0)
        {
            holder.cardTimeSlot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
            holder.txtTimeSlotDescription.setText("Available");
            holder.txtTimeSlotDescription.setTextColor(context.getResources()
                    .getColor(android.R.color.black));
            holder.txtTimeSlot.setTextColor(context.getResources().getColor(android.R.color.black));

        }
        else //nếu có vị trí đã đầy  (booked)
        {
            for(TimeSlot slotValue:timeSlotList) {
                //Lặp lại tất cả các khoangr thời gian từ máy chủ và đặt màu khác nhau
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if (slot == position) {
                    //đặt thẻ cho tất cả các khoảng thời gian đã đầy
                    //dựa trên thẻ, có thể đặt tất cả nền thẻ còn lại mà không cần thay đổi toàn thời gian
                    holder.cardTimeSlot.setTag(Common.DISABLE_TAG);
                    holder.cardTimeSlot.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));

                    holder.txtTimeSlotDescription.setText("Full");
                    holder.txtTimeSlotDescription.setTextColor(context.getResources()
                            .getColor(android.R.color.white));
                    holder.txtTimeSlot.setTextColor(context.getResources().getColor(android.R.color.white));

                }
            }
        }
        //them tat ca card vao danh sach
        if(!cardViewList.contains(holder.cardTimeSlot))
            cardViewList.add(holder.cardTimeSlot);
        //kiem tra neu card time slot available

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {

                //Loop all card in card list
                for(CardView cardView:cardViewList)
                {
                    if (cardView.getTag() == null) //only available card time slot be change
                        cardView.setCardBackgroundColor(context.getResources()
                                .getColor(android.R.color.white));
                }
                //Our selected card will be change color
                holder.cardTimeSlot.setCardBackgroundColor(context.getResources()
                        .getColor(android.R.color.holo_orange_dark));
            }
        });
    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTimeSlot, txtTimeSlotDescription;
        CardView cardTimeSlot;
        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cardTimeSlot = itemView.findViewById(R.id.card_time_slot);
            txtTimeSlot = itemView.findViewById(R.id.txtTimeSlot);
            txtTimeSlotDescription = itemView.findViewById(R.id.txtTimeSlotDescription);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}
