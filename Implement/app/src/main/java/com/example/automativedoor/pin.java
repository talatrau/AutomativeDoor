package com.example.automativedoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.EntityClass.Account;
import com.hanks.passcodeview.PasscodeView;

public class pin extends AppCompatActivity {

    PasscodeView pinView;
    UserController user1 = UserController.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        user1.setup();
        String s = user1.user.getPin();

        pinView = (PasscodeView) findViewById(R.id.pinView);
        pinView.setPasscodeLength(6)
        .setLocalPasscode(s)
        .setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {
                //Nếu sai
                Toast.makeText(getApplicationContext(), "Pin is wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String number) {
                //Nếu đúng
                startActivity(new Intent(getApplicationContext(), HomePage.class));
                finish();
            }
        });


    }
}