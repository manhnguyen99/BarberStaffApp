package com.example.barberstaffapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberstaffapp.Common.Common;
import com.example.barberstaffapp.Common.CustomLoginDialog;
import com.example.barberstaffapp.Interface.IDialogClickListener;
import com.example.barberstaffapp.Interface.IGetBaberListener;
import com.example.barberstaffapp.Interface.IRecyclerItemSelectedListener;
import com.example.barberstaffapp.Interface.IUserLoginRememberListener;
import com.example.barberstaffapp.MainActivity;
import com.example.barberstaffapp.Model.Baber;
import com.example.barberstaffapp.Model.Salon;
import com.example.barberstaffapp.R;
import com.example.barberstaffapp.StaffHomeActivity;
import com.example.barberstaffapp.StaffLogin;
import com.example.barberstaffapp.StaffRegistration;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MySalonAdapter extends RecyclerView.Adapter<MySalonAdapter.MyViewHolder> implements IDialogClickListener {

    Context context;
    List<Salon> salonList;
    List<CardView> cardViewList;

    IUserLoginRememberListener iUserLoginRememberListener;
    IGetBaberListener iGetBaberListener;

    DatabaseReference databaseReference;
    FirebaseAuth Fauth;


    public MySalonAdapter(Context context, List<Salon> salonList) {
        this.context = context;
        this.salonList = salonList;
        cardViewList = new ArrayList<>();
    }
    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_salon, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        holder.txtSalonName.setText(salonList.get(position).getNameSalon());
        holder.txtSalonAddress.setText(salonList.get(position).getAddressSalon());
        if (!cardViewList.contains(holder.card_salon)) {
            cardViewList.add(holder.card_salon);
            holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
                @Override
                public void onItemSelectedListener(View view, int pos) {
                    //set white background for all card not be selected
                    Common.selected_Salon = salonList.get(pos);
                    showLoginDialog();
                }
            });
        }
        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                Common.selected_Salon = salonList.get(position);
                showLoginDialog();
            }
        });
    }
    private void showLoginDialog() {
        CustomLoginDialog.getInstance()
                .showLoginDialog("STAFF LOGIN",
                        "Registration",
                        "Login",
                        context,
                        this
                );
    }
    @Override
    public int getItemCount() {
        return salonList.size();
    }

    @Override
    public void onClickPositiveButton(DialogInterface dialogInterface) {
        AlertDialog loading = new SpotsDialog.Builder().setCancelable(false)
                .setContext(context).build();
        loading.show();

        Intent staffReges = new Intent(context, StaffHomeActivity.class);
        staffReges.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        staffReges.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(staffReges);
    }

    @Override
    public void onClickNegativeButton(DialogInterface dialogInterface) {

        AlertDialog loading = new SpotsDialog.Builder().setCancelable(false)
                .setContext(context).build();
        loading.show();
        dialogInterface.dismiss();
        loading.dismiss();
        Intent stafflogin = new Intent(context, StaffLogin.class);
        stafflogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        stafflogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(stafflogin);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtSalonName, txtSalonAddress;
        CardView card_salon;
        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;
        LocalBroadcastManager localBroadcastManager;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txtSalonName = itemView.findViewById(R.id.txtSalonName);
            txtSalonAddress = itemView.findViewById(R.id.txtSalonAddress);
            card_salon = itemView.findViewById(R.id.card_salon);
            localBroadcastManager = LocalBroadcastManager.getInstance(context);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}
