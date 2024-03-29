package com.project1.mycrashgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.project1.mycrashgame.Interfaces.Callback_Sensors;
import com.project1.mycrashgame.Interfaces.Callback_Speed;
import com.project1.mycrashgame.Interfaces.Callback_Timer;
import com.project1.mycrashgame.Logic.GameManager;
import com.project1.mycrashgame.DataBase.DataBase;
import com.project1.mycrashgame.Model.MyMatrix;
import com.project1.mycrashgame.Utilities.MySignal;
import com.project1.mycrashgame.Utilities.MyTImer;

import com.google.android.gms.location.LocationServices;
import com.project1.mycrashgame.Utilities.StepDetector;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final long VIBRATION = 500;

    private static final int SLOW = 900;
    private static final int FAST = 500;


    private static final int MOVE_RIGHT = 1;
    private static final int MOVE_LEFT = -1;

    private final int DISTANCE_STATUS = 0;
    private final int cauldron_STATUS = 1;
    private final int CLOUD_VALUE = 1;
    private final int CAULDRON_VALUE = 2;
    private final int LOCATION_VALUE = 3;


    private ArrayList<LinearLayoutCompat> main_All_Layouts_Of_cauldron = new ArrayList<>();
    private ArrayList<ShapeableImageView> main_IMG_hats = new ArrayList<>();
    private ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Cloud = new ArrayList<>();
    private ArrayList<ShapeableImageView> main_LAYOUT_Of_Witches = new ArrayList<>();
    private ShapeableImageView main_IMG_background;
    private ExtendedFloatingActionButton main_FAB_left, main_FAB_right;
    private MaterialButton main_BTN_submit;
    private MaterialTextView main_LBL_score;
    private FrameLayout main_FRAME_name;
    private EditText main_EDITTEXT_newName;

    Gson gson = new Gson();
    private FusedLocationProviderClient fusedLocationClient;
    private MyTImer myTImer;
    private GameManager gameManager;
    private MyMatrix myMatrix;
    private DataBase dataBase = new DataBase();
    private StepDetector stepDetector;


    private int directionRight;
    private int directionLeft;
    private boolean sensorsMode;

    private int numOfWitches;
    private int numOfRowsInCloudMatrix, numOfColsInCloudMatrix;
    private int life;

    private int delay;
    private double lat;
    private double lon;
    private boolean speedControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        sensorsMode = intent.getBooleanExtra(getString(R.string.sensorsmode), false);
        speedControl = intent.getBooleanExtra(getString(R.string.speedcontrol), false);
        initGame();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startGame(delay);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myTImer.timerOn();
        stepDetector.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        myTImer.timerOff();
        if (sensorsMode) {
            stepDetector.stop();
        }
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


    private void initGame() {
        findViews();
        initBackground();
        life = main_IMG_hats.size();
        gameManager = new GameManager(main_All_Layouts_Of_Cloud, main_All_Layouts_Of_cauldron, life);
        myMatrix = gameManager.getMyMatrix();
        gameSizes();
        setVisibility();
        initButtonListeners();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkMode();
        directionRight = MOVE_RIGHT;
        directionLeft = MOVE_LEFT;
        delay = SLOW;
        initDetector();

        if (sensorsMode) {
            stepDetector.start();
        }
        if (speedControl) {
            delay = FAST;
        }
    }

    private void checkMode() {
        if (sensorsMode) {
            main_FAB_left.setVisibility(View.INVISIBLE);
            main_FAB_right.setVisibility(View.INVISIBLE);
        }
    }

    private void startGame(int speed) {

        Callback_Timer callBack_timer = new Callback_Timer() {
            @Override
            public void tick() {
                runOnUiThread(() -> updateTimerUI());
            }
        };

        myTImer = new MyTImer(callBack_timer, speed);
    }

    private void initDetector() {
        stepDetector = new StepDetector(this, new Callback_Sensors() {
            @Override
            public void step(int move) {
                if (sensorsMode)
                    checkWitchMove(move);
            }
        }, new Callback_Speed() {
            @Override
            public void speed(int speed) {
                startGame(speed);
            }
        });
    }

    private void updateTimerUI() {
        if (gameManager.isGameOver()) {
            gameOver();
        } else {
            startRunItems();
            startOdometer();
        }

    }

    private void startOdometer() {
        gameManager.setScore(DISTANCE_STATUS);
        main_LBL_score.setText(gameManager.getScore() + "");

    }

    private void startRunItems() {
        advanceItemsInMatrix();
        initNewItemDown(myMatrix);
    }

    // MARK: ADVAACE_IN_MATRIX
    private void advanceItemsInMatrix() {
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
                        checkIfCrush(currentCol, currentValue);
                    }

                }
                if (currentValue == CAULDRON_VALUE) {
                    View currentcauldron = myMatrix.getLayoutscauldronList().get(currentCol).getChildAt(currentRow);
                    currentcauldron.setVisibility(View.INVISIBLE);
                    if (currentRow + 1 <= numOfRowsInCloudMatrix) {
                        View nextcauldron = myMatrix.getLayoutscauldronList().get(currentCol).getChildAt(currentRow + 1);
                        nextcauldron.setVisibility(View.VISIBLE);
                    } else {
                        checkIfCrush(currentCol, currentValue);
                    }
                }
            }
        }
    }

    private void initNewItemDown(MyMatrix myMatrix) {
        int max = myMatrix.getLayoutsCloudList().size() - 1;
        int randomIndex = (int) Math.floor(Math.random() * max * 2);
        if (randomIndex <= max) {
            gameManager.getAdvanceMatrix()[0][randomIndex] = CLOUD_VALUE;
            myMatrix.getLayoutsCloudList().get(randomIndex).getChildAt(0).setVisibility(View.VISIBLE);
        } else {
            myMatrix.getLayoutscauldronList().get(randomIndex % (max + 1)).getChildAt(0).setVisibility(View.VISIBLE);
            gameManager.getAdvanceMatrix()[0][randomIndex % max - 1] = CAULDRON_VALUE;
        }
    }

    private void checkIfCrush(int currentCol, int currentValue) {
        int currentWitch = gameManager.getWitchVisibleIndex();

        if (currentCol == currentWitch) {
            if (currentValue == CLOUD_VALUE) {
                updateCrushUI();
            } else {
                gameManager.setScore(cauldron_STATUS);
            }
            addEffects(currentValue);

        }
    }

    private void addEffects(int currentValue) {
        if (currentValue == CLOUD_VALUE) {
            MySignal.getInstance().sound(R.raw.melting);
            if (gameManager.isGameOver()) {
                MySignal.getInstance().toast(getString(R.string.end_game_toast));
            } else {
                MySignal.getInstance().toast(getString(R.string.crush_toast));
            }
        } else if (currentValue == LOCATION_VALUE) {
            MySignal.getInstance().toast(getString(R.string.Location_toast));
        } else {
            MySignal.getInstance().sound(R.raw.happy);
            MySignal.getInstance().toast(getString(R.string.cauldron_toast));
        }
        MySignal.getInstance().vibrate(VIBRATION);
    }

    // MARK: UPDATE_UI_CRUSH
    private void updateCrushUI() {
        int currentNumOfCrush = gameManager.getNumOfCrush();
        gameManager.updateNumOfCrush();
        if (gameManager.isGameOver()) {
            main_IMG_hats.get(life - 1).setVisibility(View.INVISIBLE);
            main_FRAME_name.setVisibility(View.VISIBLE);
            gameOver();

        } else if (currentNumOfCrush < main_IMG_hats.size()) {
            main_IMG_hats.get(currentNumOfCrush).setVisibility(View.INVISIBLE);
        }

    }

    private void saveThePlayer(int score) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        String playerName = main_EDITTEXT_newName.getText().toString();
                        gameManager.setPlayer(playerName, score, lat, lon);
                        gameManager.addPlayerToDB();

                        if (!main_EDITTEXT_newName.getText().toString().isEmpty()) {
                            changeActivity();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveThePlayer(gameManager.getScore());
            }
        } else {
            addEffects(LOCATION_VALUE);
        }
    }


    private void changeActivity() {
        Intent recordIntent = new Intent(this, RecordsActivity.class);
        recordIntent.putExtra(getString(R.string.sensorsmode), sensorsMode);
        recordIntent.putExtra(getString(R.string.speedcontrol), speedControl);
        startActivity(recordIntent);
        finish();
    }

    private void initButtonListeners() {

        main_FAB_left.setOnClickListener(v -> checkWitchMove(directionLeft));
        main_FAB_right.setOnClickListener(v -> checkWitchMove(directionRight));
        main_BTN_submit.setOnClickListener(v -> saveThePlayer(gameManager.getScore()));
    }

    private void checkWitchMove(int direction) {
        int witchVisibleIndex = gameManager.getWitchVisibleIndex();

        if (direction == directionRight) {
            moveWitch(MOVE_RIGHT, witchVisibleIndex);

        } else if (direction == directionLeft) {
            moveWitch(MOVE_LEFT, witchVisibleIndex);
        }

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

        main_FRAME_name.setVisibility(View.INVISIBLE);
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
        main_FRAME_name = findViewById(R.id.main_FRAME_name);
        main_EDITTEXT_newName = findViewById(R.id.main_EDITTEXT_newName);
        main_BTN_submit = findViewById(R.id.main_BTN_submit);

        main_All_Layouts_Of_cauldron.add(findViewById(R.id.main_LAYOUT_cauldron_0));
        main_All_Layouts_Of_cauldron.add(findViewById(R.id.main_LAYOUT_cauldron_1));
        main_All_Layouts_Of_cauldron.add(findViewById(R.id.main_LAYOUT_cauldron_2));
        main_All_Layouts_Of_cauldron.add(findViewById(R.id.main_LAYOUT_cauldron_3));
        main_All_Layouts_Of_cauldron.add(findViewById(R.id.main_LAYOUT_cauldron_4));

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
        main_FAB_left.setVisibility(View.INVISIBLE);
        main_FAB_right.setVisibility(View.INVISIBLE);
        stepDetector.stop();
        myTImer.timerOff();
    }
}