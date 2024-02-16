package com.project1.mycrashgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.material.textview.MaterialTextView;
import com.project1.mycrashgame.Logic.GameManager;
import com.project1.mycrashgame.Model.MyMatrix;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int MOVE_RIGHT = 1;
    private static final int MOVE_LEFT = -1;
    private final int DISTANCE_STATUS = 0;
    private final int BROOM_STATUS = 1;
    private final int CLOUD_VALUE = 1;
    private final int BROOM_VALUE = 2;
    private static final long DELAY = 800;
    private static final long DISTANCE_DELAY = 700;
    private static final long VIBRATION = 500;
    private Timer distance_timer;
    private Timer init_timer;

    private ShapeableImageView main_IMG_background;
    private ExtendedFloatingActionButton main_FAB_left;
    private ExtendedFloatingActionButton main_FAB_right;
    private MaterialTextView main_LBL_score;
    private ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Broom = new ArrayList<>();
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
        initGame();
        startGame();
    }

    private void initGame() {
        findViews();
        initBackground();
        initButtonListeners();
        life = main_IMG_hats.size();
        gameManager = new GameManager(main_All_Layouts_Of_Cloud, main_All_Layouts_Of_Broom, life);
        myMatrix = gameManager.getMyMatrix();
        gameSizes();
        setVisibility();
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

    private void startGame() {
        startRunClouds();
        startOdometer();
    }

    private void startOdometer() {
        distance_timer = new Timer();

        distance_timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    gameManager.setScore(DISTANCE_STATUS);
                    main_LBL_score.setText(gameManager.getScore() + "");
                });
            }
        }, 0, DISTANCE_DELAY);
    }

    private void startRunClouds() {
        init_timer = new Timer();

        init_timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    advanceInMatrix();
                    initNewCloudDown(myMatrix);
                });
            }
        }, 0, DELAY);
    }


    private void advanceInMatrix() {
        int preValue;
        for (int currentCol = 0; currentCol <= numOfColsInCloudMatrix; currentCol++) {
            for (int currentRow = numOfRowsInCloudMatrix; currentRow >= 0; currentRow--) {

                int currentValue = gameManager.getAdvanceMatrix()[currentRow][currentCol];

                if (currentRow == 0) {
                    preValue = 0;
                } else {
                    preValue = gameManager.getAdvanceMatrix()[currentRow - 1][currentCol];
                }

                gameManager.getAdvanceMatrix()[currentRow][currentCol] = preValue;

                if (currentValue == CLOUD_VALUE) {
                    View currentCloud = myMatrix.getLayoutsCloudList().get(currentCol).getChildAt(currentRow);
                    currentCloud.setVisibility(View.INVISIBLE);
                    if (currentRow + 1 <= numOfRowsInCloudMatrix) {
                        View nextCloud = myMatrix.getLayoutsCloudList().get(currentCol).getChildAt(currentRow + 1);
                        nextCloud.setVisibility(View.VISIBLE);
                    } else {
                        checkCrush(currentCol, currentValue);
                    }

                }
                if (currentValue == BROOM_VALUE) {
                    View currentBroom = myMatrix.getLayoutsBroomList().get(currentCol).getChildAt(currentRow);
                    currentBroom.setVisibility(View.INVISIBLE);
                    if (currentRow + 1 <= numOfRowsInCloudMatrix) {
                        View nextBroom = myMatrix.getLayoutsBroomList().get(currentCol).getChildAt(currentRow + 1);
                        nextBroom.setVisibility(View.VISIBLE);
                    } else {
                        checkCrush(currentCol, currentValue);
                    }
                }
            }
        }
    }


    private void initNewCloudDown(MyMatrix myMatrix) {
        int max = myMatrix.getLayoutsCloudList().size() - 1;
        int randomIndex = (int) Math.floor(Math.random() * max * 2);
        if (randomIndex <= max) {
            gameManager.getAdvanceMatrix()[0][randomIndex] = CLOUD_VALUE;
            myMatrix.getLayoutsCloudList().get(randomIndex).getChildAt(0).setVisibility(View.VISIBLE);
        } else {
            myMatrix.getLayoutsBroomList().get(randomIndex % (max + 1)).getChildAt(0).setVisibility(View.VISIBLE);
            gameManager.getAdvanceMatrix()[0][randomIndex % max - 1] = BROOM_VALUE;
        }
    }


    private void checkCrush(int currentCol, int currentValue) {
        int currentWitch = gameManager.getWitchVisibleIndex();

        if (currentCol == currentWitch) {
            if (currentValue == CLOUD_VALUE) {
                updateCrushUI();
            } else {
                gameManager.setScore(BROOM_STATUS);
            }
            vibrate();
            toast(currentValue);
        }
    }

    private void updateCrushUI() {
        int currentNumOfCrush = gameManager.getNumOfCrush();

        gameManager.updateNumOfCrush();
        main_IMG_hats.get(currentNumOfCrush).setVisibility(View.INVISIBLE);

        if (gameManager.isGameOver()) {
            gameOver();
            changeActivity(gameManager.getScore());
        }
    }

    private void changeActivity(int score) {
        Intent recordIntent = new Intent(this, RecordsActivity.class);
        startActivity(recordIntent);
        finish();
    }

    private void toast(int currentValue) {
        if (currentValue == CLOUD_VALUE) {
            if (gameManager.isGameOver()) {
                Toast.makeText(this, getString(R.string.end_game_toast), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.crush_toast), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.broom_toast), Toast.LENGTH_SHORT).show();
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

    private void initBackground() {
        Glide
                .with(this)
                .load(R.drawable.pumpkin)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(main_IMG_background);
    }

    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        main_FAB_left = findViewById(R.id.main_FAB_left);
        main_FAB_right = findViewById(R.id.main_FAB_right);
        main_LBL_score = findViewById(R.id.main_LBL_score);

        main_All_Layouts_Of_Broom.add(findViewById(R.id.main_LAYOUT_broom_0));
        main_All_Layouts_Of_Broom.add(findViewById(R.id.main_LAYOUT_broom_1));
        main_All_Layouts_Of_Broom.add(findViewById(R.id.main_LAYOUT_broom_2));
        main_All_Layouts_Of_Broom.add(findViewById(R.id.main_LAYOUT_broom_3));
        main_All_Layouts_Of_Broom.add(findViewById(R.id.main_LAYOUT_broom_4));

        main_All_Layouts_Of_Cloud.add(findViewById(R.id.main_LAYOUT_cloud_0));
        main_All_Layouts_Of_Cloud.add(findViewById(R.id.main_LAYOUT_cloud_1));
        main_All_Layouts_Of_Cloud.add(findViewById(R.id.main_LAYOUT_cloud_2));
        main_All_Layouts_Of_Cloud.add(findViewById(R.id.main_LAYOUT_cloud_3));
        main_All_Layouts_Of_Cloud.add(findViewById(R.id.main_LAYOUT_cloud_4));

        main_IMG_hats.add(findViewById(R.id.main_IMG_hat0));
        main_IMG_hats.add(findViewById(R.id.main_IMG_hat1));
        main_IMG_hats.add(findViewById(R.id.main_IMG_hat2));

        main_LAYOUT_Of_Witches.add(findViewById(R.id.main_IMG_witch0));
        main_LAYOUT_Of_Witches.add(findViewById(R.id.main_IMG_witch1));
        main_LAYOUT_Of_Witches.add(findViewById(R.id.main_IMG_witch2));
        main_LAYOUT_Of_Witches.add(findViewById(R.id.main_IMG_witch3));
        main_LAYOUT_Of_Witches.add(findViewById(R.id.main_IMG_witch4));

    }

    private void gameSizes() {
        numOfColsInCloudMatrix = gameManager.getNumOfCols() - 1;
        numOfRowsInCloudMatrix = gameManager.getNumOfRows() - 1;
        numOfWitches = main_LAYOUT_Of_Witches.size();
    }


    private void gameOver() {
        init_timer.cancel();
        distance_timer.cancel();
    }
}