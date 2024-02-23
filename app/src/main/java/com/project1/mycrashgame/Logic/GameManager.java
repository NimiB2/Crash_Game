package com.project1.mycrashgame.Logic;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.gson.Gson;
import com.project1.mycrashgame.DataBase.DataBase;
import com.project1.mycrashgame.Model.MyMatrix;
import com.project1.mycrashgame.Model.Player;
import com.project1.mycrashgame.Utilities.SharedPreferencesManager;

import java.util.ArrayList;

public class GameManager {
    private static final String RECOREDS_LIST = "RECOREDS_LIST";
    private final int DISTANCE = 10;
    private final int cauldron = 100;
    private final int NUM_OF_LIFE = 3;
    private  int life;
    private int score=0;
    private Player player;
    private Gson gson;
    private DataBase dataBase;
    private int [][] advanceMatrix;
    private MyMatrix myMatrix;
    private  int numOfColumns;
    private  int numOfRows;
    private int witchVisibleIndex;
    private int numOfCrush=0;

    public GameManager() {

    }

    public GameManager(ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Cloud,ArrayList<LinearLayoutCompat> main_All_Layouts_Of_cauldron) {
        this.life=NUM_OF_LIFE;
        this.myMatrix= new MyMatrix(main_All_Layouts_Of_Cloud,main_All_Layouts_Of_cauldron);
        this.numOfColumns =this.myMatrix.getLayoutsCloudList().size();
        this.numOfRows=this.myMatrix.getLayoutsCloudList().get(0).getChildCount();
        this.witchVisibleIndex=this.numOfColumns/2;
        this.advanceMatrix= new int[this.numOfRows][this.numOfColumns];
    }

    public GameManager(ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Cloud,ArrayList<LinearLayoutCompat> main_All_Layouts_Of_cauldron, int life) {
        this.life=life;
        this.myMatrix= new MyMatrix(main_All_Layouts_Of_Cloud,main_All_Layouts_Of_cauldron);
        this.numOfColumns =this.myMatrix.getLayoutsCloudList().size();
        this.numOfRows=this.myMatrix.getLayoutsCloudList().get(0).getChildCount();
        this.witchVisibleIndex=this.numOfColumns/2;
        this.advanceMatrix= new int[this.numOfRows][this.numOfColumns];
    }

    public int[][] getAdvanceMatrix() {
        return advanceMatrix;
    }

    public int getNumOfCols() {
        return this.numOfColumns;
    }
    public int getNumOfRows() {
        return numOfRows;
    }

    public int getNumOfCrush() {
        return this.numOfCrush;
    }

    public MyMatrix getMyMatrix() {
        return myMatrix;
    }

    public void updateNumOfCrush() {
        this.numOfCrush ++;
    }
    public int getWitchVisibleIndex() {
        return witchVisibleIndex;
    }

    public void setWitchVisibleIndex(int witchVisibleIndex) {
        this.witchVisibleIndex = witchVisibleIndex;
    }
    public MyMatrix setMyMatrix(ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Cloud,ArrayList<LinearLayoutCompat> main_All_Layouts_Of_cauldron) {
        this.myMatrix= new MyMatrix(main_All_Layouts_Of_Cloud,main_All_Layouts_Of_cauldron);
        return this.myMatrix;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int status) {
        if(status==0){
            this.score += DISTANCE;
        }
       else if(status==1){
            this.score+=cauldron;
        }

    }

    public void setDataBase(Gson gson) {
        String fromSP=SharedPreferencesManager.getInstance().getString(RECOREDS_LIST,"");
        this.dataBase= gson.fromJson(fromSP,DataBase.class );
        if (dataBase==null){
            this.dataBase=new DataBase();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(String name,int score,double lat,double lon) {
        this.player = new Player()
                .setName(name)
                .setScore(score)
                .setLon(lon)
                .setLat(lat);
    }

    public void addPlayerToDB(){
        gson=new Gson();
        setDataBase(gson);
        this.dataBase.addRecord(this.getPlayer());
        this.player.setPosition(this.dataBase.getRecords().indexOf(this.player));
        String spDB= gson.toJson(this.dataBase);
        SharedPreferencesManager.getInstance().putString(RECOREDS_LIST,spDB);
    }



    public boolean isGameOver(){
        return this.life== this.numOfCrush;
    }
}
