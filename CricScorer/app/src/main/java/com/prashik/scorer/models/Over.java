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
    private int runs = 0;
    private boolean maiden = false;
    private boolean overCompleted = false;
    private int wickets = 0;

    public Over(int matchOverId) {
        this.matchOverId = matchOverId;
    }

    public ArrayList<String> getOverSummary() {
        return overSummary;
    }

    public void addToOverSummary(String str) {
        this.overSummary.add(str);
    }

    public void removeLastElementFromOverSummary() {
        int size = this.overSummary.size();
        this.overSummary.remove(size - 1);
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

    public void decrementLegalDeliveries() {
        this.legalDeliveries = this.legalDeliveries - 1;
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

    public void decrementDotBalls() {
        this.dots = this.dots - 1;
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

    public int getRuns() {
        return runs;
    }
    public void incrementRuns() {
        this.runs = this.runs + 1;
    }
    public void decrementRuns() {
        this.runs = this.runs - 1;
    }
    public void addTwoToRuns() {
        this.runs = this.runs + 2;
    }
    public void decrementTwoFromRuns() {
        this.runs = this.runs - 2;
    }
    public void addThreeToRuns() {
        this.runs = this.runs + 3;
    }
    public void decrementThreeFromRuns() {
        this.runs = this.runs - 3;
    }
    public void addFourToRuns() {
        this.runs = this.runs + 4;
    }
    public void decrementFourFromRuns() {
        this.runs = this.runs - 4;
    }
    public void addFiveToRuns() {
        this.runs = this.runs + 5;
    }
    public void decrementFiveFromRuns() {
        this.runs = this.runs - 5;
    }
    public void addSixToRuns() {
        this.runs = this.runs + 6;
    }
    public void decrementSixFromRuns() {
        this.runs = this.runs - 6;
    }
    public void addSevenToRuns() {
        // no ball + 6
        this.runs = this.runs + 7;
    }

    public void decrementSevenFromRuns() {
        this.runs = this.runs - 7;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public boolean isMaiden() {
        return maiden;
    }

    public void setMaiden(boolean maiden) {
        this.maiden = maiden;
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public void incrementWickets() {
        this.wickets = this.wickets + 1;
    }
    public void decrementWickets() {
        this.wickets = this.wickets - 1;
    }

    public void updateMainden() {
        if(this.runs == 0) {
            this.maiden = true;
        }
    }

    public void updateOverCompleted() {
        this.overCompleted = false;
        if(this.legalDeliveries >= this.maxLegalDeliveries) {
            this.overCompleted = true;
            this.updateMainden();
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
                ", runs=" + runs +
                ", maiden=" + maiden +
                ", overCompleted=" + overCompleted +
                ", wickets=" + wickets +
                '}';
    }
}
