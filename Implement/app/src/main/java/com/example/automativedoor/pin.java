package com.example.automativedoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.automativedoor.Control.UserController;

public class pin extends AppCompatActivity {

    public static final String PIN_RESULT = "PIN_RESULT";

    UserController controller = UserController.getInstance();
    TextView txt_1, txt_2, txt_3, txt_4, txt_5, txt_6;
    ImageView del;
    Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9;

    private String pinCode = "";
    private int count = 0;

    private void setWidgetInstance() {
        btn_1 = (Button) findViewById(R.id.pin_1);
        btn_2 = (Button) findViewById(R.id.pin_2);
        btn_3 = (Button) findViewById(R.id.pin_3);
        btn_4 = (Button) findViewById(R.id.pin_4);
        btn_5 = (Button) findViewById(R.id.pin_5);
        btn_6 = (Button) findViewById(R.id.pin_6);
        btn_7 = (Button) findViewById(R.id.pin_7);
        btn_8 = (Button) findViewById(R.id.pin_8);
        btn_9 = (Button) findViewById(R.id.pin_9);

        txt_1 = (TextView) findViewById(R.id.pin_txt_1);
        txt_2 = (TextView) findViewById(R.id.pin_txt_2);
        txt_3 = (TextView) findViewById(R.id.pin_txt_3);
        txt_4 = (TextView) findViewById(R.id.pin_txt_4);
        txt_5 = (TextView) findViewById(R.id.pin_txt_5);
        txt_6 = (TextView) findViewById(R.id.pin_txt_6);
        del = (ImageView) findViewById(R.id.pin_del);
    }

    private void setButtonOnclick() {
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCode += "1";
                setTextView(true);
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCode += "2";
                setTextView(true);
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCode += "3";
                setTextView(true);
            }
        });

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCode += "4";
                setTextView(true);
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCode += "5";
                setTextView(true);
            }
        });

        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCode += "6";
                setTextView(true);
            }
        });

        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCode += "7";
                setTextView(true);
            }
        });

        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCode += "8";
                setTextView(true);
            }
        });

        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCode += "9";
                setTextView(true);
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinCode.length() > 0) {
                    setTextView(false);
                    pinCode = pinCode.substring(0, pinCode.length() - 1);
                }
            }
        });
    }

    private void setTextView(boolean insert) {
        if (pinCode.length() == 1) {
            if (insert) txt_1.setText("*");
            else txt_1.setText("");
        }
        else if (pinCode.length() == 2) {
            if (insert) txt_2.setText("*");
            else txt_2.setText("");
        }
        else if (pinCode.length() == 3) {
            if (insert) txt_3.setText("*");
            else txt_3.setText("");
        }
        else if (pinCode.length() == 4) {
            if (insert) txt_4.setText("*");
            else txt_4.setText("");
        }
        else if (pinCode.length() == 5) {
            if (insert) txt_5.setText("*");
            else txt_5.setText("");
        }
        else if (pinCode.length() == 6) {
            if (insert) txt_6.setText("*");
            else txt_6.setText("");
        }
        pinCodeVerify();
    }

    private void pinCodeVerify() {
        if (pinCode.length() == 6) {
            if (this.controller.user.pinVerify(this.pinCode)) {
                Intent data = new Intent();
                data.putExtra(PIN_RESULT, "pin correct");
                setResult(AppCompatActivity.RESULT_OK, data);
                finish();
            }
            else {
                count += 1;
                if (count == 5) {
                    Intent data = new Intent();
                    data.putExtra(PIN_RESULT, "disable");
                    setResult(AppCompatActivity.RESULT_OK, data);
                    finish();
                }
                txt_1.setText("");
                txt_2.setText("");
                txt_3.setText("");
                txt_4.setText("");
                txt_5.setText("");
                txt_6.setText("");
                pinCode = "";
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        setWidgetInstance();
        setButtonOnclick();

    }

    @Override
    public void onBackPressed() {
        setResult(AppCompatActivity.RESULT_CANCELED);
        super.onBackPressed();
    }
}