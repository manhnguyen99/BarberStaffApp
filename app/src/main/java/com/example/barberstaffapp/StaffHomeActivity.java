package com.example.barberstaffapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.barberstaffapp.Adapter.MyTimeSlotAdapter;
import com.example.barberstaffapp.Common.Common;
import com.example.barberstaffapp.Common.SpacesItemDecoration;
import com.example.barberstaffapp.Interface.ITimeSlotLoadListener;
import com.example.barberstaffapp.Model.Baber;
import com.example.barberstaffapp.Model.Salon;
import com.example.barberstaffapp.Model.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

public class StaffHomeActivity extends AppCompatActivity implements ITimeSlotLoadListener {

    DatabaseReference barberRef;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    android.app.AlertDialog alertDialog;

    HorizontalCalendarView calendarView;

    RecyclerView recyclerTimeSlot;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);
        getControls();
        innitt();
        initView();
//        loadBaber(Common.currentBaber);

    }
//    //test
//    private void loadBaber(Baber baber) {
//        alertDialog.show();
//        DatabaseReference branchRef;
//        branchRef = FirebaseDatabase.getInstance()
//                .getReference("Salon")
//                .child(Common.state_name)
//                .child("Branch")
//                .child(Common.selected_Salon.getSalonID())
//                .child("Baber")
//        ;
//        branchRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
//
//                if (task.isSuccessful()) {
//                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
//                        {
//                            Baber baber = dataSnapshot.getValue(Baber.class);
//                            baber.setBaberId(dataSnapshot.getKey());
//                            Common.currentBaber.getBaberId();
//                        }
//                    }
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull @NotNull Exception e) {
//                Toast.makeText(StaffHomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void innitt() {
        iTimeSlotLoadListener = this;
    }


    private void getControls() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        calendarView = findViewById(R.id.calendarView);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void initView() {

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.open,
                R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(this)
                .build();

        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, 0);
        loadAvailableTimeslotOfBarber(Common.currentBaber.getBaberId(),
                Common.simpleDateFormat.format(date.getTime()));

        recyclerTimeSlot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(StaffHomeActivity.this, 3);
        recyclerTimeSlot.setLayoutManager(gridLayoutManager);
        recyclerTimeSlot.addItemDecoration(new SpacesItemDecoration(8));

        //calendar
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 2); //2 day left

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .configure()
                .end()
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if (Common.currentDate.getTimeInMillis() != date.getTimeInMillis()) {
                    Common.currentDate = date;
                    loadAvailableTimeslotOfBarber(Common.currentBaber.getBaberId(),
                            simpleDateFormat.format(date.getTime()));
                }
            }
        });

    
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                if (item.getItemId() == R.id.menu_exit)
                    LogOut();
                return true;
            }
        });


    }



    private void loadAvailableTimeslotOfBarber(String baberId, String bookDate) {

        alertDialog.show();
        barberRef = FirebaseDatabase.getInstance()
                .getReference("Salon")
                .child(Common.state_name)
                .child("Branch")
                .child(Common.selected_Salon.getSalonID())
                .child("Baber")
                .child(Common.currentBaber.getBaberId());
        //get Informationn of this barber
        barberRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {

                        // get information of booking
                        //if not created, teturn empty
                        Salon salon = dataSnapshot.getValue(Salon.class);
                        salon.setSalonID(dataSnapshot.getKey());

                        DatabaseReference date = FirebaseDatabase.getInstance()
                                .getReference("Salon")
                                .child(Common.state_name)
                                .child("Branch")
                                .child(Common.selected_Salon.getSalonID())
                                .child("Baber")
                                .child(Common.currentBaber.getBaberId())
                                .child(bookDate);
                        date.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DataSnapshot dataSnapshot1 = task.getResult();
                                    if (!dataSnapshot1.exists())
                                        iTimeSlotLoadListener.onTimeslotLoadempty();
                                    else {
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for (DataSnapshot dataSnapshot2 : task.getResult().getChildren())
                                            timeSlots.add(dataSnapshot2.getValue(TimeSlot.class));
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                                        alertDialog.dismiss();
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    ;

    private void LogOut() {

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(StaffHomeActivity.this, "Fake function exit", Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(this, timeSlotList);
        recyclerTimeSlot.setAdapter(adapter);
        alertDialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeslotLoadempty() {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(this);
        recyclerTimeSlot.setAdapter(adapter);
        alertDialog.dismiss();
    }
}