package com.example.barberstaffapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class StaffVerifyPhone extends AppCompatActivity {
    String verificationId;
    FirebaseAuth FAuth;
    Button btnVerify, btnResendOTP;
    TextView txt;
    EditText edtEnterOTP;
    String phoneNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_verify_phone);
        phoneNum = getIntent().getStringExtra("phonenumber").trim();
        sendverifycaptioncode(phoneNum);
        edtEnterOTP = findViewById(R.id.edtPhoneOTP);
        txt=  findViewById(R.id.text);
        btnVerify = findViewById(R.id.btnVerifyOTP);
        btnResendOTP = findViewById(R.id.btnResendOTP);
        FAuth = FirebaseAuth.getInstance();
        btnResendOTP.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);
        btnVerify.setOnClickListener(v -> {
            String code = edtEnterOTP.getText().toString().trim();
            btnResendOTP.setVisibility(View.INVISIBLE);

            if (code.isEmpty() && code.length() < 6){
                edtEnterOTP.setError("Enter code");
                edtEnterOTP.requestFocus();
                return;
            }
            verifycode(code);
        });
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txt.setVisibility(View.INVISIBLE);
                txt.setText("Resend Code Within" + millisUntilFinished / 1000 + "Seconds");
            }

            @Override
            public void onFinish() {
                btnResendOTP.setVisibility(View.INVISIBLE);
                txt.setVisibility(View.INVISIBLE);
            }
        }.start();
        btnResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnResendOTP.setVisibility(View.INVISIBLE);
                ResendOTP(phoneNum);

                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        txt.setVisibility(View.INVISIBLE);
                        txt.setText("Resend Code Within" + millisUntilFinished/1000 + "Seconds");
                    }

                    @Override
                    public void onFinish() {
                        btnResendOTP.setVisibility(View.INVISIBLE);
                        txt.setVisibility(View.INVISIBLE);
                    }
                }.start();
            }
        });
    }

    private void ResendOTP(String phone) {
        sendverifycaptioncode(phone);
    }

    private void sendverifycaptioncode(String number) {
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setActivity(this)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(mCallBack)
                        .build());
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                edtEnterOTP.setText(code); //auto Verify
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
            Toast.makeText(StaffVerifyPhone.this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken){
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }
    };

    private void verifycode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        linkCredential(credential);
    }

    private void linkCredential(PhoneAuthCredential credential) {
        FAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(StaffVerifyPhone.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(StaffVerifyPhone.this, StaffHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            ReusableCodeForAll.ShowAlert(StaffVerifyPhone.this, "Error", task.getException().getMessage());
                        }
                    }
                });
    }
}