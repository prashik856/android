package com.prashik.scorer.models;

import java.io.Serializable;

public class Over implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;
    // the over which it was bowled
    int matchOverId;
    String PlayerName;
    String overSummary;
    int legalDeliveries;
    int maxLegalDeliveries = 6;
    int wides;
    int noBalls;
    int byes;

    public Over(int matchOverId) {
        this.matchOverId = matchOverId;
    }

    public String getOverSummary() {
        return overSummary;
    }

    public void setOverSummary(String overSummary) {
        this.overSummary = overSummary;
    }

    public int getLegalDeliveries() {
        return legalDeliveries;
    }

    public void setLegalDeliveries(int legalDeliveries) {
        this.legalDeliveries = legalDeliveries;
    }

    public int getMaxLegalDeliveries() {
        return maxLegalDeliveries;
    }

    public void setMaxLegalDeliveries(int maxLegalDeliveries) {
        this.maxLegalDeliveries = maxLegalDeliveries;
    }

    public int getWides() {
        return wides;
    }

    public void setWides(int wides) {
        this.wides = wides;
    }

    public int getNoBalls() {
        return noBalls;
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
}
