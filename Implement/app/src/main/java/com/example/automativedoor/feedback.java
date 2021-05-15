package com.example.automativedoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.automativedoor.Control.UserController;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class feedback extends AppCompatActivity {
    Button btnSend;
    EditText edtContent;
    RatingBar rtScore;
    FirebaseDatabase database;
    UserController user1 = UserController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnSend = (Button) findViewById(R.id.btnSend);
        edtContent = (EditText) findViewById(R.id.edtContent);
        rtScore = (RatingBar) findViewById(R.id.rtScore);

        database = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onStart(){
        super.onStart();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponse(edtContent.getText().toString(), (int) rtScore.getRating());
            }
        });
    }

    void sendResponse(String content, int star){
        user1.sendResponse(star, content);
        startActivity(new Intent(getApplicationContext(), HomePage.class));
        finish();
    }


}