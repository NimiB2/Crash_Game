package com.project1.mycrashgame.Logic;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.project1.mycrashgame.Model.MyMatrix;

import java.util.ArrayList;

public class GameManager {
    private final int DISTANCE = 10;
    private final int BROOM = 100;
    private  int life;
    private int score=0;

    private int [][] advanceMatrix;
    private MyMatrix myMatrix;
    private  int numOfColumns;
    private  int numOfRows;
    private int witchVisibleIndex;
    private int numOfCrush=0;

    public GameManager() {

    }

    public GameManager(ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Cloud,ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Broom) {
        this.life=3;
        this.myMatrix= new MyMatrix(main_All_Layouts_Of_Cloud,main_All_Layouts_Of_Broom);
        this.numOfColumns =this.myMatrix.getLayoutsCloudList().size();
        this.numOfRows=this.myMatrix.getLayoutsCloudList().get(0).getChildCount();
        this.witchVisibleIndex=this.numOfColumns/2;
        this.advanceMatrix= new int[this.numOfRows][this.numOfColumns];
    }

    public GameManager(ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Cloud,ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Broom, int life) {
        this.life=life;
        this.myMatrix= new MyMatrix(main_All_Layouts_Of_Cloud,main_All_Layouts_Of_Broom);
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

    public int getLife() {
        return this.life;
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
    public MyMatrix setMyMatrix(ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Cloud,ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Broom) {
        this.myMatrix= new MyMatrix(main_All_Layouts_Of_Cloud,main_All_Layouts_Of_Broom);
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
            this.score+=BROOM;
        }

    }

    public boolean isGameOver(){
        return getLife()== getNumOfCrush();
    }
}
