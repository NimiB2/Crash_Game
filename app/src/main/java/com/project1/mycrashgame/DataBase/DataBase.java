package com.project1.mycrashgame.DataBase;

import com.project1.mycrashgame.Model.Player;

import java.util.ArrayList;
import java.util.Comparator;

public class DataBase {
    private final int MAX_RECORDS = 10;
    private  ArrayList<Player> records ;


    public DataBase() {
        records = new ArrayList<>();
    }

    public  ArrayList<Player> getRecords() {
        return records;
    }

    public DataBase setRecords(ArrayList<Player> records) {
        this.records = records;
        return this;
    }

    public void addRecord(Player player){
        if(records.size()== MAX_RECORDS){
            removeRecord();
        }
        records.add(player);
        sortRecords();
    }


    public void removeRecord(){
       records.remove(records.size()-1);
    }

    public void sortRecords(){
    records.sort(Comparator.comparing(Player::getScore).reversed().thenComparing(Player::getName));
    }

}
