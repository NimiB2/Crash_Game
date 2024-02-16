package com.project1.mycrashgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.project1.mycrashgame.Model.StartActivity;

public class RecordsActivity extends AppCompatActivity {

    private ShapeableImageView list_IMG_background;
    private MaterialButton map_BTN_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        findViews();
        initBackground();
        map_BTN_back.setOnClickListener(v->changeToStartActivity());

    }

    private void changeToStartActivity() {
        Intent startIntent = new Intent(this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    private void findViews() {
        list_IMG_background=findViewById(R.id.list_IMG_background);
        map_BTN_back=findViewById(R.id.map_BTN_back);
    }


    private void initBackground() {
        Glide
                .with(this)
                .load(R.drawable.bord2)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(list_IMG_background);
    }

}