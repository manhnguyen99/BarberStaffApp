package com.example.barberstaffapp.Common;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.barberstaffapp.Interface.IDialogClickListener;
import com.example.barberstaffapp.R;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomLoginDialog {

   @BindView(R.id.txtTitle)
   TextView txtTitle;
   TextInputEditText edtPassword;
   @BindView(R.id.btnLogin)
   Button btnLogin;
   @BindView(R.id.btnCancel)
   Button btnCancel;

   public  static  CustomLoginDialog mDialog;
   public IDialogClickListener iDialogClickListener;

   public static CustomLoginDialog getInstance() {
      if(mDialog == null)
         mDialog = new CustomLoginDialog();
      return mDialog;
   }
   public void showLoginDialog(String title,
                               String positiveText,
                               String negativeText,
                               Context context,
                               IDialogClickListener iDialogClickListener)
   {
      this.iDialogClickListener = iDialogClickListener;
      Dialog dialog = new Dialog(context);
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      dialog.setContentView(R.layout.layout_login_regestration);
      ButterKnife.bind(this,dialog);
      //set title
      if(!TextUtils.isEmpty(title))
      {
         txtTitle.setText(title);
         txtTitle.setVisibility(View.VISIBLE);
      }
      btnLogin.setText(positiveText);
      btnCancel.setText(negativeText);

      dialog.setCancelable(false);
      dialog.show();

      Window window = dialog.getWindow();
      window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

      btnLogin.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            iDialogClickListener.onClickPositiveButton(dialog);
         }
      });
      btnCancel.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            iDialogClickListener.onClickNegativeButton(dialog);
         }
      });
   }
}
