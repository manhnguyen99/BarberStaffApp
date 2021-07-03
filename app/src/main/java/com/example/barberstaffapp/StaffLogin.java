package com.example.barberstaffapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barberstaffapp.Common.Common;
import com.example.barberstaffapp.Model.Baber;
import com.example.barberstaffapp.Model.Salon;
import com.example.barberstaffapp.Model.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class StaffLogin extends AppCompatActivity {
    TextInputLayout edtEmail, edtPass;
    Button btnSigin, btnSiginphone;
    TextView txtForgotPassword, txtCreateAccount;
    String emailid, password;
    FirebaseAuth firebaseAuth;
    DatabaseReference baberRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login2);
        try {
            edtEmail = findViewById(R.id.edtEmail);
            edtPass = findViewById(R.id.edtPassword);
            btnSigin = findViewById(R.id.btnLogin);
            btnSiginphone  = findViewById(R.id.btnLoginWithPhone);
            txtForgotPassword = findViewById(R.id.txtForgotpass);
            txtCreateAccount = findViewById(R.id.txtCreateAccount);

            firebaseAuth = FirebaseAuth.getInstance();

            btnSigin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emailid = edtEmail.getEditText().getText().toString().trim();
                    password = edtPass.getEditText().getText().toString().trim();
                    if(isValid()){
                        final ProgressDialog mDialog = new ProgressDialog(StaffLogin.this);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setCancelable(false);
                        mDialog.setMessage("Sign In Please Wait...");
                        mDialog.show();

                        firebaseAuth.signInWithEmailAndPassword(emailid,password).addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                mDialog.dismiss();
                                if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                                    mDialog.dismiss();

                                    //test getBaber
                                     DatabaseReference barberRef = FirebaseDatabase.getInstance()
                                            .getReference("Salon")
                                            .child(Common.state_name)
                                            .child("Branch")
                                            .child(Common.selected_Salon.getSalonID())
                                            .child("Baber");
                                    //get Informationn of this barber
                                    barberRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {

                                            if (task.isSuccessful()) {
                                                Baber baber = new Baber();
                                                DataSnapshot dataSnapshot = task.getResult();
                                                if (dataSnapshot.exists()) {
                                                   baber = dataSnapshot.getValue(Baber.class);
                                                   baber.setBaberId(dataSnapshot.getKey());
                                                    Toast.makeText(StaffLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(StaffLogin.this, StaffHomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }
                                    });


                                }
                                else
                                {
                                    ReusableCodeForAll.ShowAlert(StaffLogin.this,"Verification Failed","You Have Not Verified Your Email!!!");
                                }
                            }
                            else
                            {
                                mDialog.dismiss();
                                ReusableCodeForAll.ShowAlert(StaffLogin.this, "Error", task.getException().getMessage());
                            }
                        });
                    }
                }
            });
            txtCreateAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(StaffLogin.this, StaffRegistration.class ));
                    finish();
                }
            });
            btnSiginphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(StaffLogin.this, StaffPhone.class));
                    finish();
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValid() {

        edtEmail.setErrorEnabled(false);
        edtEmail.setError("");
        edtPass.setErrorEnabled(false);
        edtPass.setError("");

        boolean isvalid = false, isvalidemail = false, isvalidpassword = false;
        if (TextUtils.isEmpty(emailid))
        {
            edtEmail.setErrorEnabled(true);
            edtEmail.setError("Email is required");
        }
        else
        {
            if(emailid.matches(emailpattern)){ // nếu email hợp lệ
                isvalidemail =  true;
            }
            else
            {
                edtEmail.setErrorEnabled(true);
                edtEmail.setError("Invalid Email Address"); //email khonmg hợp lệ -> báo lỗi
            }
        }
        if(TextUtils.isEmpty(password)){
            edtPass.setErrorEnabled(true);
            edtPass.setError("Password is required");
        }
        else
        {
            isvalidpassword = true;
        }
        isvalid = (isvalidemail && isvalidpassword )? true : false;
        return isvalid;
    }
}