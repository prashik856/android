package com.prashik.scorer.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Over implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;
    // the over which it was bowled
    private int matchOverId;
    private String PlayerName = "";
    ArrayList<String> overSummary = new ArrayList<>();
    private int legalDeliveries = 0;
    private int maxLegalDeliveries = 6;
    private int wides = 0;
    private int noBalls = 0;
    private int byes = 0;
    private int dots = 0;
    private int extras = 0;
    private boolean overCompleted = false;

    public Over(int matchOverId) {
        this.matchOverId = matchOverId;
    }

    public ArrayList<String> getOverSummary() {
        return overSummary;
    }

    public void setOverSummary(ArrayList<String> overSummary) {
        this.overSummary = overSummary;
    }

    public int getLegalDeliveries() {
        return legalDeliveries;
    }
    public void incrementLegalDeliveries() {
        this.legalDeliveries = this.legalDeliveries + 1;
    }

    public int getMaxLegalDeliveries() {
        return maxLegalDeliveries;
    }

    public int getWides() {
        return wides;
    }
    public void incrementWides() {
        this.wides = this.wides + 1;
    }

    public int getNoBalls() {
        return noBalls;
    }
    public void incrementNoBalls() {
        this.noBalls = this.noBalls + 1;
    }

    public void setNoBalls(int noBalls) {
        this.noBalls = noBalls;
    }

    public int getByes() {
        return byes;
    }

    public void setByes(int byes) {
        this.byes = byes;
    }

    public int getMatchOverId() {
        return matchOverId;
    }

    public void setMatchOverId(int matchOverId) {
        this.matchOverId = matchOverId;
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public void setPlayerName(String playerName) {
        PlayerName = playerName;
    }

    public boolean isOverCompleted() {
        return overCompleted;
    }

    public void setOverCompleted(boolean overCompleted) {
        this.overCompleted = overCompleted;
    }

    public int getDots() {
        return dots;
    }
    public void incrementDotBalls() {
        this.dots = this.dots + 1;
    }

    public void setDots(int dots) {
        this.dots = dots;
    }

    public int getExtras() {
        return extras;
    }

    public void setExtras(int extras) {
        this.extras = extras;
    }

    public void setLegalDeliveries(int legalDeliveries) {
        this.legalDeliveries = legalDeliveries;
    }

    public void setMaxLegalDeliveries(int maxLegalDeliveries) {
        this.maxLegalDeliveries = maxLegalDeliveries;
    }

    public void setWides(int wides) {
        this.wides = wides;
    }

    public void updateOverCompleted() {
        if(this.legalDeliveries >= this.maxLegalDeliveries) {
            this.overCompleted = true;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "Over{" +
                "matchOverId=" + matchOverId +
                ", PlayerName='" + PlayerName + '\'' +
                ", overSummary=" + overSummary +
                ", legalDeliveries=" + legalDeliveries +
                ", maxLegalDeliveries=" + maxLegalDeliveries +
                ", wides=" + wides +
                ", noBalls=" + noBalls +
                ", byes=" + byes +
                ", dots=" + dots +
                ", extras=" + extras +
                ", overCompleted=" + overCompleted +
                '}';
    }
}
