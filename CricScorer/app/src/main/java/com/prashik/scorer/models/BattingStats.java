package com.prashik.scorer.models;

import android.annotation.SuppressLint;

import java.io.Serializable;

public class BattingStats implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;
    private String playerId;
    private int runs = 0;
    private double battingAverage = 0;
    private double strikeRate = 0;
    private int inningsPlayed = 0;
    private int fours = 0;
    private int sixes = 0;
    private int dots = 0;
    private int bestScore = 0;
    private int twenties = 0;
    private int thirties = 0;
    private int fifties = 0;


    public BattingStats(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public double getBattingAverage() {
        return battingAverage;
    }

    public void setBattingAverage(double battingAverage) {
        this.battingAverage = battingAverage;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public int getThirties() {
        return thirties;
    }

    public void setThirties(int thirties) {
        this.thirties = thirties;
    }

    public int getFifties() {
        return fifties;
    }

    public void setFifties(int fifties) {
        this.fifties = fifties;
    }

    public int getInningsPlayed() {
        return inningsPlayed;
    }

    public void setInningsPlayed(int inningsPlayed) {
        this.inningsPlayed = inningsPlayed;
    }

    public double getStrikeRate() {
        return strikeRate;
    }

    public void setStrikeRate(double strikeRate) {
        this.strikeRate = strikeRate;
    }

    public int getFours() {
        return fours;
    }

    public void setFours(int fours) {
        this.fours = fours;
    }

    public int getSixes() {
        return sixes;
    }

    public void setSixes(int sixes) {
        this.sixes = sixes;
    }

    public int getDots() {
        return dots;
    }

    public void setDots(int dots) {
        this.dots = dots;
    }

    @SuppressLint("DefaultLocale")
    public String toJson() {
        return String.format("{\"playerId\":\"%s\",\"runs\":%d,\"battingAverage\":%f,"
                + "\"bestScore\":%d,\"thirties\":%d,\"fifties\":%d,\"inningsPlayed\":%d}"
                , playerId, runs, battingAverage, bestScore, thirties, fifties, inningsPlayed);
    }

    public int getTwenties() {
        return twenties;
    }

    public void setTwenties(int twenties) {
        this.twenties = twenties;
    }
}
