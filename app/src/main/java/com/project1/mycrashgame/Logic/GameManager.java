package com.project1.mycrashgame.Logic;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.project1.mycrashgame.Model.MyMatrix;

import java.util.ArrayList;

public class GameManager {
    private  int life;
    private MyMatrix myMatrix;
    private  int numOfColumns;
    private  int numOfRows;
    private int witchVisibleIndex;
    private int numOfCrush;

    public GameManager() {

    }


    public GameManager(ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Cloud) {
        this.life=3;
        this.numOfCrush=0;
        this.myMatrix= new MyMatrix(main_All_Layouts_Of_Cloud);
        this.numOfColumns =this.myMatrix.getLayoutsCloudList().size();
        this.numOfRows=this.myMatrix.getLayoutsCloudList().get(0).getChildCount();
        this.witchVisibleIndex=this.numOfColumns/2;
    }

    public GameManager(ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Cloud, int life) {
        this.life=life;
        this.numOfCrush=0;
        this.myMatrix= new MyMatrix(main_All_Layouts_Of_Cloud);
        this.numOfColumns =this.myMatrix.getLayoutsCloudList().size();
        this.numOfRows=this.myMatrix.getLayoutsCloudList().get(0).getChildCount();
        this.witchVisibleIndex=this.numOfColumns/2;
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
    public MyMatrix setMyMatrix(ArrayList<LinearLayoutCompat> main_All_Layouts_Of_Cloud) {
        this.myMatrix= new MyMatrix(main_All_Layouts_Of_Cloud);
        return this.myMatrix;
    }

}
