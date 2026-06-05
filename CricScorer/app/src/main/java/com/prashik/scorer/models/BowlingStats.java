package com.prashik.scorer.models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class BowlingStats implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;
    private String playerId;
    private int wickets = 0;
    private double economy = 0;
    private double average = 0;
    private String bestBowling = "";
    private int twoFer = 0;
    private int threeFer = 0;
    private int fiveFer = 0;
    private int fours = 0;
    private int dots = 0;
    private int sixes = 0;

    private int wides = 0;
    private int nos = 0;

    public BowlingStats(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public double getEconomy() {
        return economy;
    }

    public void setEconomy(double economy) {
        this.economy = economy;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public String getBestBowling() {
        return bestBowling;
    }

    public void setBestBowling(String bestBowling) {
        this.bestBowling = bestBowling;
    }

    public int getThreeFer() {
        return threeFer;
    }

    public void setThreeFer(int threeFer) {
        this.threeFer = threeFer;
    }

    public int getFiveFer() {
        return fiveFer;
    }

    public void setFiveFer(int fiveFer) {
        this.fiveFer = fiveFer;
    }
    public int getTwoFer() {
        return twoFer;
    }

    public void setTwoFer(int twoFer) {
        this.twoFer = twoFer;
    }

    public int getFours() {
        return fours;
    }

    public void setFours(int fours) {
        this.fours = fours;
    }

    public int getDots() {
        return dots;
    }

    public void setDots(int dots) {
        this.dots = dots;
    }

    public int getSixes() {
        return sixes;
    }

    public void setSixes(int sixes) {
        this.sixes = sixes;
    }

    @SuppressLint("DefaultLocale")
    public String toJson() {
        return String.format("{\"playerId\":\"%s\",\"wickets\":%d,\"economy\":%f,\"average\":%f,\"bestBowling\":\"%s\",\"threeFer\":%d,\"fiveFer\":%d}", playerId, wickets, economy, average, bestBowling, threeFer, fiveFer);
    }

    public int getWides() {
        return wides;
    }

    public void setWides(int wides) {
        this.wides = wides;
    }

    public int getNos() {
        return nos;
    }

    public void setNos(int nos) {
        this.nos = nos;
    }
}
