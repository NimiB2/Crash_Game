package com.project1.mycrashgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.project1.mycrashgame.Interfaces.Callback_recordClicked;
import com.project1.mycrashgame.Views.ListFragment;
import com.project1.mycrashgame.Views.MapsFragment;

public class RecordsActivity extends AppCompatActivity {
    private FrameLayout record_FRAME_list;
    private FrameLayout record_FRAME_map;

    private ListFragment listFragment;
    private MapsFragment mapsFragment;
    private ShapeableImageView list_IMG_background;
    private MaterialButton map_BTN_back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        findViews();
        initBackground();
        map_BTN_back.setOnClickListener(v -> changeToStartActivity());
        initFragments();
        listFragment.setCallbackRecordClicked(new Callback_recordClicked() {
            @Override
            public void getRecordMap(double lat, double lon) {
                MapsFragment.zoom(lat,lon);
            }
        })



    }


    private void initFragments() {
        listFragment = new ListFragment();
        mapsFragment = new MapsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.record_FRAME_list,listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.record_FRAME_map,mapsFragment).commit();
    }

    private void changeToStartActivity() {
        Intent startIntent = new Intent(this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    private void findViews() {
        list_IMG_background = findViewById(R.id.list_IMG_background);
        map_BTN_back = findViewById(R.id.map_BTN_back);
        record_FRAME_list = findViewById(R.id.record_FRAME_list);
        record_FRAME_map = findViewById(R.id.record_FRAME_map);
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