package com.prashik.scorer.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class MatchPlayerBatting implements Serializable {
    // Batting
    private static final long serialVersionUID = 2462471862401256640L;
    private boolean batted = false;
    private int runsScored = 0;
    private int ballsPlayed = 0;
    private double strikeRate = 0;
    private int foursScored = 0;
    private int sixesScored = 0;
    private int dotsPlayed = 0;
    private boolean twenty = false;
    private boolean thirty = false;
    private boolean fifty = false;
    private ArrayList<String> battingDetails = new ArrayList<>();
    private boolean out = false;
    String coughtBy = "";
    String bowledBy = "";
    String runOutBy = "";
    String wicketBy = "";

    public MatchPlayerBatting() {

    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public boolean isBatted() {
        return batted;
    }

    public void setBatted(boolean batted) {
        this.batted = batted;
    }

    public double getStrikeRate() {
        return strikeRate;
    }

    public void setStrikeRate(double strikeRate) {
        this.strikeRate = strikeRate;
    }

    public boolean isTwenty() {
        return twenty;
    }
    public void updateRecords() {
        if(this.runsScored >= 20 && this.runsScored < 30) {
            this.twenty = true;
            this.thirty = false;
            this.fifty = false;
        }

        if(this.runsScored >= 30 && this.runsScored < 50) {
            this.twenty = false;
            this.thirty = true;
            this.fifty = false;
        }

        if(this.runsScored >= 50) {
            this.fifty = true;
            this.thirty = false;
            this.twenty = false;
        }
    }

    public void setTwenty(boolean twenty) {
        this.twenty = twenty;
    }

    public boolean isThirty() {
        return thirty;
    }

    public void setThirty(boolean thirty) {
        this.thirty = thirty;
    }

    public boolean isFifty() {
        return fifty;
    }

    public void setFifty(boolean fifty) {
        this.fifty = fifty;
    }

    public ArrayList<String> getBattingDetails() {
        return battingDetails;
    }
    public void addToBattingDetails(String str) {
        this.battingDetails.add(str);
    }
    public void removeLastFromBattingDetails() {
        int size = this.battingDetails.size();
        this.battingDetails.remove(size - 1);
    }

    public void setBattingDetails(ArrayList<String> battingDetails) {
        this.battingDetails = battingDetails;
    }

    public int getRunsScored() {
        return runsScored;
    }
    public void incrementRunsScored() {
        this.runsScored = this.runsScored + 1;
    }
    public void decrementRunsScored() {
        this.runsScored = this.runsScored - 1;
    }
    public void addTwoToRunsScored() {
        this.runsScored = this.runsScored + 2;
    }
    public void decrementTwoFromRunsScored() {
        this.runsScored = this.runsScored - 2;
    }
    public void addThreeToRunsScored() {
        this.runsScored = this.runsScored + 3;
    }
    public void decrementThreeFromRunsScored() {
        this.runsScored = this.runsScored - 3;
    }
    public void addFourToRunsScored() {
        this.runsScored = this.runsScored + 4;
    }
    public void decrementFourFromRunsScored() {
        this.runsScored = this.runsScored - 4;
    }
    public void addFiveToRunsScored() {
        this.runsScored = this.runsScored + 5;
    }
    public void decrementFiveFromRunsScored() {
        this.runsScored = this.runsScored - 5;
    }
    public void addSixToRunsScored() {
        this.runsScored = this.runsScored + 6;
    }
    public void decrementSixFromRunsScored() {
        this.runsScored = this.runsScored - 6;
    }
    public void addSevenToRunsScored() {
        // no ball + 6
        this.runsScored = this.runsScored + 7;
    }

    public void decrementSevenFromRunsScored() {
        this.runsScored = this.runsScored - 7;
    }

    public void setRunsScored(int runsScored) {
        this.runsScored = runsScored;
    }

    public int getBallsPlayed() {
        return ballsPlayed;
    }
    public void incrementBallsPlayed() {
        this.ballsPlayed = this.ballsPlayed + 1;
    }
    public void decrementBallsPlayed() {
        this.ballsPlayed = this.ballsPlayed - 1;
    }

    public void setBallsPlayed(int ballsPlayed) {
        this.ballsPlayed = ballsPlayed;
    }

    public int getFoursScored() {
        return foursScored;
    }
    public void incrementFoursScored() {
        this.foursScored = this.foursScored + 1;
    }
    public void decrementFoursScored() {
        this.foursScored = this.foursScored - 1;
    }

    public void setFoursScored(int foursScored) {
        this.foursScored = foursScored;
    }

    public int getSixesScored() {
        return sixesScored;
    }
    public void incrementSixesScored() {
        this.sixesScored = this.sixesScored + 1;
    }
    public void decrementSixesScored() {
        this.sixesScored = this.sixesScored - 1;
    }

    public void setSixesScored(int sixesScored) {
        this.sixesScored = sixesScored;
    }

    public int getDotsPlayed() {
        return dotsPlayed;
    }
    public void incrementDotsPlayed() {
        this.dotsPlayed = this.dotsPlayed + 1;
    }
    public void decrementDotsPlayed() {
        this.dotsPlayed = this.dotsPlayed - 1;
    }

    public void setDotsPlayed(int dotsPlayed) {
        this.dotsPlayed = dotsPlayed;
    }

    public String getCoughtBy() {
        return coughtBy;
    }

    public void setCoughtBy(String coughtBy) {
        this.coughtBy = coughtBy;
    }

    public String getBowledBy() {
        return bowledBy;
    }

    public void setBowledBy(String bowledBy) {
        this.bowledBy = bowledBy;
    }

    public String getRunOutBy() {
        return runOutBy;
    }

    public void setRunOutBy(String runOutBy) {
        this.runOutBy = runOutBy;
    }

    public String getWicketBy() {
        return wicketBy;
    }

    public void setWicketBy(String wicketBy) {
        this.wicketBy = wicketBy;
    }

    public void updateStrikeRate() {
        if(this.ballsPlayed > 0) {
            this.strikeRate = ((double) this.runsScored /this.ballsPlayed) * 100;
        } else {
            this.strikeRate = 0;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "MatchPlayerBatting{" +
                "batted=" + batted +
                ", runsScored=" + runsScored +
                ", ballsPlayed=" + ballsPlayed +
                ", strikeRate=" + strikeRate +
                ", foursScored=" + foursScored +
                ", sixesScored=" + sixesScored +
                ", dotsPlayed=" + dotsPlayed +
                ", twenty=" + twenty +
                ", thirty=" + thirty +
                ", fifty=" + fifty +
                ", battingDetails=" + battingDetails +
                ", out=" + out +
                ", coughtBy='" + coughtBy + '\'' +
                ", bowledBy='" + bowledBy + '\'' +
                ", runOutBy='" + runOutBy + '\'' +
                '}';
    }
}
