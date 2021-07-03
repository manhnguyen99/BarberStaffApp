package com.example.barberstaffapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barberstaffapp.Adapter.MySalonAdapter;
import com.example.barberstaffapp.Common.Common;
import com.example.barberstaffapp.Common.SpacesItemDecoration;
import com.example.barberstaffapp.Interface.IBranchLoadListener;
import com.example.barberstaffapp.Interface.IOnLoadCountSalon;
import com.example.barberstaffapp.Model.Salon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class SalonListActivity extends AppCompatActivity implements IOnLoadCountSalon, IBranchLoadListener {
    TextView txtSalonCount;
    RecyclerView recyclerSalon;

    IOnLoadCountSalon onLoadCountSalon;
    IBranchLoadListener iBranchLoadListener;

    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_list);

        txtSalonCount = findViewById(R.id.txtSalonCount);
        recyclerSalon = findViewById(R.id.recyclerSalon);

        initView();

        innit();

        loadSalonBaseOnCity(Common.state_name);
    }
    private void loadSalonBaseOnCity(String name) {
        dialog.show();
        FirebaseDatabase.getInstance().getReference("Salon").child(name).child("Branch")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task <DataSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<Salon> salons = new ArrayList<>();
                            for (DataSnapshot dataSnapshot:task.getResult().getChildren())
                            {
                                Salon salon = dataSnapshot.getValue(Salon.class);
                                salon.setSalonID(dataSnapshot.getKey());
                                salons.add(salon);
                            }
                            iBranchLoadListener.onBranchLoadSuccess(salons);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                iBranchLoadListener.onBranchFailed(e.getMessage());
            }
        });


    }

    private void innit() {
        dialog = new SpotsDialog.Builder().setContext(this)
                .setCancelable(false)
                .build();
        onLoadCountSalon = this;
        iBranchLoadListener = this;
    }

    private void initView() {
        recyclerSalon.setHasFixedSize(true);
        recyclerSalon.setLayoutManager(new GridLayoutManager(this,2));
        recyclerSalon.addItemDecoration(new SpacesItemDecoration(8));
    }

    @Override
    public void onLoadCountSalonSuccess(int count) {
        txtSalonCount.setText(new StringBuilder("All Salon(")
        .append(count)
                .append(")"));
    }

    @Override
    public void onBranchLoadSuccess(List<Salon> branchList) {
        MySalonAdapter salonAdapter = new MySalonAdapter(this, branchList);
        recyclerSalon.setAdapter(salonAdapter);
        dialog.dismiss();

    }

    @Override
    public void onBranchFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}