package com.project1.mycrashgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class StartActivity extends AppCompatActivity {
    private ShapeableImageView start_IMG_background;
   private SwitchCompat start_SWITCH_Sensors;
   private SwitchCompat start_SWITCH_speed;
    private MaterialTextView start_MTV_wellcome;
    private MaterialButton start_BTN_start;
    private MaterialButton start_BTN_record;

    private boolean sensorsMode;
    private boolean speedControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViews();
        initBackground();
        start_BTN_start.setOnClickListener(v -> changetoMainActivity());
        start_BTN_record.setOnClickListener(v -> changeToRecordActivity());
        start_SWITCH_Sensors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sensorsMode=isChecked;
            }
        });
        start_SWITCH_speed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                speedControl=isChecked;
            }
        });
    }

    private void changeToRecordActivity() {
        Intent recordIntent = new Intent(this, RecordsActivity.class);
        recordIntent.putExtra(getString(R.string.sensorsmode), sensorsMode);
        recordIntent.putExtra(getString(R.string.speedcontrol), speedControl);
        startActivity(recordIntent);
    }

    private void changetoMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra(getString(R.string.sensorsmode), sensorsMode);
        mainIntent.putExtra(getString(R.string.speedcontrol), speedControl);
        startActivity(mainIntent);
        finish();
    }


    private void findViews() {
        start_IMG_background = findViewById(R.id.start_IMG_background);
        start_MTV_wellcome = findViewById(R.id.start_MTV_welcome);
        start_BTN_start = findViewById(R.id.start_BTN_start);
        start_BTN_record = findViewById(R.id.start_BTN_record);
        start_SWITCH_Sensors=findViewById(R.id.start_SWITCH_Sensors);
        start_SWITCH_speed= findViewById(R.id.start_SWITCH_speed);

    }


    private void initBackground() {
        Glide
                .with(this)
                .load(R.drawable.opening_background)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(start_IMG_background);
    }
}