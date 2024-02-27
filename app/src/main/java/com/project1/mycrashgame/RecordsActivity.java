package com.project1.mycrashgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.project1.mycrashgame.Views.ListFragment;
import com.project1.mycrashgame.Views.MapsFragment;

public class RecordsActivity extends AppCompatActivity {
    private FrameLayout record_FRAME_list;
    private FrameLayout record_FRAME_map;
    private ListFragment listFragment;
    private MapsFragment mapsFragment;
    private ShapeableImageView frameList_IMG_background;
    private MaterialButton map_BTN_back;
    private MaterialButton map_BTN_play_again;

    private boolean sensorsMode;
    private boolean speedControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        findViews();
        initFragments();
        initBackground();
        map_BTN_back.setOnClickListener(v -> changeToStartActivity());
        map_BTN_play_again.setOnClickListener(v -> changeToMainActivity());

        Intent intent = getIntent();
        sensorsMode = intent.getBooleanExtra(getString(R.string.sensorsmode), false);
        speedControl = intent.getBooleanExtra(getString(R.string.speedcontrol), false);

    }


    private void initFragments() {
        listFragment = new ListFragment();
        mapsFragment = new MapsFragment();
        listFragment.setCallback((lat, lon, playerName) -> mapsFragment.zoom(lat, lon, playerName));
        getSupportFragmentManager().beginTransaction().add(R.id.record_FRAME_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.record_FRAME_map, mapsFragment).commit();
    }


    private void changeToStartActivity() {
        Intent startIntent = new Intent(this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    private void changeToMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra(getString(R.string.sensorsmode), sensorsMode);
        mainIntent.putExtra(getString(R.string.speedcontrol), speedControl);
        startActivity(mainIntent);
        finish();
    }

    private void findViews() {
        frameList_IMG_background = findViewById(R.id.frameList_IMG_background);
        map_BTN_back = findViewById(R.id.map_BTN_back);
        map_BTN_play_again = findViewById(R.id.map_BTN_play_again);
        record_FRAME_list = findViewById(R.id.record_FRAME_list);
        record_FRAME_map = findViewById(R.id.record_FRAME_map);
    }


    private void initBackground() {
        Glide
                .with(this)
                .load(R.drawable.bg)
                .placeholder(R.drawable.ic_launcher_background)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(frameList_IMG_background);

    }
}