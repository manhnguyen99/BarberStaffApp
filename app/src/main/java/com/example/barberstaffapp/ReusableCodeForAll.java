package com.example.barberstaffapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ReusableCodeForAll {
    public static void ShowAlert(Context context, String tittle, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setTitle(tittle).setMessage(message).show();
    }
}
