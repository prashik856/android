package com.prashik.scorer.models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;

public class BowlingStats implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;
    private String playerId;
    private int inningsBowled = 0;
    private int wickets = 0;
    private double economy = 0;
    private double average = 0;
    private String bestBowling = "0-0";
    private int twoFer = 0;
    private int threeFer = 0;
    private int fiveFer = 0;
    private int fours = 0;
    private int dots = 0;
    private int sixes = 0;

    private int wides = 0;
    private int nos = 0;
    private int numberOfOvers = 0;
    private int extras = 0;
    private int runs = 0;
    private int deliveriesBowled = 0;
    private int legalDeliveriesBowled = 0;
    private int maidensBowled = 0;
    private int bowledWickets = 0;

    private HashMap<String, Boolean> matchesIncluded = new HashMap<>();

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

    public HashMap<String, Boolean> getMatchesIncluded() {
        return matchesIncluded;
    }

    public void setMatchesIncluded(HashMap<String, Boolean> matchesIncluded) {
        this.matchesIncluded = matchesIncluded;
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

    @NonNull
    @Override
    public String toString() {
        return "BowlingStats{" +
                "playerId='" + playerId + '\'' +
                ", wickets=" + wickets +
                ", economy=" + economy +
                ", average=" + average +
                ", bestBowling='" + bestBowling + '\'' +
                ", twoFer=" + twoFer +
                ", threeFer=" + threeFer +
                ", fiveFer=" + fiveFer +
                ", fours=" + fours +
                ", dots=" + dots +
                ", sixes=" + sixes +
                ", wides=" + wides +
                ", nos=" + nos +
                '}';
    }

    public int getInningsBowled() {
        return inningsBowled;
    }

    public void setInningsBowled(int inningsBowled) {
        this.inningsBowled = inningsBowled;
    }

    public int getNumberOfOvers() {
        return numberOfOvers;
    }

    public void setNumberOfOvers(int numberOfOvers) {
        this.numberOfOvers = numberOfOvers;
    }

    public int getExtras() {
        return extras;
    }

    public void setExtras(int extras) {
        this.extras = extras;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getDeliveriesBowled() {
        return deliveriesBowled;
    }

    public void setDeliveriesBowled(int deliveriesBowled) {
        this.deliveriesBowled = deliveriesBowled;
    }

    public int getMaidensBowled() {
        return maidensBowled;
    }

    public void setMaidensBowled(int maidensBowled) {
        this.maidensBowled = maidensBowled;
    }

    public int getBowledWickets() {
        return bowledWickets;
    }

    public void setBowledWickets(int bowledWickets) {
        this.bowledWickets = bowledWickets;
    }

    public int getLegalDeliveriesBowled() {
        return legalDeliveriesBowled;
    }

    public void setLegalDeliveriesBowled(int legalDeliveriesBowled) {
        this.legalDeliveriesBowled = legalDeliveriesBowled;
    }
}
