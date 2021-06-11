package com.example.automativedoor;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.automativedoor.Control.UserController;
import com.example.automativedoor.EntityClass.Speaker;

public class SpeakerView extends AppCompatActivity {

    TextView textView;
    ImageView imageView;
    SeekBar seekBar;
    Button button;

    private Speaker speaker = null;
    private UserController controller = UserController.getInstance();
    private int volume;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_speaker);

        speaker = controller.speakerList.get(0);
        textView = findViewById(R.id.speaker_name);
        imageView = findViewById(R.id.stream_speaker_img);
        seekBar = findViewById(R.id.speaker_seekbar);
        button = findViewById(R.id.speaker_confirm);
        volume = speaker.getVolume();

        this.setUpEvent();
        this.setFinishOnTouchOutside(false);
    }

    private void setUpEvent() {
        textView.setText(speaker.getName());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SpeakerView.this);
                final EditText input = new EditText(getApplicationContext());
                input.setHint("   Change speaker name");
                input.setTextColor(Color.parseColor("#FFFFFFFF"));
                builder.setView(input);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = input.getText().toString();
                        if (!text.isEmpty()) {
                            speaker.updateName(text);
                            textView.setText(text);
                        }
                    }
                });
                builder.create().show();
            }
        });

        seekBar.setProgress(volume);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                volume = progressChangedValue;
                imageView.setVisibility(View.INVISIBLE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(AppCompatActivity.RESULT_OK);
                controller.setSpeaker(0, volume);
                finish();
            }
        });
    }
}
