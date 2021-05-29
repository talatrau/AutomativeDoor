package com.example.automativedoor;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.automativedoor.Control.UserController;

import java.util.regex.Pattern;


public class forgot_pass extends AppCompatActivity {

    EditText email, pin;
    Button btn_send;
    boolean pinFlag = false, emailFlag = false;

    private void setEvent() {
        email.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Pattern.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", s)) {
                    if (s.length() != 0) email.setError("Invalid EMAIL address!");
                    emailFlag = false;
                }
                else emailFlag = true;

                if (pinFlag && emailFlag) btn_send.setEnabled(true);
                else btn_send.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 6 || !Pattern.matches("[1-9]+", s)) {
                    if (s.length() != 0) pin.setError("Invalid PIN code!");
                    pinFlag = false;
                }
                else pinFlag = true;

                if (pinFlag && emailFlag) btn_send.setEnabled(true);
                else btn_send.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserController.getInstance().forgotPass(email.getText().toString(), pin.getText().toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(forgot_pass.this);
                builder.setMessage("Password had been send to your EMAIL. \n \nIf not please check your EMAIL or PIN.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                email.setText("");
                pin.setText("");
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        email = (EditText) findViewById(R.id.forgot_email);
        pin = (EditText) findViewById(R.id.forgot_pin);
        btn_send = (Button) findViewById(R.id.forgot_send);

        this.setEvent();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);

    }

}