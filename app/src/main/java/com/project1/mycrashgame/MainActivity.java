package com.project1.mycrashgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.project1.mycrashgame.Logic.GameManager;
import com.project1.mycrashgame.Model.MyMatrix;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int MOVE_RIGHT = 1;
    private static final int MOVE_LEFT = -1;
    private static final long DELAY = 1000;
    private static final long VIBRATION = 500;
    private final Handler initHandler = new Handler();
    private Runnable initRunnable;
    private ShapeableImageView main_IMG_background;
    private ExtendedFloatingActionButton main_FAB_left;
    private ExtendedFloatingActionButton main_FAB_right;
    private ArrayList<ShapeableImageView> main_IMG_hats = new ArrayList<>();
    private ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Cloud = new ArrayList<>();
    private ArrayList<ShapeableImageView> main_LAYOUT_Of_Witches = new ArrayList<>();

    private GameManager gameManager;
    private MyMatrix myMatrix;
    private int numOfWitches;
    private int life;
    private int numOfRowsInCloudMatrix;
    private int numOfColsInCloudMatrix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initBackground();
        initButtonListeners();
        startGame();
    }


    @Override
    protected void onStop() {
        super.onStop();
        gameOver();
    }
    protected void onDestroy() {
        super.onDestroy();
        gameOver();
    }

    private void initBackground() {
        Glide
                .with(this)
                .load(R.drawable.pumpkin)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(main_IMG_background);
    }

    private void startGame() {
        life = main_IMG_hats.size();
        gameManager = new GameManager(main_All_Layouts_Of_Cloud, life);
        myMatrix = gameManager.getMyMatrix();
        gameSizes();
        setVisibility();
        startRunClouds();
    }

    private void gameSizes() {
        numOfColsInCloudMatrix = gameManager.getNumOfCols() - 1;
        numOfRowsInCloudMatrix = gameManager.getNumOfRows() - 1;
        numOfWitches = main_LAYOUT_Of_Witches.size();
    }


    private void startRunClouds() {
        initRunnable = new Runnable() {
            @Override
            public void run() {
                advanceClouds();
                initNewCloudDown(myMatrix);
                initHandler.postDelayed(this, DELAY);
            }
        };
        initHandler.postDelayed(initRunnable, 0);
    }


    private void advanceClouds() {
        for (int currentCol = 0; currentCol <= numOfColsInCloudMatrix; currentCol++) {
            for (int currentRow = numOfRowsInCloudMatrix; currentRow >= 0; currentRow--) {

                View currentCloud = myMatrix.getLayoutsCloudList().get(currentCol).getChildAt(currentRow);
                View nextCloud = myMatrix.getLayoutsCloudList().get(currentCol).getChildAt(currentRow + 1);

                if ((currentRow == numOfRowsInCloudMatrix) && (currentCloud.getVisibility() == View.VISIBLE)) {
                    checkCrush(currentCloud, currentCol);

                } else if (currentCloud.getVisibility() == View.VISIBLE) {
                    currentCloud.setVisibility(View.INVISIBLE);
                    nextCloud.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void initNewCloudDown(MyMatrix myMatrix) {
        int max = myMatrix.getLayoutsCloudList().size();
        int randomIndex = (int) Math.floor(Math.random() * max);

        myMatrix.getLayoutsCloudList().get(randomIndex).getChildAt(0).setVisibility(View.VISIBLE);
    }


    private void checkCrush(View currentCloud, int currentCol) {
        int currentWitch = gameManager.getWitchVisibleIndex();
        currentCloud.setVisibility(View.INVISIBLE);
        if (main_LAYOUT_Of_Witches.get(currentCol).getVisibility() == View.VISIBLE) {
            updateCrushUI();
        }
    }

    private void updateCrushUI() {
        int currentNumOfCrush=gameManager.getNumOfCrush();
        int currentHatIndex = life - currentNumOfCrush;

        vibrate();
        toast(currentNumOfCrush);

        if (currentHatIndex > 0) {
            main_IMG_hats.get(currentNumOfCrush).setVisibility(View.INVISIBLE);
        }
        gameManager.updateNumOfCrush();
    }

    private void toast(int currentNumOfCrush) {
        if ((currentNumOfCrush+1) == life) {
            Toast.makeText(this, getString(R.string.end_game_toast), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, getString(R.string.crush_toast), Toast.LENGTH_SHORT).show();
        }
    }


    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(VIBRATION, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(VIBRATION);
        }
    }


    private void initButtonListeners() {
        main_FAB_left.setOnClickListener(v -> checkWitch(getString(R.string.left)));
        main_FAB_right.setOnClickListener(v -> checkWitch(getString(R.string.right)));
    }

    private void checkWitch(String direction) {
        int witchVisibleIndex = gameManager.getWitchVisibleIndex();

        if (direction.equals(getString(R.string.right))) {
            moveWitch(MOVE_RIGHT, witchVisibleIndex);

        } else moveWitch(MOVE_LEFT, witchVisibleIndex);

    }

    private void moveWitch(int direction, int witchVisibleIndex) {
        int newVisibleIndex = witchVisibleIndex + direction;

        if (newVisibleIndex >= 0 && newVisibleIndex < main_LAYOUT_Of_Witches.size()) {
            main_LAYOUT_Of_Witches.get(witchVisibleIndex).setVisibility(View.INVISIBLE);
            main_LAYOUT_Of_Witches.get(newVisibleIndex).setVisibility(View.VISIBLE);
            gameManager.setWitchVisibleIndex(newVisibleIndex);
        }
    }

    private void setVisibility() {
        int witchVisibleIndex = gameManager.getWitchVisibleIndex();

        for (int k = 0; k < numOfWitches; k++) {
            main_LAYOUT_Of_Witches.get(k).setVisibility(View.INVISIBLE);
        }
        main_LAYOUT_Of_Witches.get(witchVisibleIndex).setVisibility(View.VISIBLE);
    }

    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        main_FAB_left = findViewById(R.id.main_FAB_left);
        main_FAB_right = findViewById(R.id.main_FAB_right);

        main_All_Layouts_Of_Cloud.add(findViewById(R.id.main_LAYOUT_cloud_0));
        main_All_Layouts_Of_Cloud.add(findViewById(R.id.main_LAYOUT_cloud_1));
        main_All_Layouts_Of_Cloud.add(findViewById(R.id.main_LAYOUT_cloud_2));

        main_IMG_hats.add(findViewById(R.id.main_IMG_hat0));
        main_IMG_hats.add(findViewById(R.id.main_IMG_hat1));
        main_IMG_hats.add(findViewById(R.id.main_IMG_hat2));

        main_LAYOUT_Of_Witches.add(findViewById(R.id.main_IMG_witch0));
        main_LAYOUT_Of_Witches.add(findViewById(R.id.main_IMG_witch1));
        main_LAYOUT_Of_Witches.add(findViewById(R.id.main_IMG_witch2));

    }

    private void gameOver() {
        initHandler.removeCallbacks(initRunnable);
    }
}