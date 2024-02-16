package com.project1.mycrashgame.Model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.project1.mycrashgame.MainActivity;
import com.project1.mycrashgame.R;
import com.project1.mycrashgame.RecordsActivity;

public class StartActivity extends AppCompatActivity {
    private ShapeableImageView start_IMG_background;
    private MaterialTextView start_MTV_wellcome;
    private MaterialButton start_BTN_start;
    private MaterialButton start_BTN_record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViews();
        initBackground();
        start_BTN_start.setOnClickListener(v->changetoMainActivity());
        start_BTN_record.setOnClickListener(v->changeToRecordActivity());
    }

    private void changeToRecordActivity() {
        Intent recordIntent = new Intent(this, RecordsActivity.class);
        startActivity(recordIntent);
    }

    private void changetoMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void findViews() {
        start_IMG_background=findViewById(R.id.start_IMG_background);
        start_MTV_wellcome=findViewById(R.id.start_MTV_wellcome);
        start_BTN_start=findViewById(R.id.start_BTN_start);
        start_BTN_record=findViewById(R.id.start_BTN_record);
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