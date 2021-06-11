package com.example.automativedoor;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.automativedoor.Control.UserController;

public class feedback extends AppCompatActivity {
    Button btnSend;
    EditText edtContent;
    RatingBar rtScore;
    UserController user = UserController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnSend = (Button) findViewById(R.id.btnSend);
        edtContent = (EditText) findViewById(R.id.edtContent);
        rtScore = (RatingBar) findViewById(R.id.rtScore);
    }

    @Override
    protected void onStart(){
        super.onStart();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = sendResponse(edtContent.getText().toString(), (int) rtScore.getRating());
                if (result) setResult(AppCompatActivity.RESULT_OK);
                else setResult(AppCompatActivity.RESULT_CANCELED);
                finish();
            }
        });
    }

    boolean sendResponse(String content, int star){
        return user.sendResponse(star, content);
    }

    @Override
    public void onBackPressed() {
        setResult(AppCompatActivity.RESULT_OK);
        super.onBackPressed();
    }

}