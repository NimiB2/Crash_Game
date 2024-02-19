package com.project1.mycrashgame.Model;

public class Player {
    private String name ="";
    private long score=0;
    private double lat=0.0;
    private double lon=0.0;

    public Player() {
    }
    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public long getScore() {
        return score;
    }

    public Player setScore(long score) {
        this.score = score;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Player setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public Player setLon(double lon) {
        this.lon = lon;
        return this;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
