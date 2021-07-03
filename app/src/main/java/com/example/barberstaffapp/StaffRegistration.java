package com.example.barberstaffapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.barberstaffapp.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class StaffRegistration extends AppCompatActivity {
    TextInputLayout txtFirstname, txtLastname, txtEmail, txtPassword, txtConfirmpass, txtPhonenum;
    Button btnSignup, btnEmail, btnPhonenum;
    CountryCodePicker Cpp;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;

    String fname;
    String lname;
    String emailid;
    String mobile;
    String password;
    String cfpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_registration);
        txtFirstname = (TextInputLayout) findViewById(R.id.edtFirstName);
        txtLastname = (TextInputLayout) findViewById(R.id.edtLastName);
        txtEmail = (TextInputLayout) findViewById(R.id.edtEmail);
        txtPassword = (TextInputLayout) findViewById(R.id.edtPass);
        txtConfirmpass = (TextInputLayout) findViewById(R.id.edtCpass);
        txtPhonenum = (TextInputLayout) findViewById(R.id.edtPhoneNumber);

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnEmail = (Button) findViewById(R.id.btnEmail);
        btnPhonenum = (Button) findViewById(R.id.btnPhoneNumber);
        Cpp = (CountryCodePicker) findViewById(R.id.CountryCode);

        FAuth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fname = txtFirstname.getEditText().getText().toString().trim();
                lname = txtLastname.getEditText().getText().toString().trim();
                emailid = txtEmail.getEditText().getText().toString().trim();
                mobile = txtPhonenum.getEditText().getText().toString().trim();
                password = txtPassword.getEditText().getText().toString().trim();
                cfpass = txtConfirmpass.getEditText().getText().toString().trim();
                if (isValid()) {

                    final ProgressDialog mDialog = new ProgressDialog(StaffRegistration.this);
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Registering please wait...");
                    mDialog.show();

                    FAuth.createUserWithEmailAndPassword(emailid, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                databaseReference = FirebaseDatabase.getInstance().getReference("Salon").child(Common.state_name).child("Branch").child(Common.selected_Salon.getSalonID()).child("Baber");
                                        HashMap<String, String> hashMap= new HashMap<>();
                                        hashMap.put("nameBaber", fname);
                                        hashMap.put("lastname", lname);
                                        hashMap.put("email", emailid);
                                        hashMap.put("phone", mobile);
                                        hashMap.put("password", password);
                                        databaseReference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mDialog.dismiss();
                                                FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(StaffRegistration.this);
                                                            builder.setMessage("Registered Successfully,Please Verify your Email");
                                                            builder.setCancelable(false);
                                                            builder.setPositiveButton("OK", (dialog, which) -> {
                                                                dialog.dismiss();
                                                                String phonenumber = Cpp.getSelectedCountryCodeWithPlus() + mobile;
                                                                Intent b = new Intent(StaffRegistration.this, StaffVerifyPhone.class);
                                                                b.putExtra("phonenumber", phonenumber);
                                                                startActivity(b);
                                                            });
                                                            AlertDialog alert = builder.create();
                                                            alert.show();

                                                        } else {
                                                            mDialog.dismiss();
                                                            ReusableCodeForAll.ShowAlert(StaffRegistration.this, "Error", task.getException().getMessage());
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                            else {
                                mDialog.dismiss();
                                ReusableCodeForAll.ShowAlert(StaffRegistration.this, "Error", task.getException().getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public boolean isValid() {
        txtEmail.setErrorEnabled(false);
        txtEmail.setError("");
        txtFirstname.setErrorEnabled(false);
        txtFirstname.setError("");
        txtLastname.setErrorEnabled(false);
        txtLastname.setError("");
        txtPassword.setErrorEnabled(false);
        txtPassword.setError("");
        txtPhonenum.setErrorEnabled(false);
        txtPhonenum.setError("");
        txtConfirmpass.setErrorEnabled(false);
        txtConfirmpass.setError("");

        boolean isValidname = false, isValidemail = false, isvalidpassword = false, isvalidconfirmpassword = false, isvalid = false, isvalidmobileno = false, isvalidlname = false;
        if (TextUtils.isEmpty(fname)) {
            txtFirstname.setErrorEnabled(true);
            txtFirstname.setError("Firstname is required");
        } else {
            isValidname = true;
        }
        if (TextUtils.isEmpty(lname)) {
            txtLastname.setErrorEnabled(true);
            txtLastname.setError("Lastname is required");
        } else {
            isvalidlname = true;
        }
        if (TextUtils.isEmpty(emailid)) {
            txtEmail.setErrorEnabled(true);
            txtEmail.setError("Email is required");
        } else {
            if (emailid.matches(emailpattern)) {
                isValidemail = true;
            } else {
                txtEmail.setErrorEnabled(true);
                txtEmail.setError("Enter a valid Email Address");
            }

        }
        if (TextUtils.isEmpty(password)) {
            txtPassword.setErrorEnabled(true);
            txtPassword.setError("Password is required");
        } else {
            if (password.length() < 6) {
                txtPassword.setErrorEnabled(true);
                txtPassword.setError("password too weak");
            } else {
                isvalidpassword = true;
            }
        }
        if (TextUtils.isEmpty(cfpass)) {
            txtConfirmpass.setErrorEnabled(true);
            txtConfirmpass.setError("Confirm Password is required");
        } else {
            if (!password.equals(cfpass)) {
                txtConfirmpass.setErrorEnabled(true);
                txtConfirmpass.setError("Password doesn't match");
            } else {
                isvalidconfirmpassword = true;
            }
        }
        if (TextUtils.isEmpty(mobile)) {
            txtPhonenum.setErrorEnabled(true);
            txtPhonenum.setError("Mobile number is required");
        } else {
            if (mobile.length() < 10) {
                txtPhonenum.setErrorEnabled(true);
                txtPhonenum.setError("Invalid mobile number");
            } else {
                isvalidmobileno = true;
            }
        }

        isvalid = (isValidname && isvalidlname && isValidemail && isvalidconfirmpassword && isvalidpassword && isvalidmobileno) ? true : false;
        return isvalid;
    }
}