package com.example.automativedoor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automativedoor.Control.UserController;
import com.hsalf.smilerating.SmileRating;

import java.util.ArrayList;
import java.util.List;

public class Feedback extends AppCompatActivity {
    Button btnSend;
    EditText edtContent;
    SmileRating rtScore;
    Spinner spinner;
    LinearLayout tks;
    LinearLayout spam;

    private int score;
    private String text;
    private String category;
    private UserController user = UserController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadeout, R.anim.fadein);
        setContentView(R.layout.activity_feedback);

        btnSend = findViewById(R.id.feedback_send);
        edtContent = findViewById(R.id.feedback_text);
        rtScore = findViewById(R.id.feedback_rating);
        spinner = findViewById(R.id.feedback_type);
        tks = findViewById(R.id.feedback_tks);
        spam = findViewById(R.id.feedback_uhoh);

        ArrayList<SpinnerItem> spinnerItems = new ArrayList<>();
        spinnerItems.add(new SpinnerItem("Product", R.drawable.spinner_product));
        spinnerItems.add(new SpinnerItem("Maintain Service", R.drawable.spinner_maintain));
        spinnerItems.add(new SpinnerItem("Mobile App", R.drawable.spinner_mobile));
        SpinnerAdapter adapter = new SpinnerAdapter(this, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        setResult(AppCompatActivity.RESULT_OK);
    }

    @Override
    protected void onStart(){
        super.onStart();
        btnSend.setEnabled(false);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtContent.getText().toString().isEmpty()) text = "No more detail";
                else text = edtContent.getText().toString();
                boolean result = user.sendResponse(score, text, category);
                if (!result) {
                    setResult(AppCompatActivity.RESULT_CANCELED);
                    spam.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);
                }
                else {
                    tks.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);
                }
            }
        });

        rtScore.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
                if (level == 0) btnSend.setEnabled(false);
                else {
                    btnSend.setEnabled(true);
                    score = level;
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) category = "Product";
                else if (position == 1) category = "Maintain";
                else if (position == 2) category = "App";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fadeout, R.anim.fadein);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    class SpinnerItem {
        public String spinnerText;
        public int spinnerImage;

        public SpinnerItem(String text, int img) {
            this.spinnerText = text;
            this.spinnerImage = img;
        }

    }

    class SpinnerAdapter extends ArrayAdapter<SpinnerItem> {

        public SpinnerAdapter(@NonNull Context context, ArrayList<SpinnerItem> list) {
            super(context, 0, list);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return customView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return customView(position, convertView, parent);
        }

        public View customView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.stream_feedback_spinner, parent, false);
            }
            SpinnerItem item = getItem(position);
            ImageView img = convertView.findViewById(R.id.spinner_img);
            TextView text = convertView.findViewById(R.id.spinner_text);

            if (item != null) {
                img.setImageResource(item.spinnerImage);
                text.setText(item.spinnerText);
            }

            return convertView;
        }
    }

}