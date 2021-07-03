package com.example.barberstaffapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.barberstaffapp.Adapter.MyStaterAdapter;
import com.example.barberstaffapp.Common.SpacesItemDecoration;
import com.example.barberstaffapp.Interface.IOnAllStateLoadListener;
import com.example.barberstaffapp.Model.City;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements IOnAllStateLoadListener {

    RecyclerView recyclerState;
    DatabaseReference allSalonRef;
    IOnAllStateLoadListener iOnAllStateLoadListener;
    MyStaterAdapter adapter;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerState = findViewById(R.id.recyclerState);

        initView();

        innit();

        loadAllstateFromDatabase();

    }

    private void loadAllstateFromDatabase() {
        dialog.show();

        allSalonRef
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        iOnAllStateLoadListener.onAllStateLoadFailed(e.getMessage());
                    }
                }).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    List<City> cities = new ArrayList<>();
                    for (DataSnapshot dataSnapshot:task.getResult().getChildren())
                    {
                        City city = dataSnapshot.getValue(City.class);
                        cities.add(city);
                    }
                    iOnAllStateLoadListener.onAllStateLoadSuccess(cities);
                }
            }
        });
    }

    private void innit() {
        allSalonRef = FirebaseDatabase.getInstance().getReference("Salon");
        iOnAllStateLoadListener = this;
        dialog = new SpotsDialog.Builder().setContext(this)
                .setCancelable(false)
                .build();
    }

    private void initView() {
        recyclerState.setHasFixedSize(true);
        recyclerState.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerState.addItemDecoration(new SpacesItemDecoration(8));
    }

    @Override
    public void onAllStateLoadSuccess(List<City> cityList) {
        adapter = new MyStaterAdapter(this, cityList);
        recyclerState.setAdapter(adapter);
        dialog.dismiss();
    }

    @Override
    public void onAllStateLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}