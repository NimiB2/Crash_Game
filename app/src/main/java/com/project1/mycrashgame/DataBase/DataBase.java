package com.project1.mycrashgame.DataBase;

import com.project1.mycrashgame.Model.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class DataBase {
    private final int MAX_RECORDS=10;
    private ArrayList<Player> records = new ArrayList<>();

    public DataBase() {
    }

    public ArrayList<Player> getRecords() {
        return records;
    }

    public DataBase setRecords(ArrayList<Player> records) {
        this.records = records;
        return this;
    }
    public DataBase addRecord(Player player){
        if(this.records.size()==MAX_RECORDS){
            removeRecord();
        }
        this.records.add(player);
        sortRecords();
        return this;
    }

    public void removeRecord(){
       this.records.remove(this.records.size()-1);
    }

    public void sortRecords(){
    this.records.sort(Comparator.comparing(Player::getScore).thenComparing(Player::getName));
    }

    @Override
    public String toString() {
        return "DataBase{" +
                "records=" + records +
                '}';
    }
}
