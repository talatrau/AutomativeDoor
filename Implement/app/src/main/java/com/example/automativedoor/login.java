package com.example.automativedoor;
import com.example.automativedoor.Control.UserController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import com.google.firebase.database.DataSnapshot;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class login extends AppCompatActivity {

    private UserController controller = UserController.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (controller.fauth.getCurrentUser() != null) {
            controller.setup();
            Toast.makeText(login.this, "Welcome back! " + controller.fauth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), HomePage.class));
            finish();
        }

        TextView forgotpass = (TextView) findViewById(R.id.forgotpass);
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, forgot_pass.class);
                startActivity(intent);
            }
        });

        TextView contact = (TextView) findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactShow();
            }
        });

        EditText emailEt = (EditText) findViewById(R.id.email);
        EditText passEt = (EditText) findViewById(R.id.password);
        Button button = (Button) findViewById(R.id.login);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                button.startAnimation(AnimationUtils.loadAnimation(login.this, R.anim.bounce));
                String email = emailEt.getText().toString();
                String pass = passEt.getText().toString();

                if (email.isEmpty()) {
                    emailEt.setError("Email is Required");
                    return;
                } else if (pass.isEmpty()) {
                    passEt.setError("Password is Required");
                    return;
                }

                controller.fauth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            controller.setup().get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    if (controller.user.getPin() == null) {
                                        startActivity(new Intent(getApplicationContext(), PinCreate.class));
                                        finish();
                                    } else {
                                        Toast.makeText(login.this, "Welcome back! " + email, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), HomePage.class));
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(login.this, "Wrong Email or Password! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void contactShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
        builder.setTitle("Contact us via");
        builder.setMessage("\n Phone: \t 113-114-115 \n \n Email: \n \n \t smarthome.hcmut.k18@gmail.com");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog al = builder.create();
        al.show();
    }

}